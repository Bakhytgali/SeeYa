package com.example.seeya.ui.theme.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.seeya.data.model.EventType
import com.example.seeya.viewmodel.event.EventViewModel

@Composable
fun EventTypeBottomModalCard(
    onClick: () -> Unit,
    eventType: EventType,
    eventViewModel: EventViewModel,
) {
    Row(
        modifier = Modifier.clickable {
            onClick()
        }
    ) {
        RadioButton(
            selected = eventType.title == eventViewModel.eventTypeValue,
            onClick = onClick
        )
        Column {
            Text(
                text = eventType.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(eventType.description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondaryContainer,
            )
        }
    }
}