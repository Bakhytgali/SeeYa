package com.example.seeya.ui.theme.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seeya.data.model.Creator
import com.example.seeya.data.model.Event
import com.example.seeya.data.model.Participant
import com.example.seeya.ui.theme.Poppins
import com.example.seeya.ui.theme.Unbounded
import com.example.seeya.ui.theme.grayText
import com.example.seeya.ui.theme.primaryContainerColor
import kotlinx.coroutines.CoroutineStart
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

@Composable
fun EventTitleCard(
    event: Event,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = primaryContainerColor
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            if(!event.eventPicture.isNullOrEmpty()) {
                event.eventPicture.let {base64 ->
                    val bitmap = base64ToBitmap(base64)

                    bitmap.let {
                        if (it != null) {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = "Event Picture",
                                modifier = Modifier.size(100.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = event.name,
                fontSize = 16.sp,
                fontFamily = Unbounded,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = event.category,
                fontSize = 14.sp,
                fontFamily = Poppins,
                textAlign = TextAlign.Center,
                color = grayText
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun EventExample(modifier: Modifier = Modifier) {
    val event = Event(
        name = "Hurry Up Tomorrow Listening Party",
        category = "Music",
        eventPicture = null,
        isClosed = false,
        creator = Creator(
            id = "something",
            name = "Rakhat",
            surname = "Bakhytgali",
            username = "NinjaFrog",
            rating = 5.0
        ),
        participants = listOf(
            Participant(
                id = "something",
                name = "Rakhat",
                surname = "Bakhytgali",
                username = "ninja_frog"
            )
        ),
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras orci tellus, hendrerit et magna ac, porttitor",
        location = "AITU, Open Space",
        startDate = Date.from(
            LocalDateTime.of(2025, 1, 31, 18, 0)
                .atZone(ZoneId.systemDefault())
                .toInstant()
        ),
        createdAt = Date(),
        eventId = "EventId"
    )

    EventTitleCard(event)
}


fun base64ToBitmap(base64String: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        null
    }
}