package com.example.seeya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.seeya.data.repository.AuthRepository
import com.example.seeya.data.repository.EventRepository
import com.example.seeya.data.repository.SearchRepository
import com.example.seeya.ui.theme.SeeYaTheme
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.auth.AuthViewModelFactory
import com.example.seeya.viewmodel.event.EventViewModel
import com.example.seeya.viewmodel.event.EventViewModelFactory
import com.example.seeya.viewmodel.search.SearchViewModel
import com.example.seeya.viewmodel.search.SearchViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var eventViewModel: EventViewModel
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(application, AuthRepository(application))
        )[AuthViewModel::class.java]

        eventViewModel = ViewModelProvider(
            this,
            EventViewModelFactory(application, EventRepository(application))
        )[EventViewModel::class.java]

        searchViewModel = ViewModelProvider(
            this,
            SearchViewModelFactory(application, SearchRepository(application))
        )[SearchViewModel::class.java]

        setContent {
            SeeYaTheme {
                AppNavigation(authViewModel, eventViewModel, searchViewModel)
            }
        }
    }
}
