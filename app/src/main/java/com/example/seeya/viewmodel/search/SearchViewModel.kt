package com.example.seeya.viewmodel.search

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    var searchText by mutableStateOf("")
        private set

    fun onSearchTextChange(newValue: String) {
        searchText = newValue
        searchUser(searchText)
    }

    private val _getUserState = MutableStateFlow<GetUserState>(GetUserState.Idle)
    val getUserState: StateFlow<GetUserState> = _getUserState.asStateFlow()

    private fun searchUser(query: String) {
        viewModelScope.launch {
            _searchState.value = SearchState.Loading
            when (val result = repository.searchUser(query)) {
                is SearchRepository.SearchResult.Success -> {
                    _searchState.value = if (result.users.isNotEmpty()) {
                        SearchState.Success(result.users)
                    } else {
                        SearchState.Empty
                    }
                }
                is SearchRepository.SearchResult.Error -> {
                    _searchState.value = SearchState.Error(
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

            when(val result = repository.getUserById(userId)) {
                is SearchRepository.GetUserResult.Success -> {
                    _getUserState.value = if(result.user != null) {
                        GetUserState.Success(user = result.user)
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
}

sealed class SearchState {
    data object Idle : SearchState()
    data object Loading : SearchState()
    data object Empty : SearchState()
    data class Success(val users: List<SearchUser>) : SearchState()
    data class Error(val message: String, val code: Int? = null) : SearchState()
}

sealed class GetUserState {
    data object Idle: GetUserState()
    data object Loading: GetUserState()
    data object Empty: GetUserState()
    data class Success(val user: User?): GetUserState()
    data class Error(val message: String, val code: Int? = null): GetUserState()
}