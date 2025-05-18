package com.example.seeya.ui.theme.screens

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seeya.data.model.Event
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.ui.theme.components.EventCard
import com.example.seeya.ui.theme.components.FilterButton
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.utils.TokenManager
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.event.EventViewModel
import java.time.format.DateTimeFormatter

@Composable
fun MainScreen(
    navController: NavController,
    eventViewModel: EventViewModel,
    authViewModel: AuthViewModel,
    bottomBarViewModel: BottomBarViewModel,
    modifier: Modifier = Modifier
) {
    MainScaffold(
        title = "Events",
        navController = navController,
        bottomBarViewModel = bottomBarViewModel,
        authViewModel = authViewModel,
        content = { mod ->
            MainScreenContent(
                modifier = mod,
                navController = navController,
                bottomBarViewModel = bottomBarViewModel,
                authViewModel = authViewModel,
                eventViewModel = eventViewModel
            )
        }
    )
}

@Composable
fun MainScreenContent(
    eventViewModel: EventViewModel,
    navController: NavController,
    bottomBarViewModel: BottomBarViewModel,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val filters = listOf("Recommended", "Managing")
    var activeFilter by remember {
        mutableStateOf(filters[0])
    }

    val events = remember { mutableStateOf<List<Event>>(emptyList()) }

    val myEvents = remember { mutableStateOf<List<Event>>(emptyList()) }

    val context = LocalContext.current
    val user = TokenManager.getUser(context)

    Log.d("MainScreen", "User: ${user.toString()}")

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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
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
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Crossfade(
                targetState = activeFilter,
                animationSpec = tween(durationMillis = 300),
                label = "TabContentAnimation"
            ) { currentFilter ->
                when (currentFilter) {
                    "Recommended" -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(15.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(events.value.reversed()) { event ->
                                EventCard(
                                    event,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { navController.navigate("event/${event.eventId}") },
                                )
                            }
                        }
                    }

                    "Managing" -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(15.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(myEvents.value.reversed()) { event ->
                                EventCard(event, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                }
            }
        }
    }
}
