package com.example.seeya

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.seeya.ui.theme.screens.AddPostScreen
import com.example.seeya.ui.theme.screens.AnimatedSplashScreen
import com.example.seeya.ui.theme.screens.ClubScreen
import com.example.seeya.ui.theme.screens.ClubsScreen
import com.example.seeya.ui.theme.screens.CreateClubScreen
import com.example.seeya.ui.theme.screens.CreateEventScreen
import com.example.seeya.ui.theme.screens.CreateScreen
import com.example.seeya.ui.theme.screens.EditMyProfileScreen
import com.example.seeya.ui.theme.screens.EventMediaScreen
import com.example.seeya.ui.theme.screens.EventOnMap
import com.example.seeya.ui.theme.screens.EventScreen
import com.example.seeya.ui.theme.screens.EventUsersPage
import com.example.seeya.ui.theme.screens.ForgotPasswordScreen
import com.example.seeya.ui.theme.screens.LoginScreen
import com.example.seeya.ui.theme.screens.MainScreen
import com.example.seeya.ui.theme.screens.ManageEventScreen
import com.example.seeya.ui.theme.screens.MapPickerScreen
import com.example.seeya.ui.theme.screens.ProfileScreen
import com.example.seeya.ui.theme.screens.RegisterScreen
import com.example.seeya.ui.theme.screens.SearchScreen
import com.example.seeya.ui.theme.screens.VisitedEventsScreen
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.ThemeViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.clubs.ClubsViewModel
import com.example.seeya.viewmodel.event.EventViewModel
import com.example.seeya.viewmodel.search.SearchViewModel
import java.util.Locale

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    eventViewModel: EventViewModel,
    searchViewModel: SearchViewModel,
    clubsViewModel: ClubsViewModel,
    themeViewModel: ThemeViewModel,
    startDestination: String
) {
    val navController = rememberNavController()

    val bottomBarViewModel: BottomBarViewModel = viewModel()

    val context = LocalContext.current

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
            if (!eventId.isNullOrEmpty()) {
                ManageEventScreen(
                    eventId = eventId,
                    navController = navController,
                    eventViewModel = eventViewModel
                )
            }
        }
        composable("clubs/{clubId}") { navBackStackEntry ->
            val clubId = navBackStackEntry.arguments?.getString("clubId")

            if (!clubId.isNullOrEmpty()) {
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
                    authViewModel = authViewModel,
                    themeViewModel = themeViewModel
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
        composable("forgotPassword") {
            ForgotPasswordScreen(
                authViewModel = authViewModel,
                navController = navController
            )
        }
        composable("editProfile") {
            EditMyProfileScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable(
            "event/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType }),
            deepLinks = listOf(navDeepLink { uriPattern = "seeya://event/{eventId}" })
        ) { backStackEntry ->
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

        composable("eventMedia/{eventId}") {backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            if(!eventId.isNullOrEmpty()) {
                EventMediaScreen(
                    navController = navController,
                    eventViewModel = eventViewModel
                )
            }
        }

        composable("map_picker") {
            MapPickerScreen(
                onBack = { navController.popBackStack() },
                onLocationPicked = { latLng ->
                    eventViewModel.onEventCoordinatesChange("${latLng.latitude},${latLng.longitude}")
                    val placeName = getAddressFromLatLng(context, latLng.latitude, latLng.longitude)
                    eventViewModel.onEventLocationChange(placeName ?: "N/A")
                    navController.popBackStack()
                }
            )
        }


        composable("visitedEvents") {
            VisitedEventsScreen(
                navController = navController,
                authViewModel = authViewModel,
                bottomBarViewModel = bottomBarViewModel,
                searchViewModel = searchViewModel,
                eventViewModel = eventViewModel
            )
        }

        composable("addPost") {
            AddPostScreen(
                navController = navController,
                eventViewModel = eventViewModel,
            )
        }

        composable("eventOnMap") {
            EventOnMap(
                navController = navController,
                eventViewModel = eventViewModel
            )
        }
    }
}

fun getAddressFromLatLng(context: Context, lat: Double, lng: Double): String? {
    return try {
        val geocoder = Geocoder(context, Locale("en")) // англ. язык
        val addresses = geocoder.getFromLocation(lat, lng, 1)
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            val city = address.locality ?: ""
            val addressLine = address.getAddressLine(0)
            addressLine?.substringBeforeLast(city)?.trim(',', ' ')
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}



