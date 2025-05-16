package com.example.seeya

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seeya.ui.theme.screens.AnimatedSplashScreen
import com.example.seeya.ui.theme.screens.ClubsScreen
import com.example.seeya.ui.theme.screens.CreateEventScreen
import com.example.seeya.ui.theme.screens.EventScreen
import com.example.seeya.ui.theme.screens.LoginScreen
import com.example.seeya.ui.theme.screens.MainScreen
import com.example.seeya.ui.theme.screens.RegisterScreen
import com.example.seeya.ui.theme.screens.SearchScreen
import com.example.seeya.utils.TokenManager
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.event.EventViewModel
import com.example.seeya.viewmodel.search.SearchViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    eventViewModel: EventViewModel,
    searchViewModel: SearchViewModel
) {
    val navController = rememberNavController()
    val startDestination = "splash"

    val bottomBarViewModel: BottomBarViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { fadeIn(animationSpec = tween(500)) },
        exitTransition = { fadeOut(animationSpec = tween(500)) },
    ) {
        composable("splash") {
            AnimatedSplashScreen(navController)
        }

        composable("register") { RegisterScreen(navController, authViewModel) }
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("main") { MainScreen(navController, eventViewModel, authViewModel, bottomBarViewModel) }
        composable("search") { SearchScreen(searchViewModel, navController, bottomBarViewModel) }
        composable("clubs") { ClubsScreen(navController = navController, bottomBarViewModel = bottomBarViewModel) }
        composable("createEvent") {
            CreateEventScreen(navController = navController, eventViewModel = eventViewModel)
        }
        composable("event/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            if (eventId != null) {
                EventScreen(
                    navController = navController,
                    eventId = eventId,
                    eventViewModel = eventViewModel,
                    authViewModel = authViewModel,
                    bottomBarViewModel = bottomBarViewModel
                )
            }
        }
    }
}