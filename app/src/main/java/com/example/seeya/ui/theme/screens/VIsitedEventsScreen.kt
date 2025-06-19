package com.example.seeya.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seeya.data.model.Event
import com.example.seeya.ui.theme.components.EventCard
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.event.EventViewModel
import com.example.seeya.viewmodel.search.SearchViewModel

@Composable
fun VisitedEventsScreen(
    navController: NavController,
    bottomBarViewModel: BottomBarViewModel,
    searchViewModel: SearchViewModel,
    authViewModel: AuthViewModel,
    eventViewModel: EventViewModel,
    modifier: Modifier = Modifier
) {
    MainScaffold(
        title = "My Events",
        navController = navController,
        bottomBarViewModel = bottomBarViewModel,
        searchViewModel = searchViewModel,
        authViewModel = authViewModel,
    ) { paddingValues ->
        VisitedEventsScreenContent(
            eventViewModel = eventViewModel,
            navController = navController,
            modifier = paddingValues
        )
    }
}

@Composable
fun VisitedEventsScreenContent(
    eventViewModel: EventViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val list = eventViewModel.visitedEvents.collectAsState()
    val isLoading = eventViewModel.loadingVisitedEvents.collectAsState()

    LaunchedEffect(Unit) {
        eventViewModel.getVisitedEvents()
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
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(20.dp))
            if (isLoading.value) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(list.value) { event ->
                        EventCard(
                            event = event,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("event/${event.eventId}") }
                        )
                    }
                }
            }
        }
    }
}