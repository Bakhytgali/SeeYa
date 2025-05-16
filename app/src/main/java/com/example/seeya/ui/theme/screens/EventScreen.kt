package com.example.seeya.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seeya.ui.theme.components.EventClubScreenButton
import com.example.seeya.ui.theme.components.EventInfoBottomModal
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.ui.theme.components.ParticipateModal
import com.example.seeya.ui.theme.components.SeeYaLogo
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
    LaunchedEffect(Unit) {
        eventViewModel.getEvent(
            eventId,
            onSuccess = {
                eventViewModel.checkIfParticipating(authViewModel.user.value!!.id!!)
            },
            onError = {}
        )
    }

    val doesParticipate = eventViewModel.isParticipating.collectAsState()
    val isLoading = eventViewModel.eventIsLoading.collectAsState()

    MainScaffold(
        title = "",
        navController = navController,
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Nav Back Icon",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 10.dp)
            )
        },
        bottomBarViewModel = bottomBarViewModel
    ) { mod ->
        Box(
            modifier = mod
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
                                    eventViewModel.checkIfParticipating(authViewModel.user.value!!.id!!)
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

                        if (!event.eventPicture.isNullOrEmpty()) {
                            eventViewModel.decodeBase64ToBitmap(event.eventPicture)?.let { bitmap ->
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = "Event Image",
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

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = event.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        Text(
                            text = event.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = "Participants",
                                    tint = MaterialTheme.colorScheme.secondaryContainer,
                                    modifier = Modifier.size(25.dp)
                                )
                                Text(
                                    text = event.participants?.size?.toString() ?: "0",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                )
                            }

                            Spacer(Modifier.width(35.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Lock,
                                    contentDescription = "Event Type",
                                    tint = MaterialTheme.colorScheme.secondaryContainer,
                                    modifier = Modifier.size(25.dp)
                                )
                                Text(
                                    text = if (event.isOpen) "Open" else "Closed",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                )
                            }
                        }

                        Spacer(Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            if (event.creator.id == authViewModel.user.value!!.id) {
                                EventClubScreenButton(
                                    title = "Manage",
                                    onClick = {},
                                    containerColor = MaterialTheme.colorScheme.background,
                                    textColor = MaterialTheme.colorScheme.onBackground,
                                    borderColor = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.weight(1f)
                                )
                            } else if (doesParticipate.value) {
                                EventClubScreenButton(
                                    title = "Participating",
                                    onClick = {
                                        eventViewModel.joinEvent(
                                            eventId = event.eventId,
                                            onSuccess = {
                                                eventViewModel.onIsParticipateModalOpen(true)
                                            },
                                            onError = {
                                                eventViewModel.onIsParticipateModalOpen(true)
                                            }
                                        )
                                    },
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    textColor = MaterialTheme.colorScheme.primary,
                                    borderColor = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(1f)
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
                                    modifier = Modifier.weight(1f)
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
                                modifier = Modifier.weight(1f)
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
                                title = "0 Images",
                                onClick = {},
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                textColor = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.weight(1f),
                                textPadding = 10,
                                borderColor = MaterialTheme.colorScheme.primaryContainer,
                                fontWeight = FontWeight.Normal
                            )

                            EventClubScreenButton(
                                title = "0 Videos",
                                onClick = {},
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

            // Модальные окна поверх основного контента
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

            if(eventViewModel.eventInfoModalOpen) {
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
}
