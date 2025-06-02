package com.example.seeya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.seeya.data.repository.AuthRepository
import com.example.seeya.data.repository.ClubsRepository
import com.example.seeya.data.repository.EventRepository
import com.example.seeya.data.repository.SearchRepository
import com.example.seeya.ui.theme.SeeYaTheme
import com.example.seeya.viewmodel.ThemeViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.auth.AuthViewModelFactory
import com.example.seeya.viewmodel.clubs.ClubsViewModel
import com.example.seeya.viewmodel.clubs.ClubsViewModelFactory
import com.example.seeya.viewmodel.event.EventViewModel
import com.example.seeya.viewmodel.event.EventViewModelFactory
import com.example.seeya.viewmodel.search.SearchViewModel
import com.example.seeya.viewmodel.search.SearchViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var eventViewModel: EventViewModel
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var clubsViewModel: ClubsViewModel

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

        clubsViewModel = ViewModelProvider(
            this,
            ClubsViewModelFactory(application, ClubsRepository(application))
        )[ClubsViewModel::class.java]

        val startDestination = intent?.data?.let { uri ->
            val eventId = uri.lastPathSegment
            if (eventId != null) {
                "event/$eventId"
            } else {
                "splash"
            }
        } ?: "splash"

        val themeViewModel: ThemeViewModel = ThemeViewModel(application)

        setContent {
            SeeYaTheme(darkTheme = themeViewModel.isDarkMode.value) {
                AppNavigation(
                    startDestination = startDestination,
                    authViewModel = authViewModel,
                    eventViewModel = eventViewModel,
                    searchViewModel = searchViewModel,
                    clubsViewModel = clubsViewModel,
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}
