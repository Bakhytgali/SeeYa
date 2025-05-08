package com.example.seeya.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.seeya.viewmodel.event.EventViewModel

@Composable
fun NameEvent(
    text: String,
    onValueChange: (String) -> Unit,
    onRadioButtonClick: (String) -> Unit,
    eventViewModel: EventViewModel,
    modifier: Modifier = Modifier
) {
    var bottomSheetVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 3.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "0/40",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
            CustomTextField(
                text = text,
                onValueChange = onValueChange,
                placeholder = "Event Title",
                modifier = modifier.fillMaxWidth()
            )
        }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 3.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Event Type",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
            CustomTextField(
                text = eventViewModel.eventTypeValue,
                onValueChange = {},
                placeholder = "Event type",
                isActive = false,
                onClick = {
                    bottomSheetVisible = true
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier
                            .size(30.dp)
                    )
                },
                modifier = modifier.fillMaxWidth()
            )
        }

        if (bottomSheetVisible) {
            CustomBottomSheet(
                onDismiss = {
                    bottomSheetVisible = false
                },
                onRadioButtonClick = {
                    onRadioButtonClick(it)
                },
                eventViewModel = eventViewModel
            )
        }
    }
}