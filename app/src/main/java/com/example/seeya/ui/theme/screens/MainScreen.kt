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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.data.model.Event
import com.example.seeya.data.model.EventTags
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.ui.theme.components.EventCard
import com.example.seeya.ui.theme.components.FilterButton
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.utils.TokenManager
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.event.EventViewModel
import com.example.seeya.viewmodel.search.SearchViewModel
import java.time.format.DateTimeFormatter

@Composable
fun MainScreen(
    navController: NavController,
    eventViewModel: EventViewModel,
    authViewModel: AuthViewModel,
    bottomBarViewModel: BottomBarViewModel,
    searchViewModel: SearchViewModel,
    modifier: Modifier = Modifier
) {
    MainScaffold(
        title = "",
        icon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier
                    .padding(start = 25.dp)
                    .clickable {
                        navController.navigate("eventOnMap")
                    }
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = "Astana",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    fontWeight = FontWeight.Medium,
                )
            }
        },
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
        },
        searchViewModel = searchViewModel
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
    val filters = listOf("For You", "By You")
    var activeFilter by remember { mutableStateOf(filters[0]) }

    val events = remember { mutableStateOf<List<Event>>(emptyList()) }
    val myEvents = remember { mutableStateOf<List<Event>>(emptyList()) }

    var expanded by remember { mutableStateOf(false) }
    val allTags = EventTags.entries.toList()
    val selectedTags = remember { mutableStateListOf<EventTags>() }

    LaunchedEffect(Unit) {
        eventViewModel.getAllEvents(
            onSuccess = { fetchedEvents -> events.value = fetchedEvents },
            onError = { error -> Log.d("MainScreen", error) }
        )

        eventViewModel.getMyEvents(
            onSuccess = { fetchedEvents -> myEvents.value = fetchedEvents },
            onError = { error -> Log.d("MainScreen", error) }
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
                .fillMaxWidth(0.95f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Outlined.FilterList,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondaryContainer,
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    allTags.forEach { tag ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = selectedTags.contains(tag),
                                        onCheckedChange = {
                                            if (it) selectedTags.add(tag)
                                            else selectedTags.remove(tag)
                                        }
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = tag.title,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontSize = 14.sp
                                    )
                                }
                            },
                            onClick = {
                                if (selectedTags.contains(tag)) {
                                    selectedTags.remove(tag)
                                } else {
                                    selectedTags.add(tag)
                                }
                            }
                        )
                    }
                }
                filters.forEach { filter ->
                    FilterButton(
                        text = filter,
                        isActive = filter == activeFilter,
                        onClick = { activeFilter = filter },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (selectedTags.isNotEmpty()) {
                    IconButton(onClick = { selectedTags.clear() }) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.secondaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Crossfade(
                targetState = activeFilter,
                animationSpec = tween(durationMillis = 300),
                label = "TabContentAnimation"
            ) { currentFilter ->
                when (currentFilter) {
                    "For You" -> {
                        val selectedTagNames = selectedTags.map { it.title }

                        val filteredEvents = if (selectedTags.isEmpty()) {
                            events.value
                        } else {
                            events.value.filter { event ->
                                event.eventTags in selectedTagNames
                            }
                        }

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(15.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(filteredEvents) { event ->
                                EventCard(
                                    event,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { navController.navigate("event/${event.eventId}") }
                                )
                            }
                        }
                    }

                    "By You" -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(15.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(myEvents.value.reversed()) { event ->
                                EventCard(
                                    event,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            navController.navigate("event/${event.eventId}")
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
