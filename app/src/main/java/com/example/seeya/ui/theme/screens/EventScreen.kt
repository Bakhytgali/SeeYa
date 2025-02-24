package com.example.seeya.ui.theme.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.R
import com.example.seeya.data.model.Event
import com.example.seeya.ui.theme.Poppins
import com.example.seeya.ui.theme.Unbounded
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.ui.theme.components.EventTitleCard
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.ui.theme.components.OptionButton
import com.example.seeya.ui.theme.grayText
import com.example.seeya.ui.theme.primaryColor
import com.example.seeya.ui.theme.secondaryColor
import com.example.seeya.utils.TokenManager
import com.example.seeya.viewmodel.event.EventViewModel
import java.util.Calendar


@Composable
fun EventScreen(
    navController: NavController,
    eventId: String,
    eventViewModel: EventViewModel
) {
    val event = remember { mutableStateOf<Event?>(null) }
    val context = LocalContext.current

    val options = listOf("Description", "Info")
    val activeOption = remember {
        mutableStateOf(options[0])
    }

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
            modifier = mod
                .fillMaxSize()
                .background(bgColor)
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

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Image(
                                painter = painterResource(R.drawable.star),
                                contentDescription = "Organizer Rating Icon"
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = event.value!!.creator.rating.toString(),
                                    fontFamily = Unbounded,
                                    color = secondaryColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "/ 5",
                                    fontFamily = Unbounded,
                                    color = grayText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Image(
                                painter = painterResource(R.drawable.calendar),
                                contentDescription = "Start Date Icon"
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                event.value?.startDate?.let { startDate ->
                                    val calendar = Calendar.getInstance().apply {
                                        time = startDate
                                    }

                                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                                    val month = calendar.get(Calendar.MONTH) + 1
                                    val year = calendar.get(Calendar.YEAR)

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "$day.$month",
                                            fontFamily = Unbounded,
                                            color = primaryColor,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "$year",
                                            fontFamily = Unbounded,
                                            color = grayText,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                } ?: Text(
                                    text = "Date not available",
                                    fontFamily = Unbounded,
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Image(
                                painter = painterResource(R.drawable.group),
                                contentDescription = "Participants Icon"
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = event.value!!.participants?.size.toString(),
                                    fontFamily = Unbounded,
                                    color = primaryColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "/ 20",
                                    fontFamily = Unbounded,
                                    color = grayText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        options.forEach { option ->
                            OptionButton(
                                text = option,
                                isActive = activeOption.value == option,
                                onClick = {
                                    activeOption.value = option
                                },
                            )
                        }
                    }

                    if (activeOption.value == "Description") {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = event.value!!.description,
                            fontSize = 16.sp,
                            color = grayText,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 15.dp)
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                                .padding(vertical = 15.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Organizer",
                                    fontSize = 14.sp,
                                    color = grayText
                                )

                                Text(
                                    text = "${event.value!!.creator.name}  ${event.value!!.creator.surname} (${event.value!!.creator.rating}",
                                    fontSize = 14.sp,
                                    color = primaryColor
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Location",
                                    fontSize = 14.sp,
                                    color = grayText
                                )

                                Text(
                                    text = event.value!!.location,
                                    fontSize = 14.sp,
                                    color = primaryColor
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Category",
                                    fontSize = 14.sp,
                                    color = grayText
                                )

                                Text(
                                    text = event.value!!.category,
                                    fontSize = 14.sp,
                                    color = primaryColor
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Event Media",
                                    fontSize = 14.sp,
                                    color = grayText
                                )

                                Text(
                                    text = "None (To Be Provided)",
                                    fontSize = 14.sp,
                                    color = primaryColor
                                )
                            }
                        }

                    }

                    val currentUser = TokenManager.getUser(context)
                    Log.d("EventScreen", "Current user: $currentUser")

                    val eventButtonStatus = remember {
                        mutableStateOf("")
                    }

                    Log.d(
                        "EventScreen",
                        "Participants: ${currentEvent.participants?.map { it.id }}"
                    )
                    var isUserParticipating =
                        currentEvent.participants?.any { it.id == currentUser?.id } == true
                    eventButtonStatus.value = "You're Participating!"

                    if (!isUserParticipating) {
                        if (currentEvent.creator.id == currentUser?.id) {
                            isUserParticipating = true
                            eventButtonStatus.value = "This Is Your Event"
                        }
                    }

                    if (!isUserParticipating) {
                        Spacer(modifier = Modifier.height(30.dp))

                        Button(
                            onClick = {
                                Log.d(
                                    "EventScreen",
                                    "User attempting to join event: ${currentEvent.eventId}"
                                )

                                eventViewModel.joinEvent(
                                    eventId = currentEvent.eventId,
                                    onSuccess = {
                                        Log.d("EventScreen", "Successfully joined event!")
                                        Toast.makeText(
                                            context,
                                            "Successfully joined!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        eventViewModel.getEvent(
                                            eventId,
                                            onSuccess = { updatedEvent ->
                                                Log.d(
                                                    "EventScreen",
                                                    "Event updated after joining: $updatedEvent"
                                                )
                                                event.value = updatedEvent
                                            },
                                            onError = { errorMsg ->
                                                Log.e(
                                                    "EventScreen",
                                                    "Failed to refresh event! Error: $errorMsg"
                                                )
                                            }
                                        )
                                    },
                                    onError = { errorMsg ->
                                        Log.e(
                                            "EventScreen",
                                            "Failed to join event! Error: $errorMsg"
                                        )
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
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(
                            onClick = {
                                Log.d("EventScreen", "User is already participating in the event.")
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(2.dp, secondaryColor, shape = RoundedCornerShape(10.dp)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = bgColor
                            )
                        ) {
                            Text(
                                text = eventButtonStatus.value,
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

