package com.example.seeya.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seeya.data.model.Event
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.ui.theme.components.EventCard
import com.example.seeya.ui.theme.components.FilterButton
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.event.EventViewModel

@Composable
fun MainScreen(
    navController: NavController,
    eventViewModel: EventViewModel,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val filters = listOf("For You", "Popular", "By You")
    var activeFilter by remember {
        mutableStateOf(filters[0])
    }

    // ALL EVENTS
    val events = remember { mutableStateOf<List<Event>>(emptyList()) }

    val myEvents = remember { mutableStateOf<List<Event>>(emptyList()) }

    LaunchedEffect(Unit) {
        eventViewModel.getAllEvents(
            onSuccess = { fetchedEvents ->
                events.value = fetchedEvents
            },
            onError = { error ->
                Log.d("MainScreen", error)
            }
        )

        eventViewModel.getMyEvents(
            onSuccess = { fetchedEvents ->
                myEvents.value = fetchedEvents
            },
            onError = { error ->
                Log.d("MainScreen", error)
            }
        )
    }

    MainScaffold(
        title = "Events",
        navController = navController
    ) { mod ->
        Box(
            modifier = mod
                .fillMaxSize()
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.height(20.dp))

                TextButton(
                    onClick = {
                        authViewModel.logout()

                        navController.navigate("login") {
                            popUpTo(0)
                        }
                    }
                ) {
                    Text(
                        text = "Log Out"
                    )
                }

                // Row Of Event Filtering Buttons
                Spacer(modifier = Modifier.height(20.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    filters.forEach { filter ->
                        FilterButton(
                            text = filter,
                            isActive = filter == activeFilter,
                            onClick = {
                                activeFilter = filter
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if(activeFilter == "For You") {
                        items(events.value) { event ->
                            EventCard(
                                event = event,
                                modifier = modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    } else if (activeFilter == "By You") {
                        items(myEvents.value) { event ->
                            EventCard(
                                event = event,
                                modifier = modifier.fillMaxWidth()
                                    .clickable {
                                        navController.navigate("event/${event.eventId}")
                                    }
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
                }
            }
        }
    }
}
