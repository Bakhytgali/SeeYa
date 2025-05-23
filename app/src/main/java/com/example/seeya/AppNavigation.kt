package com.example.seeya

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seeya.ui.theme.screens.AnimatedSplashScreen
import com.example.seeya.ui.theme.screens.ClubScreen
import com.example.seeya.ui.theme.screens.ClubsScreen
import com.example.seeya.ui.theme.screens.CreateClubScreen
import com.example.seeya.ui.theme.screens.CreateEventScreen
import com.example.seeya.ui.theme.screens.CreateScreen
import com.example.seeya.ui.theme.screens.EditMyProfileScreen
import com.example.seeya.ui.theme.screens.EventScreen
import com.example.seeya.ui.theme.screens.EventUsersPage
import com.example.seeya.ui.theme.screens.LoginScreen
import com.example.seeya.ui.theme.screens.MainScreen
import com.example.seeya.ui.theme.screens.ManageEventScreen
import com.example.seeya.ui.theme.screens.ProfileScreen
import com.example.seeya.ui.theme.screens.RegisterScreen
import com.example.seeya.ui.theme.screens.SearchScreen
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.clubs.ClubsViewModel
import com.example.seeya.viewmodel.event.EventViewModel
import com.example.seeya.viewmodel.search.SearchViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    eventViewModel: EventViewModel,
    searchViewModel: SearchViewModel,
    clubsViewModel: ClubsViewModel
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
        composable("main") {
            MainScreen(
                navController,
                eventViewModel,
                authViewModel,
                bottomBarViewModel,
                searchViewModel = searchViewModel
            )
        }
        composable("search") {
            SearchScreen(
                searchViewModel,
                navController,
                bottomBarViewModel,
                authViewModel = authViewModel
            )
        }
        composable("clubs") {
            ClubsScreen(
                navController = navController,
                bottomBarViewModel = bottomBarViewModel,
                authViewModel = authViewModel,
                clubsViewModel = clubsViewModel,
                searchViewModel = searchViewModel
            )
        }
        composable("createEvent") {
            CreateEventScreen(navController = navController, eventViewModel = eventViewModel)
        }
        composable("createClub") {
            CreateClubScreen(
                navController = navController,
                clubsViewModel = clubsViewModel,
                bottomBarViewModel = bottomBarViewModel,
                authViewModel = authViewModel
            )
        }
        composable("createScreen") {
            CreateScreen(
                navController = navController,
                bottomBarViewModel = bottomBarViewModel,
                authViewModel = authViewModel,
                searchViewModel = searchViewModel
            )
        }
        composable("manageEvent/{eventId}") {
            val eventId = it.arguments?.getString("eventId")
            if(!eventId.isNullOrEmpty()) {
                ManageEventScreen(
                    eventId = eventId,
                    navController = navController,
                    eventViewModel = eventViewModel
                )
            }
        }
        composable("clubs/{clubId}") { navBackStackEntry ->
            val clubId = navBackStackEntry.arguments?.getString("clubId")

            if(!clubId.isNullOrEmpty()) {
                ClubScreen(
                    navController = navController,
                    clubsViewModel = clubsViewModel,
                    authViewModel = authViewModel,
                    clubId = clubId,
                    bottomBarViewModel = bottomBarViewModel
                )
            }
        }
        composable("profile/{userId}") { navBackStackEntry ->
            val userId = navBackStackEntry.arguments?.getString("userId")

            Log.d("Navigation to Profile", "$userId")

            if (!userId.isNullOrEmpty()) {
                ProfileScreen(
                    searchViewModel = searchViewModel,
                    userId = userId,
                    navController = navController,
                    bottomBarViewModel = bottomBarViewModel,
                    authViewModel = authViewModel
                )
            }
        }
        composable("eventUsers") {
            EventUsersPage(
                eventViewModel = eventViewModel,
                bottomBarViewModel = bottomBarViewModel,
                navController = navController,
                authViewModel = authViewModel,
                searchViewModel = searchViewModel
            )
        }
        composable("editProfile") {
            EditMyProfileScreen(
                navController = navController,
                authViewModel = authViewModel
            )
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