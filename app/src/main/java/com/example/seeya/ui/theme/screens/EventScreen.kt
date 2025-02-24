package com.example.seeya.ui.theme.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.data.model.Event
import com.example.seeya.ui.theme.Poppins
import com.example.seeya.ui.theme.Unbounded
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.ui.theme.components.EventTitleCard
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.ui.theme.secondaryColor
import com.example.seeya.utils.TokenManager
import com.example.seeya.viewmodel.event.EventViewModel


@Composable
fun EventScreen(
    navController: NavController,
    eventId: String,
    eventViewModel: EventViewModel
) {
    val event = remember { mutableStateOf<Event?>(null) }
    val context = LocalContext.current

    Log.d("EventScreen", "Opening event screen for eventId: $eventId")

    LaunchedEffect(Unit) {
        eventViewModel.getEvent(
            eventId,
            onSuccess = { fetchedEvent ->
                Log.d("EventScreen", "Event loaded successfully: $fetchedEvent")
                event.value = fetchedEvent
            },
            onError = { errorMsg ->
                Log.e("EventScreen", "Failed to fetch an event! Error: $errorMsg")
            }
        )
    }

    MainScaffold(
        title = "Event Details",
        navController = navController
    ) { mod ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = mod.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                event.value?.let { currentEvent ->
                    EventTitleCard(currentEvent)

                    Spacer(modifier = Modifier.height(30.dp))

                    val currentUser = TokenManager.getUser(context)
                    Log.d("EventScreen", "Current user: $currentUser")

                    Log.d(
                        "EventScreen",
                        "Participants: ${currentEvent.participants?.map { it.id }}"
                    )

                    val isUserParticipating =
                        currentEvent.participants?.any { it.id == currentUser?.id } == true
                    Log.d("EventScreen", "Is user participating? $isUserParticipating")

                    if (!isUserParticipating) {
                        Button(
                            onClick = {
                                Log.d("EventScreen", "User attempting to join event: ${currentEvent.eventId}")

                                eventViewModel.joinEvent(
                                    eventId = currentEvent.eventId,
                                    onSuccess = {
                                        Log.d("EventScreen", "Successfully joined event!")
                                        Toast.makeText(context, "Successfully joined!", Toast.LENGTH_SHORT).show()

                                        eventViewModel.getEvent(
                                            eventId,
                                            onSuccess = { updatedEvent ->
                                                Log.d("EventScreen", "Event updated after joining: $updatedEvent")
                                                event.value = updatedEvent
                                            },
                                            onError = { errorMsg ->
                                                Log.e("EventScreen", "Failed to refresh event! Error: $errorMsg")
                                            }
                                        )
                                    },
                                    onError = { errorMsg ->
                                        Log.e("EventScreen", "Failed to join event! Error: $errorMsg")
                                        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = secondaryColor
                            )
                        ) {
                            Text(
                                text = "Participate",
                                color = bgColor,
                                fontFamily = Unbounded,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                Log.d("EventScreen", "User is already participating in the event.")
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(2.dp, secondaryColor),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = bgColor
                            )
                        ) {
                            Text(
                                text = "You're Participating",
                                fontFamily = Unbounded,
                                fontWeight = FontWeight.Bold,
                                color = secondaryColor,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }
                    }
                } ?: Text(
                    text = "Failed to fetch the event",
                    color = Color.Red,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

