package com.example.seeya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seeya.data.repository.AuthRepository
import com.example.seeya.data.repository.EventRepository
import com.example.seeya.ui.theme.SeeYaTheme
import com.example.seeya.ui.theme.screens.AuthorizeScreen
import com.example.seeya.ui.theme.screens.CreateScreen
import com.example.seeya.ui.theme.screens.EventScreen
import com.example.seeya.ui.theme.screens.LoginScreen
import com.example.seeya.ui.theme.screens.MainScreen
import com.example.seeya.utils.TokenManager
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.auth.AuthViewModelFactory
import com.example.seeya.viewmodel.event.EventViewModel
import com.example.seeya.viewmodel.event.EventViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var eventViewModel: EventViewModel

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

        setContent {
            SeeYaTheme {
                AppNavigation(authViewModel, eventViewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(authViewModel: AuthViewModel, eventViewModel: EventViewModel) {
    val navController = rememberNavController()

    val startDestination = if (!TokenManager.getToken(authViewModel.getApplication()).isNullOrEmpty()) {
        "main"
    } else {
        "login"
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("authorize") { AuthorizeScreen(navController, authViewModel) }
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("main") { MainScreen(navController, eventViewModel, authViewModel) }
        composable("create") { CreateScreen(navController, eventViewModel, authViewModel) }
        composable("event/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            if (eventId != null) {
                EventScreen(navController, eventId, eventViewModel)
            }
        }
    }
}