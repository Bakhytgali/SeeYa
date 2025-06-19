package com.example.seeya.viewmodel.search

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.seeya.data.model.Event
import com.example.seeya.data.model.SearchUser
import com.example.seeya.data.model.User
import com.example.seeya.data.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    application: Application,
    private val repository: SearchRepository
) : AndroidViewModel(application) {

    var searchText by mutableStateOf("")
        private set

    private val _searchUserState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchUserState: StateFlow<SearchState> = _searchUserState.asStateFlow()

    private val _searchEventsResults = MutableStateFlow<List<Event>>(emptyList())
    val searchEventsResults: StateFlow<List<Event>> = _searchEventsResults.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _getUserState = MutableStateFlow<GetUserState>(GetUserState.Idle)
    val getUserState: StateFlow<GetUserState> = _getUserState.asStateFlow()

    fun onSearchTextChange(newValue: String) {
        searchText = newValue
        searchUser(newValue)
        searchEvents(newValue)
    }

    private fun searchEvents(query: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val result = repository.searchEvents(query)
                _searchEventsResults.value = result
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
                _searchEventsResults.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }

    private fun searchUser(query: String) {
        viewModelScope.launch {
            _searchUserState.value = SearchState.Loading
            when (val result = repository.searchUser(query)) {
                is SearchRepository.SearchUserResult.Success -> {
                    _searchUserState.value = if (result.users.isNotEmpty()) {
                        SearchState.Success(result.users)
                    } else {
                        SearchState.Empty
                    }
                }
                is SearchRepository.SearchUserResult.Error -> {
                    _searchUserState.value = SearchState.Error(
                        message = result.message,
                        code = result.code
                    )
                }
            }
        }
    }

    fun getUserById(userId: String) {
        viewModelScope.launch {
            _getUserState.value = GetUserState.Loading
            when (val result = repository.getUserById(userId)) {
                is SearchRepository.GetUserResult.Success -> {
                    _getUserState.value = if (result.user != null) {
                        GetUserState.Success(result.user)
                    } else {
                        GetUserState.Empty
                    }
                }
                is SearchRepository.GetUserResult.Error -> {
                    _getUserState.value = GetUserState.Error(
                        message = result.message,
                        code = result.code
                    )
                }
            }
        }
    }

    fun clearGetUserState() {
        _getUserState.value = GetUserState.Idle
    }

    fun clearError() {
        _error.value = null
    }
}

sealed class SearchState {
    data object Idle : SearchState()
    data object Loading : SearchState()
    data object Empty : SearchState()
    data class Success(val users: List<SearchUser>) : SearchState()
    data class Error(val message: String, val code: Int? = null) : SearchState()
}

sealed class GetUserState {
    data object Idle : GetUserState()
    data object Loading : GetUserState()
    data object Empty : GetUserState()
    data class Success(val user: User?) : GetUserState()
    data class Error(val message: String, val code: Int? = null) : GetUserState()
}


