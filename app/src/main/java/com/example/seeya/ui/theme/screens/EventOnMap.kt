package com.example.seeya.ui.theme.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seeya.data.model.Event
import com.example.seeya.ui.theme.components.fetchCurrentLocation
import com.example.seeya.viewmodel.event.EventViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun EventOnMap(
    navController: NavController,
    eventViewModel: EventViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(true) {
        currentLocation = fetchCurrentLocation(context)
        eventViewModel.getAllEvents(
            onSuccess = { events = it },
            onError = { Log.e("EventOnMap", "Error loading events: $it") }
        )
    }

    currentLocation?.let { location ->
        val markerState = remember(location) { MarkerState(position = location) }

        LaunchedEffect(location) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(location, 13f)
        }

        Scaffold { paddingValues ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                GoogleMap(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    cameraPositionState = cameraPositionState
                ) {
                    events.forEach { event ->
                        val coords = event.locationCoordinates
                            ?.split(",")
                            ?.mapNotNull { it.trim().toDoubleOrNull() }

                        if (coords?.size == 2) {
                            val latLng = LatLng(coords[0], coords[1])
                            Marker(
                                state = MarkerState(position = latLng),
                                title = event.name,
                                snippet = event.location,
                                onClick = {
                                    selectedEvent = event
                                    true // suppress default info window
                                }
                            )
                        }
                    }

                    // User's current location marker
                    Marker(
                        state = markerState,
                        title = "You are here",
                        snippet = "Current location",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                        onClick = { false }
                    )
                }

                // Show AlertDialog when a marker is clicked
                selectedEvent?.let { event ->
                    val coords = event.locationCoordinates
                        ?.split(",")
                        ?.mapNotNull { it.trim().toDoubleOrNull() }

                    val latLng = coords?.takeIf { it.size == 2 }?.let {
                        LatLng(it[0], it[1])
                    }

                    AlertDialog(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        onDismissRequest = { selectedEvent = null },
                        title = { Text(event.name) },
                        text = {
                            Column {
                                Text("Location: ${event.location}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
                                Text("Date: ${event.startDate}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    selectedEvent = null
                                    navController.navigate("event/${event.eventId}")
                                },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("See More", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            }
                        },
                        dismissButton = {
                            Row {
                                TextButton(
                                    onClick = {
                                        selectedEvent = null
                                    }
                                ) {
                                    Text("Cancel", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondaryContainer)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                TextButton(
                                    onClick = {
                                        selectedEvent = null
                                        latLng?.let {
                                            val gmmIntentUri = Uri.parse("geo:${it.latitude},${it.longitude}?q=${it.latitude},${it.longitude}(${Uri.encode(event.name)})")
                                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                            mapIntent.setPackage("com.google.android.apps.maps")
                                            context.startActivity(mapIntent)
                                        }
                                    }
                                ) {
                                    Text("Navigate", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    )
                }
            }
        }
    } ?: Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
