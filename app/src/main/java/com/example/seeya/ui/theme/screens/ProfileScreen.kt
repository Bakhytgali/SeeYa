package com.example.seeya.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.data.model.User
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.ui.theme.components.SeeYaLogo
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel

@Composable
fun ProfileScreen(
    userId: String,
    bottomBarViewModel: BottomBarViewModel,
    navController: NavController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val user = authViewModel.user.value

    MainScaffold(
        title = "User",
        bottomBarViewModel = bottomBarViewModel,
        navController = navController,
        authViewModel = authViewModel,
        content = { mod -> ProfileScreenContent(user = user, authViewModel = authViewModel, modifier = mod) }
    )
}

@Composable
fun ProfileScreenContent(
    user: User?,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val userPicture = if (user?.profilePicture != null)
        authViewModel.decodeBase64ToBitmap(user.profilePicture)
    else
        null

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if(user == null) {
            Text(
                text = "Could not load the profile, sorry!",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(25.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if(userPicture == null) {
                            SeeYaLogo(
                                fontSize = 72,
                                color = MaterialTheme.colorScheme.secondaryContainer
                            )
                        } else {
                            Image(
                                bitmap = userPicture.asImageBitmap(),
                                contentDescription = "User Profile Photo",
                                modifier = Modifier.size(150.dp).clip(RoundedCornerShape(10.dp))
                            )
                        }

                        Text(
                            text = "@${user.username}",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 18.sp,
                        )

                        Text(
                            text = "${user.name} ${user.surname}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                    }
                }

            }
        }
    }
}