package com.example.seeya.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.viewmodel.event.EventViewModel


@Composable
fun EventScreen(
    navController: NavController,
    eventId: String,
    eventViewModel: EventViewModel
) {
    LaunchedEffect(Unit) {
        eventViewModel.getEvent(
            eventId,
            {},
            {}
        )
    }
    MainScaffold(
        title = "",
        navController = navController
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9F)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
//                if(eventViewModel.eventPicture.isNotEmpty()) {
//                    Image(
//                        bitmap = eventViewModel.event!!.eventPicture,
//                        contentDescription = "Event Picture",
//                    )
//                }
            }
        }
    }
}

