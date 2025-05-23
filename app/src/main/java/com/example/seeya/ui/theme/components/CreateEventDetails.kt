package com.example.seeya.ui.theme.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import java.util.Calendar

@Composable
fun CreateEventDetails(
    description: String,
    onDescriptionChange: (String) -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
    eventDateTime: LocalDateTime,
    onDateTimeChange: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var pickedDateTime by rememberSaveable { mutableStateOf(eventDateTime) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Column {
            Text(
                text = "0/80",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                textAlign = TextAlign.End
            )
            CustomTextField(
                text = description,
                placeholder = "Event Description",
                onValueChange = onDescriptionChange,
                numberOfLines = 4,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
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
            }
        )

        CustomTextField(
            text = eventDateTime.toString(), // Просто выводим как есть
            onValueChange = { /* не нужно, так как поле только для чтения */ },
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
                                    month + 1, // Месяцы в Calendar от 0 до 11
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
    }
}