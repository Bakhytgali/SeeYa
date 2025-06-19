package com.example.seeya.ui.theme.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun CreateEventDetails(
    description: String,
    onDescriptionChange: (String) -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
    eventDateTime: LocalDateTime,
    onDateTimeChange: (LocalDateTime) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var pickedDateTime by rememberSaveable { mutableStateOf(eventDateTime) }

    var showMap by remember { mutableStateOf(false) }

    if (showMap) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            MapPickerContent(
                onLocationPicked = { latLng ->
                    val coords = "${latLng.latitude}, ${latLng.longitude}"
                    onLocationChange(coords)
                    showMap = false
                },
            )
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Column {
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = "${description.length}/40",
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                textAlign = TextAlign.End
            )
            CustomTextField(
                text = description,
                placeholder = "Event Description",
                onValueChange = {
                    if(description.length <= 80) {
                        onDescriptionChange(it)
                    }
                },
                numberOfLines = 4,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                limit = 40
            )
        }

        CustomTextField(
            text = location,
            placeholder = "Location",
            onValueChange = onLocationChange,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location Icon",
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            },
            isActive = false,
            onClick = onClick
        )

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val formattedDateTime = eventDateTime.format(formatter)

        CustomTextField(
            text = formattedDateTime,
            onValueChange = { },
            placeholder = "Date",
            isActive = false,
            onClick = {
                val now = Calendar.getInstance()
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                val newDateTime = LocalDateTime.of(
                                    year,
                                    month + 1,
                                    dayOfMonth,
                                    hour,
                                    minute
                                )
                                pickedDateTime = newDateTime
                                onDateTimeChange(newDateTime)
                            },
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        )

        Log.d("Date Picker", "$eventDateTime")
    }
}