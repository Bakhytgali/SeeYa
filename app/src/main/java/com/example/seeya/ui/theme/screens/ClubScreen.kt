package com.example.seeya.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.data.model.Club
import com.example.seeya.ui.theme.components.*
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.clubs.ClubsViewModel

@Composable
fun ClubScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    clubId: String,
    clubsViewModel: ClubsViewModel,
    bottomBarViewModel: BottomBarViewModel
) {
    Scaffold(
        topBar = {
            SimpleTopBar("") {
                navController.popBackStack()
            }
        }
    ) { paddingValues ->
        ClubScreenContent(
            navController = navController,
            authViewModel = authViewModel,
            clubId = clubId,
            clubsViewModel = clubsViewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}


@Composable
fun ClubScreenContent(
    navController: NavController,
    authViewModel: AuthViewModel,
    clubId: String,
    clubsViewModel: ClubsViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        clubsViewModel.fetchClubById(clubId)
        authViewModel.currentUser.value?.id?.let { userId ->
            clubsViewModel.checkIfParticipating(userId)
        }
    }

    var showAlreadyJoinedDialog by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        when {
            clubsViewModel.isLoading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Loading club...")
                }
            }

            clubsViewModel.club == null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Failed to load club")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {

                    }) {
                        Text("Retry")
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    val club = clubsViewModel.club!!

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        if (!club.clubPicture.isNullOrEmpty()) {
                            clubsViewModel.decodeBase64ToBitmap(club.clubPicture)
                                ?.let { bitmap ->
                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = "Club Image",
                                        modifier = Modifier
                                            .size(150.dp)
                                            .clip(RoundedCornerShape(10.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                        } else {
                            SeeYaLogo(
                                color = MaterialTheme.colorScheme.secondaryContainer
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        ) {
                            Text(
                                text = club.name,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                text = club.description,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.secondaryContainer
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.clickable {
                                        navController.navigate("clubMembers/${clubId}")
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Person,
                                        contentDescription = "Participants",
                                        tint = MaterialTheme.colorScheme.secondaryContainer,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = club.participants.size.toString(),
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.secondaryContainer
                                    )
                                }

                                Spacer(Modifier.width(25.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Lock,
                                        contentDescription = "Club Type",
                                        tint = MaterialTheme.colorScheme.secondaryContainer,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = if (club.isOpen) "Open" else "Closed",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.secondaryContainer
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        if (club.creator.id == authViewModel.currentUser.value!!.id) {
                            EventClubScreenButton(
                                title = "Manage",
                                onClick = {
                                    navController.navigate("manageClub/${clubId}")
                                },
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                textColor = MaterialTheme.colorScheme.onBackground,
                                borderColor = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.weight(1f),
                                textPadding = 5
                            )
                        } else if (clubsViewModel.isParticipating) {
                            EventClubScreenButton(
                                title = "Joined",
                                onClick = {
                                    showAlreadyJoinedDialog = true
                                },
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                textColor = MaterialTheme.colorScheme.primary,
                                borderColor = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f),
                                textPadding = 5
                            )
                        } else {
                            EventClubScreenButton(
                                title = "Join",
                                onClick = {

                                },
                                containerColor = MaterialTheme.colorScheme.primary,
                                textColor = MaterialTheme.colorScheme.background,
                                borderColor = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f),
                                textPadding = 5
                            )
                        }

                        EventClubScreenButton(
                            title = "Info",
                            onClick = {
                                // clubsViewModel.showClubInfoDialog(true)
                            },
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            textColor = MaterialTheme.colorScheme.secondaryContainer,
                            borderColor = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.weight(1f),
                            textPadding = 5
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = "Events",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )

                    Spacer(Modifier.height(10.dp))

                }
            }
        }

//        if (clubsViewModel.showJoinSuccessDialog.value) {
//            AlertDialog(
//                onDismissRequest = { clubsViewModel.showJoinSuccessDialog(false) },
//                confirmButton = {
//                    TextButton(
//                        onClick = { clubsViewModel.showJoinSuccessDialog(false) }
//                    ) {
//                        Text("OK")
//                    }
//                },
//                title = { Text("Success") },
//                text = { Text("You have successfully joined the club!") }
//            )
//        }

        /*        if (clubsViewModel.showJoinErrorDialog.value) {
        AlertDialog(
            onDismissRequest = { clubsViewModel.showJoinErrorDialog(false) },
            confirmButton = {
                TextButton(
                    onClick = { clubsViewModel.showJoinErrorDialog(false) }
                ) {
                    Text("OK")
                }
            },
            title = { Text("Error") },
            text = { Text("Failed to join the club. Please try again.") }
        )
    }

    if (clubsViewModel.showClubInfoDialog.value) {
        clubsViewModel.newClub?.let { club ->
            ClubInfoDialog(
                club = club,
                onDismiss = { clubsViewModel.showClubInfoDialog(false) }
            )
        }
    }*/

        if (showAlreadyJoinedDialog) {
            AlertDialog(
                onDismissRequest = { showAlreadyJoinedDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = { showAlreadyJoinedDialog = false }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showAlreadyJoinedDialog = false
                        }
                    ) {
                        Text("Leave", color = Color.Red)
                    }
                },
                title = { Text("Already Joined") },
                text = { Text("You are already a member of this club.") }
            )
        }
    }
}

@Composable
fun ClubInfoDialog(
    club: Club,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(club.name) },
        text = {
            Column {
                Text("Category: ${club.category}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tags: ${club.clubTags}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Created: ${club.createdAt}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Type: ${if (club.isOpen) "Open" else "Closed"}")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}