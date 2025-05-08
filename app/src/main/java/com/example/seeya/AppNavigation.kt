package com.example.seeya

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seeya.ui.theme.screens.CreateEventScreen
import com.example.seeya.ui.theme.screens.EventScreen
import com.example.seeya.ui.theme.screens.LoginScreen
import com.example.seeya.ui.theme.screens.MainScreen
import com.example.seeya.ui.theme.screens.RegisterScreen
import com.example.seeya.utils.TokenManager
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.event.EventViewModel

@Composable
fun AppNavigation(authViewModel: AuthViewModel, eventViewModel: EventViewModel) {
    val navController = rememberNavController()

    val startDestination = if (!TokenManager.getToken(authViewModel.getApplication()).isNullOrEmpty()) {
        "createEvent"
    } else {
        "main"
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("register") { RegisterScreen(navController, authViewModel) }
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("main") { MainScreen(navController, eventViewModel, authViewModel) }
        composable("createEvent") { CreateEventScreen(navController = navController, eventViewModel = eventViewModel) }
        composable("event/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            if (eventId != null) {
                EventScreen(navController, eventId, eventViewModel)
            }
        }
    }
}