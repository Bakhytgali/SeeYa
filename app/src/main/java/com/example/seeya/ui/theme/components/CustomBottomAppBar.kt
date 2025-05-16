package com.example.seeya.ui.theme.components

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.viewmodel.BottomBarViewModel

@Composable
fun CustomBottomAppBar(
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
                        Icon(
                            imageVector = Icons.Rounded.Home,
                            contentDescription = "Bottom Bar Home Page Icon",
                            modifier = Modifier.size(30.dp),
                            tint = if (bottomBarViewModel.activePage == "Home") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
                        )
                    },
                    navigate = {
                        if(bottomBarViewModel.activePage != "Home") {
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
                        Icon(
                            imageVector = Icons.Outlined.Group,
                            contentDescription = "Bottom Bar Clubs Page Icon",
                            modifier = Modifier.size(30.dp),
                            tint = if (bottomBarViewModel.activePage == "Clubs") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
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
                        Icon(
                            imageVector = Icons.Rounded.AddCircle,
                            contentDescription = "Bottom Bar Create Page",
                            modifier = Modifier.size(45.dp),
                            tint = MaterialTheme.colorScheme.secondaryContainer
                        )
                    },
                    navigate = {
                        navController.navigate("createEvent") {
                            launchSingleTop = true
                        }
                    }
                )
                BottomBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Bottom Bar Search Page Icon",
                            modifier = Modifier.size(30.dp),
                            tint = if (bottomBarViewModel.activePage == "Search") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
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
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = "Bottom Bar Account Page Icon",
                            modifier = Modifier.size(30.dp),
                            tint = MaterialTheme.colorScheme.secondaryContainer
                        )
                    },
                    navigate = {

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