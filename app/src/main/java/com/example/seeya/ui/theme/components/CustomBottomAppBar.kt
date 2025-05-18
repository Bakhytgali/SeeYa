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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
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

@Composable
fun CustomBottomAppBar(
    authViewModel: AuthViewModel,
    navController: NavController,
    bottomBarViewModel: BottomBarViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        BottomAppBar(
            modifier = Modifier.fillMaxWidth(0.85f),
            containerColor = bgColor
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomBarItem(
                    icon = {
                        Image(
                            painter = if (bottomBarViewModel.activePage == "Home") painterResource(R.drawable.home_icon) else painterResource(
                                R.drawable.home_icon_inactive
                            ),
                            contentDescription = "Bottom Bar Home Icon",
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    navigate = {
                        if (bottomBarViewModel.activePage != "Home") {
                            navController.navigate("main") {
                                popUpTo("main") { inclusive = true }
                                launchSingleTop = true
                                bottomBarViewModel.onActivePageChange(newValue = "Home")
                            }
                        }
                    }
                )
                BottomBarItem(
                    icon = {
                        Image(
                            painter = if (bottomBarViewModel.activePage == "Clubs") painterResource(
                                R.drawable.clubs_icon
                            ) else painterResource(R.drawable.clubs_icon_inactive),
                            contentDescription = "Bottom Bar Clubs Icon",
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    navigate = {
                        navController.navigate("clubs") {
                            launchSingleTop = true
                            bottomBarViewModel.onActivePageChange(newValue = "Clubs")
                        }
                    }
                )
                BottomBarItem(
                    icon = {
                        Image(
                            painter = if (bottomBarViewModel.activePage == "Create") painterResource(
                                R.drawable.add_icon
                            ) else painterResource(R.drawable.add_icon_inactive),
                            contentDescription = "Bottom Bar Create Icon",
                            modifier = Modifier.size(45.dp)
                        )
                    },
                    navigate = {
                        navController.navigate("createScreen") {
                            launchSingleTop = true
                            bottomBarViewModel.onActivePageChange(newValue = "Create")
                        }
                    }
                )
                BottomBarItem(
                    icon = {
                        Image(
                            painter = if (bottomBarViewModel.activePage == "Search") painterResource(
                                R.drawable.search_icon
                            ) else painterResource(R.drawable.search_icon_inactive),
                            contentDescription = "Bottom Bar Search Icon",
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    navigate = {
                        navController.navigate("search") {
                            popUpTo("search") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                            bottomBarViewModel.onActivePageChange(newValue = "Search")
                        }
                    }
                )
                BottomBarItem(
                    icon = {
                        Image(
                            painter = if (bottomBarViewModel.activePage == "Profile") painterResource(
                                R.drawable.profile_icon
                            ) else painterResource(R.drawable.user_inactive),
                            contentDescription = "Bottom Bar Clubs Icon",
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    navigate = {
                        val userId = authViewModel.user.value?.id

                        if (!userId.isNullOrEmpty()) {
                            navController.navigate("profile/$userId") {
                                popUpTo("profile/{userId}") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                                bottomBarViewModel.onActivePageChange(newValue = "Profile")
                            }
                        } else {
                            navController.navigate("profile/failed") {
                                popUpTo("profile/{userId}") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                                bottomBarViewModel.onActivePageChange(newValue = "Profile")
                            }
                        }
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