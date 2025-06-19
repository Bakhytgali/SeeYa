package com.example.seeya.ui.theme.components

import android.graphics.Paint.Align
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.seeya.data.model.Event
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventInfoBottomModal(
    event: Event,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(horizontal = 10.dp, vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                Text(
                    text = "Event Details",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
                Text(
                    text = event.description,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                EventInfoModalRow(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Group,
                            contentDescription = "Info Modal Participants",
                            tint = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    iconTitle = "Participants",
                    rowText = if (event.participants?.size != null) event.participants.size.toString() else "0"
                )
                EventInfoModalRow(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "Info Modal com.example.seeya.data.model.Event Type",
                            tint = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    iconTitle = "Type",
                    rowText = if (event.isOpen) "Open" else "Closed"
                )
                EventInfoModalRow(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "Info Modal com.example.seeya.data.model.Event Location",
                            tint = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    iconTitle = "Location",
                    rowText = event.location
                )

                val formattedDate = LocalDateTime.parse(event.startDate)
                    .format(DateTimeFormatter.ofPattern("dd.MM HH:mm"))

                EventInfoModalRow(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = "Event Date",
                            tint = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    iconTitle = "Date Time",
                    rowText = formattedDate
                )
            }
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        sheetState.hide()
                        onDismiss()
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .size(45.dp)
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close com.example.seeya.data.model.Event Info Bottom Modal",
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}

@Composable
fun EventInfoModalRow(
    icon: @Composable () -> Unit,
    iconTitle: String,
    rowText: String
) {
    Row(horizontalArrangement = Arrangement.spacedBy(30.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            icon()
            Text(
                text = iconTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
        }

        Text(
            text = rowText,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Right
        )
    }
}