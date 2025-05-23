package com.example.seeya.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.data.model.User
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.ui.theme.components.ProfileBottomModal
import com.example.seeya.ui.theme.components.SeeYaLogo
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.search.GetUserState
import com.example.seeya.viewmodel.search.SearchViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ProfileScreen(
    userId: String,
    authViewModel: AuthViewModel,
    searchViewModel: SearchViewModel,
    bottomBarViewModel: BottomBarViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentUser by authViewModel.currentUser.observeAsState()
    val getUserState by searchViewModel.getUserState.collectAsState()

    Log.d("My Log", currentUser?.username ?: "Something")

    val isCurrentUserProfile = userId == currentUser?.id
    val userState = remember { mutableStateOf<User?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId, currentUser) {
        if (isCurrentUserProfile) {
            userState.value = currentUser
            isLoading.value = false
        } else {
            searchViewModel.getUserById(userId)
        }
    }

    LaunchedEffect(getUserState) {
        when (getUserState) {
            is GetUserState.Loading -> {
                isLoading.value = true
                errorMessage.value = null
            }

            is GetUserState.Success -> {
                userState.value = (getUserState as GetUserState.Success).user
                isLoading.value = false
                errorMessage.value = null
            }

            is GetUserState.Error -> {
                errorMessage.value = (getUserState as GetUserState.Error).message
                isLoading.value = false
            }

            else -> {}
        }
    }

    MainScaffold(
        title = if (isCurrentUserProfile) "My Profile" else "User Profile",
        bottomBarViewModel = bottomBarViewModel,
        navController = navController,
        authViewModel = authViewModel,
        searchViewModel = searchViewModel,
        content = { mod ->
            when {
                isLoading.value -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                errorMessage.value != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Error loading profile",
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = errorMessage.value!!,
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(onClick = { searchViewModel.getUserById(userId) }) {
                                Text("Retry")
                            }
                        }
                    }
                }

                userState.value == null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("User not found")
                    }
                }

                else -> {
                    ProfileScreenContent(
                        user = userState.value!!,
                        authViewModel = authViewModel,
                        modifier = mod,
                        isCurrentUserProfile = isCurrentUserProfile,
                        navController = navController
                    )
                }
            }
        }
    )
}

@Composable
fun ProfileScreenContent(
    navController: NavController,
    user: User?,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    isCurrentUserProfile: Boolean = false
) {
    val userPicture = if (user?.profilePicture != null)
        authViewModel.decodeBase64ToBitmap(user.profilePicture)
    else
        null

    var profileOptionsModalOpen by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (user == null) {
            Text(
                text = "Could not load the profile, sorry!",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
        } else {
            if(profileOptionsModalOpen) {
                ProfileBottomModal(
                    onClose = {
                        profileOptionsModalOpen = false
                    },
                    onEditProfile =  {
                        navController.navigate("editProfile") {
                            launchSingleTop = true
                        }
                    },
                    onDeleteAccount =  {

                    },
                    onExitProfile =  {
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                            launchSingleTop = true
                        }
                        authViewModel.logout()
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(top = 20.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {

                    if(isCurrentUserProfile) {
                        IconButton(
                            onClick = {
                                profileOptionsModalOpen = true
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 10.dp, top = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreHoriz,
                                contentDescription = "Account Options",
                                tint = MaterialTheme.colorScheme.secondaryContainer
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .padding(vertical = 25.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (userPicture == null) {
                            SeeYaLogo(
                                fontSize = 90,
                                color = MaterialTheme.colorScheme.secondaryContainer
                            )
                        } else {
                            Image(
                                bitmap = userPicture.asImageBitmap(),
                                contentDescription = "User Profile Photo",
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(20.dp))
                            )
                        }

                        Text(
                            text = "@${user.username}",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 20.sp,
                        )

                        Text(
                            text = "${user.name} ${user.surname}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                    }
                }

                Spacer(Modifier.height(25.dp))

                val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
                val date = inputFormat.parse(user.createdAt.toString())

                val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val formattedDate = outputFormat.format(date)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(25.dp)
                ) {
                    ProfileInfoWidget(
                        widgetInfo = user.visitedEvents.size.toString(),
                        widgetTitle = "Events Visited",
                        modifier = Modifier.weight(1f)
                    )

                    ProfileInfoWidget(
                        widgetInfo = user.joinedClubs.size.toString(),
                        widgetTitle = "Joined Clubs",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(25.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(25.dp)
                ) {
                    ProfileInfoWidget(
                        widgetInfo = "4.9",
                        widgetTitle = "Organizer Rating",
                        widgetInfoColor = Color(0xFF8338ec),
                        modifier = Modifier.weight(1f)
                    )

                    ProfileInfoWidget(
                        widgetInfo = formattedDate,
                        widgetTitle = "Joined SeeYa",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileInfoWidget(
    widgetTitle: String,
    widgetInfo: String,
    modifier: Modifier = Modifier,
    widgetInfoColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = widgetTitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
            Spacer(Modifier.height(10.dp))

            Text(
                text = widgetInfo,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = widgetInfoColor
            )
        }
    }
}