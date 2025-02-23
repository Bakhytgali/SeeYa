package com.example.seeya.ui.theme.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seeya.data.model.Creator
import com.example.seeya.data.model.Event
import com.example.seeya.data.model.Participant
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.ui.theme.components.EventCard
import com.example.seeya.ui.theme.components.FilterButton
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.viewmodel.auth.AuthViewModel
import java.util.Calendar

@Composable
fun MainScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val filters = listOf("For You", "Popular", "By You")
    var activeFilter by remember {
        mutableStateOf(filters[0])
    }

    val creator = Creator(
        id = "something",
        name = "Rakhat",
        surname = "Bakhytgali",
        username = "Rocky",
        rating = 5.0
    )

    val participant = Participant(
        id = "something",
        name = "Rakhat",
        surname = "Bakhytgali",
        username = "SOMETHINNNNGGGG"
    )

    val sampleEvent = Event(
        name = "Tech Meetup 2025",
        description = "A networking event for tech enthusiasts, developers, and startups.",
        category = "Technology",
        eventPicture = "https://example.com/event_picture.jpg",
        isClosed = false,
        creator = creator,
        participants = listOf(participant, participant, participant),
        location = "Astana, Kazakhstan",
        startDate = Calendar.getInstance().apply {
            set(2025, Calendar.APRIL, 10, 18, 0)
        }.time,
        createdAt = Calendar.getInstance().time
    )

    val forYouEvents = listOf(
        sampleEvent,
        sampleEvent,
        sampleEvent,
        sampleEvent,
        sampleEvent,
        sampleEvent
    )

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
                    items(forYouEvents) { event ->
                        EventCard(
                            event = event,
                            modifier = modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }
        }
    }
}
