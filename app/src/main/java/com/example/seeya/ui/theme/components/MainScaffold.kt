package com.example.seeya.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.R
import com.example.seeya.ui.theme.Unbounded
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.ui.theme.primaryColor
import com.example.seeya.ui.theme.primaryContainerColor
import com.example.seeya.ui.theme.secondaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    title: String,
    navController: NavController,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                // TODO
                            }
                        ) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "Open Drawer Menu",
                                tint = primaryColor,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    },
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                fontSize = 28.sp,
                                fontFamily = Unbounded,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = bgColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                BottomAppBar(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    containerColor = bgColor
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BottomBarItem(
                            R.drawable.home_icon,
                            label = "Home",
                            navigate = {
                                navController.navigate("main") {
                                    popUpTo(navController.graph.startDestinationId) {inclusive = false}
                                    launchSingleTop = true
                                }
                            }
                        )
                        BottomBarItem(
                            R.drawable.clubs_icon,
                            label = "Clubs",
                            navigate = {

                            }
                        )
                        BottomBarItem(
                            R.drawable.add_icon,
                            label = "Add",
                            navigate = {
                                navController.navigate("create") {
                                    popUpTo("main") { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        )
                        BottomBarItem(
                            R.drawable.search_icon,
                            label = "Search",
                            navigate = {

                            }
                        )
                        BottomBarItem(
                            R.drawable.profile_icon,
                            label = "Profile",
                            navigate = {

                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        content(Modifier.padding(paddingValues))
    }
}


@Composable
fun BottomBarItem(
    iconRes: Int,
    label: String,
    navigate: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable {
            navigate()
        }
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = "$label Icon"
        )
        Text(
            text = label,
            color = secondaryColor,
            fontSize = 14.sp
        )
    }
}


