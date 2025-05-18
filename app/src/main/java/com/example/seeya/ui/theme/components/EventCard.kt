package com.example.seeya.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seeya.data.model.Event
import com.example.seeya.ui.theme.Unbounded
import com.example.seeya.ui.theme.grayText
import com.example.seeya.ui.theme.primaryContainerColor
import com.example.seeya.viewmodel.BottomBarViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun EventCard(
    event: Event,
    modifier: Modifier = Modifier
) {
    val displayFormatter = DateTimeFormatter.ofPattern("dd/MM    HH:mm")

    val dateTime = try {
        LocalDateTime.parse(event.startDate, DateTimeFormatter.ISO_DATE_TIME)
    } catch (e: Exception) {
        LocalDateTime.now()
    }

    val formattedDate = dateTime.format(displayFormatter)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 20.dp, top = 15.dp, bottom = 15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.95f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )

                Text(
                    text = if(event.isOpen) "Open" else "Closed",
                    style = MaterialTheme.typography.bodySmall,
                    color = if(event.isOpen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = event.name,
                style = MaterialTheme.typography.titleSmall,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = event.location,
                fontSize = 13.sp,
                color = grayText
            )
        }
    }

}