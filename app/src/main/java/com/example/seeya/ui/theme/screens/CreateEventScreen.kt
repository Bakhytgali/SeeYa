package com.example.seeya.ui.theme.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seeya.data.model.EventCategory
import com.example.seeya.ui.theme.components.ChooseEventTags
import com.example.seeya.ui.theme.components.CreateEventDetails
import com.example.seeya.ui.theme.components.CreateEventPicture
import com.example.seeya.ui.theme.components.CreateEventSoothe
import com.example.seeya.ui.theme.components.NameEvent
import com.example.seeya.ui.theme.components.SimpleTopBar
import com.example.seeya.viewmodel.event.EventViewModel

@Composable
fun CreateEventScreen(
    navController: NavController,
    eventViewModel: EventViewModel
) {
    var currentStep by remember {
        mutableIntStateOf(0)
    }

    BackHandler {
        if(currentStep > 0) {
            currentStep--
        } else {
            navController.popBackStack()
            eventViewModel.clearEntries()
        }
    }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                eventViewModel.handleImageUri(it)
            }
        }

    val navOption = {
        if (currentStep > 0) {
            currentStep--
        } else {
            eventViewModel.clearEntries()
            navController.popBackStack()
        }
    }

    val content: @Composable () -> Unit = when (currentStep) {
        0 -> {
            {
                EventTypeChoose(
                    onEventTypeSelected = {
                        eventViewModel.onNewEventCategory(it.title)
                        currentStep++
                    }
                )
            }
        }

        1 -> {
            {
                NameEvent(
                    text = eventViewModel.eventTitle,
                    onValueChange = {
                        eventViewModel.onEventTitleChange(it)
                    },
                    onRadioButtonClick = {
                        eventViewModel.setEventType(it)
                    },
                    eventViewModel = eventViewModel
                )
            }
        }

        2 -> {
            {
                ChooseEventTags(
                    eventViewModel = eventViewModel
                )
            }
        }

        3 -> {
            {
                CreateEventPicture(
                    onClick = {
                        launcher.launch("image/*")
                    },
                    eventViewModel = eventViewModel
                )
            }
        }

        4 -> {
            {
                CreateEventDetails(
                    description = eventViewModel.eventDescription,
                    onDescriptionChange = {
                        eventViewModel.onEventDescriptionChange(it)
                    },
                    location = eventViewModel.eventLocation,
                    onLocationChange = {
                        eventViewModel.onEventLocationChange(it)
                    },
                    eventDateTime = eventViewModel.eventStartDate!!,
                    onDateTimeChange = {
                        eventViewModel.setNewEventStartDate(it)
                    }
                )
            }
        }

        else -> {
            { }
        }
    }
    Scaffold(
        topBar = {
            SimpleTopBar(
                title = "New Event",
                onClick = {
                    navOption()
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            CreateEventSoothe(
                title = when (currentStep) {
                    0 -> "What is your goal?"
                    1 -> "Name your event"
                    2 -> "Select subject"
                    3 -> "Choose a picture"
                    4 -> "Finally, fill the details"
                    else -> ""
                },
                subtitle = when (currentStep) {
                    0 -> "So, what kind of event are you planning to host?"
                    1 -> "Give your event a short title that best captures what it’s about!"
                    2 -> "Choose what your event’s about — games, business, music, or whatever you’re into!"
                    3 -> "Show off the vibe and let people see what they’re signing up for!"
                    4 -> "Where it’s happening, what it’s about, and anything guests should know!"
                    else -> ""
                },
                content = content,
                currentStep = currentStep,
                onButtonClick = {
                    if(currentStep < 4) {
                        currentStep++
                        Log.d("MyLog", "$currentStep")
                    } else if(currentStep == 4) {
                        Log.d("My Log", "Creating an Event...")
                        eventViewModel.createEvent(
                            onSuccess = {
                                navController.popBackStack()
                                eventViewModel.clearEntries()
                            },
                            onError = {
                                Log.d("My Log", "Event Not Created")
                                currentStep = 0
                            }
                        )
                    }
                },
                titleFilled = eventViewModel.eventTitle.isNotBlank(),
                tagsChosen = eventViewModel.eventTags.isNotBlank(),
                pictureChosen = eventViewModel.eventPicture.isNotBlank(),
                locationAndDescriptionFilled = eventViewModel.eventDescription.isNotBlank() && eventViewModel.eventLocation.isNotBlank()
            )
        }
    }
}

@Composable
fun EventTypeChoose(
    onEventTypeSelected: (EventCategory) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        EventCategory.entries.forEach { eventType ->
            EventTypeCard(eventType, onClick = {
                onEventTypeSelected(eventType)
            })
        }
    }
}


@Composable
fun EventTypeCard(
    eventType: EventCategory,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 15.dp)
                .height(90.dp)
        ) {
            Image(
                painter = painterResource(eventType.iconId),
                contentDescription = "${eventType.title} icon"
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = eventType.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(eventType.subtitleResId),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                )
            }
        }
    }
}