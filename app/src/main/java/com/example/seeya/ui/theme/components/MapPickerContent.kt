package com.example.seeya.ui.theme.components

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import kotlinx.coroutines.suspendCancellableCoroutine
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.seeya.ui.theme.Poppins
import kotlinx.coroutines.launch


@Composable
fun MapPickerContent(
    onLocationPicked: (LatLng) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var query by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }

    val placesClient = remember { Places.createClient(context) }
    val cameraPositionState = rememberCameraPositionState()
    var mapLoaded by remember { mutableStateOf(false) }

    val markerState = remember { MarkerState(position = cameraPositionState.position.target) }

    val locationPermissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationPermissionGranted.value = isGranted
        if (isGranted) {
            coroutineScope.launch {
                val current = fetchCurrentLocation(context)
                current?.let {
                    cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(it, 15f))
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        if (!locationPermissionGranted.value) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            val current = fetchCurrentLocation(context)
            current?.let {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
            }
        }
    }

    LaunchedEffect(cameraPositionState.position.target) {
        markerState.position = cameraPositionState.position.target
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                if (it.isNotEmpty()) {
                    val request = FindAutocompletePredictionsRequest.builder()
                        .setQuery(it)
                        .setCountries(listOf("KZ"))
                        .build()

                    placesClient.findAutocompletePredictions(request)
                        .addOnSuccessListener { response ->
                            searchResults = response.autocompletePredictions
                        }
                        .addOnFailureListener {
                            searchResults = emptyList()
                        }
                } else {
                    searchResults = emptyList()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = {
                Text(
                    text = "Find",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            textStyle = TextStyle(
                fontFamily = Poppins,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedPlaceholderColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )

        if (searchResults.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                items(searchResults) { prediction ->

                    val fullText = prediction.getFullText(null)?.toString() ?: "Нет данных"

                    ListItem(
                        headlineContent = {
                            Text(
                                text = fullText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .clickable {
                                val placeId = prediction.placeId
                                val placeFields = listOf(Place.Field.LAT_LNG)

                                val request = FetchPlaceRequest
                                    .builder(placeId, placeFields)
                                    .build()
                                placesClient
                                    .fetchPlace(request)
                                    .addOnSuccessListener { response ->
                                        response.place.latLng?.let { latLng ->
                                            cameraPositionState.move(
                                                CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                                            )
                                            query = prediction
                                                .getFullText(null)
                                                .toString()
                                            searchResults = emptyList()
                                        }
                                    }
                            },
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            headlineColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = { mapLoaded = true },
                properties = MapProperties(
                    isMyLocationEnabled = locationPermissionGranted.value,
                    mapType = MapType.NORMAL
                ),
                uiSettings = MapUiSettings(zoomControlsEnabled = true)
            ) {
                if (mapLoaded) {
                    Marker(
                        state = markerState,
                        title = "Выбранное место",
                        snippet = "Это выбранная точка"
                    )
                }
            }


            Button(
                onClick = {
                    val center = cameraPositionState.position.target
                    onLocationPicked(center)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    text = "Choose Location",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
            }
        }
    }
}


suspend fun fetchCurrentLocation(context: Context): LatLng? {
    if (ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return null
    }

    val fusedClient = LocationServices.getFusedLocationProviderClient(context)
    return suspendCancellableCoroutine { cont ->
        fusedClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    cont.resume(LatLng(location.latitude, location.longitude), null)
                } else {
                    cont.resume(null, null)
                }
            }
            .addOnFailureListener {
                cont.resume(null, null)
            }
    }
}

