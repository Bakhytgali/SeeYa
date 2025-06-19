package com.example.seeya.ui.theme.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.seeya.ui.theme.screens.CustomTextFieldWithCounter
import com.example.seeya.viewmodel.event.EventViewModel

@Composable
fun ManageEventInfo(
    eventViewModel: EventViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val originalTitle = eventViewModel.event?.name.orEmpty()
    val originalDescription = eventViewModel.event?.description.orEmpty()
    val originalLocation = eventViewModel.event?.location.orEmpty()
    val originalImage = eventViewModel.event?.eventPicture

    LaunchedEffect(Unit) {
        eventViewModel.onEventTitleChange(originalTitle)
        eventViewModel.onEventDescriptionChange(originalDescription)
        eventViewModel.onEventLocationChange(originalLocation)
    }

    var selectedUri: Uri? by remember { mutableStateOf(null) }

    val isAnyFieldChanged = remember {
        derivedStateOf {
            originalTitle != eventViewModel.eventTitle ||
                    originalDescription != eventViewModel.eventDescription ||
                    originalLocation != eventViewModel.eventLocation ||
                    selectedUri != null
        }
    }.value

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedUri = uri
        }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    )
    {
        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            when {
                selectedUri != null -> {
                    eventViewModel.eventPictureUri = selectedUri
                    AsyncImage(
                        model = selectedUri,
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Icon(
                        imageVector = Icons.Default.TouchApp,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.TopEnd)
                    )
                }

                !originalImage.isNullOrBlank() -> {
                    AsyncImage(
                        model = originalImage,
                        contentDescription = "Current Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Icon(
                        imageVector = Icons.Default.TouchApp,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.TopEnd)
                    )
                }

                else -> {
                    SeeYaLogo(fontSize = 74)
                }
            }
        }
        CustomTextFieldWithCounter(
            placeholder = "New Title",
            text = eventViewModel.eventTitle,
            onValueChange = { newValue ->
                eventViewModel.onEventTitleChange(newValue)
            },
            limit = 50,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2,
            fieldHeight = 90
        )

        CustomTextFieldWithCounter(
            placeholder = "New Description",
            text = eventViewModel.eventDescription,
            onValueChange = { newValue ->
                eventViewModel.onEventDescriptionChange(newValue)
            },
            limit = 200,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth(),
            fieldHeight = 200
        )

        CustomTextField(
            placeholder = "Location",
            text = eventViewModel.eventLocation,
            onValueChange = { newValue ->
                eventViewModel.onEventLocationChange(newValue)
            },
            onClick = {
                navController.navigate("map_picker") {
                    restoreState = true
                }
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isActive = false
        )
        CustomTitleButton(
            onClick = {
                val eventId = eventViewModel.event?.eventId
                if (eventId != null) {
                    eventViewModel.updateEventInfo { success, error ->
                        if (success) {
                            navController.popBackStack()
                        } else {
                            Log.d("EVENT UPDATING", error)
                            eventViewModel.clearEntries()
                        }
                    }
                } else {
                    Log.e("NAVIGATION", "eventId is null")
                    eventViewModel.clearEntries()
                }
            },
            title = "Update",
            isActive = isAnyFieldChanged,
        )
    }
}