package com.example.seeya.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seeya.R
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.search.SearchViewModel

@Composable
fun CustomBottomAppBar(
    authViewModel: AuthViewModel,
    navController: NavController,
    searchViewModel: SearchViewModel,
    bottomBarViewModel: BottomBarViewModel,
    modifier: Modifier = Modifier
) {
    fun navigateTo(destination: String) {
        val baseRoute = destination.substringBefore("/")
        val activeBaseRoute = bottomBarViewModel.activePage.substringBefore("/")

        if (activeBaseRoute == baseRoute && baseRoute != "profile") return

        bottomBarViewModel.onActivePageChange(destination)

        navController.navigate(destination) {
            launchSingleTop = true
            restoreState = false

            when (baseRoute) {
                "main" -> {
                    popUpTo("main") { inclusive = false }
                }
                else -> {
                    popUpTo("main") { saveState = false }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        BottomAppBar(
            modifier = Modifier.fillMaxWidth(0.85f),
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home
                BottomBarItem(
                    icon = {
                        Image(
                            painter = if (bottomBarViewModel.activePage == "main") painterResource(R.drawable.home_icon)
                            else painterResource(R.drawable.home_icon_inactive),
                            contentDescription = "Home",
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    navigate = { navigateTo("main") }
                )

                // Clubs
                BottomBarItem(
                    icon = {
                        Image(
                            painter = if (bottomBarViewModel.activePage == "clubs") painterResource(R.drawable.clubs_icon)
                            else painterResource(R.drawable.clubs_icon_inactive),
                            contentDescription = "Clubs",
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    navigate = { navigateTo("clubs") }
                )

                // Create
                BottomBarItem(
                    icon = {
                        Image(
                            painter = if (bottomBarViewModel.activePage == "createScreen") painterResource(R.drawable.add_icon)
                            else painterResource(R.drawable.add_icon_inactive),
                            contentDescription = "Create",
                            modifier = Modifier.size(45.dp)
                        )
                    },
                    navigate = { navigateTo("createScreen") }
                )

                // Search
                BottomBarItem(
                    icon = {
                        Image(
                            painter = if (bottomBarViewModel.activePage == "search") painterResource(R.drawable.search_icon)
                            else painterResource(R.drawable.search_icon_inactive),
                            contentDescription = "Search",
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    navigate = { navigateTo("search") }
                )

                // Profile
                BottomBarItem(
                    icon = {
                        Image(
                            painter = if (bottomBarViewModel.activePage.contains("profile")) painterResource(R.drawable.profile_icon)
                            else painterResource(R.drawable.user_inactive),
                            contentDescription = "Profile",
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    navigate = {
                        searchViewModel.clearGetUserState()

                        authViewModel.currentUser.value?.id?.let { userId ->
                            navigateTo("profile/$userId")
                        } ?: navigateTo("profile/failed")
                    }
                )
            }
        }
    }
}

@Composable
fun BottomBarItem(
    icon: @Composable () -> Unit,
    navigate: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable {
            navigate()
        }
    ) {
        icon()
    }
}