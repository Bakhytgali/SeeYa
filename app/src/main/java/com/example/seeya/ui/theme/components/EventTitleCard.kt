package com.example.seeya.ui.theme.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seeya.data.model.Event
import com.example.seeya.ui.theme.Poppins
import com.example.seeya.ui.theme.Unbounded
import com.example.seeya.ui.theme.secondaryContainer
import com.example.seeya.ui.theme.primaryContainerColor

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
                .padding(horizontal = 10.dp, vertical = 15.dp)
        ) {
            if(!event.eventPicture.isNullOrEmpty()) {
                event.eventPicture.let {base64 ->
                    val bitmap = base64ToBitmap(base64)

                    bitmap.let {
                        if (it != null) {
                            Card (
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Event Picture",
                                    modifier = Modifier.size(150.dp),
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = event.name,
                fontSize = 18.sp,
                fontFamily = Unbounded,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = event.category ?: "Null",
                fontSize = 14.sp,
                fontFamily = Poppins,
                textAlign = TextAlign.Center,
                color = secondaryContainer
            )
        }
    }
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