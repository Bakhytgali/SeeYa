package com.example.seeya.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.seeya.ui.theme.components.EventClubScreenButton
import com.example.seeya.ui.theme.components.EventInfoBottomModal
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.ui.theme.components.ParticipateModal
import com.example.seeya.ui.theme.components.SeeYaLogo
import com.example.seeya.ui.theme.components.SimpleTopBar
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.event.EventViewModel

@Composable
fun EventScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    eventId: String,
    eventViewModel: EventViewModel,
    bottomBarViewModel: BottomBarViewModel
) {
    Scaffold(
        topBar = {
            SimpleTopBar(
                ""
            ) {
                navController.popBackStack()
            }
        }
    ) { paddingValues ->
        EventScreenContent(
            navController = navController,
            authViewModel = authViewModel,
            eventId = eventId,
            eventViewModel = eventViewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun EventScreenContent(
    navController: NavController,
    authViewModel: AuthViewModel,
    eventId: String,
    eventViewModel: EventViewModel,
    modifier: Modifier = Modifier
) {

    LaunchedEffect(Unit) {
        eventViewModel.getEvent(
            eventId,
            onSuccess = {
                eventViewModel.checkIfParticipating(authViewModel.currentUser.value!!.id!!)
            },
            onError = {}
        )
    }

    val doesParticipate = eventViewModel.isParticipating.collectAsState()
    val isLoading = eventViewModel.eventIsLoading.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading.value -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Loading event...")
                }
            }

            eventViewModel.event == null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Failed to load event")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        eventViewModel.getEvent(
                            eventId,
                            onSuccess = {
                                eventViewModel.checkIfParticipating(authViewModel.currentUser.value!!.id!!)
                            },
                            onError = {}
                        )
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

                    val event = eventViewModel.event!!

                    Row(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(15.dp)
                            )
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        if (!event.eventPicture.isNullOrEmpty()) {
                            AsyncImage(
                                model = event.eventPicture,
                                contentDescription = "Event Image",
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop
                            )
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
                                text = event.name,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                text = event.eventTags,
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
                                        navController.navigate("eventUsers")
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Person,
                                        contentDescription = "Participants",
                                        tint = MaterialTheme.colorScheme.secondaryContainer,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = event.participants?.size?.toString() ?: "0",
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
                                        contentDescription = "Event Type",
                                        tint = MaterialTheme.colorScheme.secondaryContainer,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = if (event.isOpen) "Open" else "Closed",
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
                        if (event.creator.id == authViewModel.currentUser.value!!.id) {
                            EventClubScreenButton(
                                title = "Manage",
                                onClick = {
                                    navController.navigate("manageEvent/${event.eventId}")
                                },
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                textColor = MaterialTheme.colorScheme.onBackground,
                                borderColor = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.weight(1f),
                                textPadding = 5
                            )
                        } else if (doesParticipate.value) {
                            EventClubScreenButton(
                                title = "Participating",
                                onClick = {
                                    eventViewModel.onAlreadyParticipateModalOpen(true)
                                },
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                textColor = MaterialTheme.colorScheme.primary,
                                borderColor = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f),
                                textPadding = 5
                            )
                        } else {
                            EventClubScreenButton(
                                title = "Participate",
                                onClick = {
                                    eventViewModel.joinEvent(
                                        eventId = event.eventId,
                                        onSuccess = {
                                            eventViewModel.onSetIsParticipating(true)
                                            eventViewModel.onIsParticipateModalOpen(true)
                                        },
                                        onError = {
                                            eventViewModel.onSetIsParticipating(false)
                                            eventViewModel.onIsParticipateModalOpen(true)
                                        }
                                    )
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
                                eventViewModel.onEventInfoModalOpenChange(true)
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
                        text = "Media",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )

                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        EventClubScreenButton(
                            title = "Chat Link",
                            onClick = {},
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            textColor = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.weight(1f),
                            textPadding = 10,
                            borderColor = MaterialTheme.colorScheme.primaryContainer,
                            fontWeight = FontWeight.Normal
                        )

                        val clipboardManager = LocalClipboardManager.current
                        val context = LocalContext.current

                        EventClubScreenButton(
                            title = "Copy Link",
                            onClick = {
                                val eventLink = "https://seeya.app/event/${eventViewModel.event?.eventId ?: ""}"
                                clipboardManager.setText(AnnotatedString(eventLink))
                                Toast.makeText(context, "Link copied to clipboard!", Toast.LENGTH_SHORT).show()
                            },
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            textColor = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.weight(1f),
                            textPadding = 10,
                            borderColor = MaterialTheme.colorScheme.primaryContainer,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }
        if (eventViewModel.isParticipateModalOpen) {
            eventViewModel.event?.let { event ->
                ParticipateModal(
                    event = event,
                    onOk = {
                        eventViewModel.onIsParticipateModalOpen(false)
                    }
                )
            }
        }

        if(eventViewModel.alreadyParticipateModalOpen) {
            EventScreenModal(
                title = "Already Joined!",
                text = "You are already participating in the event",
                onDismiss = {
                    eventViewModel.onAlreadyParticipateModalOpen(false)
                },
                onConfirm = {
                    eventViewModel.onAlreadyParticipateModalOpen(false)
                },
                confirmTitle = "Undo"
            )
        }

        if (eventViewModel.eventInfoModalOpen) {
            eventViewModel.event?.let { event ->
                EventInfoBottomModal(
                    event = event,
                    onDismiss = {
                        eventViewModel.onEventInfoModalOpenChange(false)
                    }
                )
            }
        }
    }
}

@Composable
fun EventScreenModal(
    confirmTitle: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    text: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Ok",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = confirmTitle,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp
                )
            }
        },
        text = {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 25.dp).fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        title = {
            Text(
                text = title,
                modifier = Modifier.padding(top = 15.dp).fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    )
}
