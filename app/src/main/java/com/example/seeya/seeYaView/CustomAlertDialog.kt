package com.example.seeya.seeYaView

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seeya.models.Event
import com.example.seeya.ui.theme.ContainerColor
import com.example.seeya.ui.theme.InterFont
import com.example.seeya.ui.theme.TitleColor

@Composable
fun CustomAlertDialog(
    modifier: Modifier = Modifier,
    eventList: MutableState<ArrayList<Event>>,
    alertDialogOpened: MutableState<Boolean>
) {
    val eventName = remember {
        mutableStateOf("")
    }

    val eventSubtitle = remember {
        mutableStateOf("")
    }

    val location = remember {
        mutableStateOf("")
    }

    val errorMessage = remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = { alertDialogOpened.value = false },
        title = {
            Text(
                text = "Add an event",
                fontSize = 18.sp,
                fontFamily = InterFont,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                CustomTextField(
                    value = eventName.value,
                    label = "Event Name",
                    onValueChange = { newValue ->
                        eventName.value = newValue
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    value = eventSubtitle.value,
                    label = "Subtitle",
                    onValueChange = { newValue ->
                        eventSubtitle.value = newValue
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))

                CustomTextField(
                    value = location.value,
                    label = "Location",
                    onValueChange = { newValue ->
                        location.value = newValue
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = errorMessage.value,
                    color = Color.Red,
                    fontFamily = InterFont,
                    fontSize = 14.sp
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if(
                        eventName.value.isNotEmpty() &&
                        eventSubtitle.value.isNotEmpty() &&
                        location.value.isNotEmpty()
                    ) {
                        val newEvent = Event(
                            title = eventName.value,
                            author = "Rakhat B.",
                            location = location.value,
                            subtitle = eventSubtitle.value
                        )

                        val updatedEventList = ArrayList(eventList.value)
                        updatedEventList.add(newEvent)

                        eventList.value = updatedEventList

                        alertDialogOpened.value = false
                    } else {
                        errorMessage.value = "Make sure to fill all fields."
                    }

                }
            ) {
                Text(
                    text = "Add",
                    fontSize = 16.sp,
                    color = TitleColor
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    alertDialogOpened.value = false
                }
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 16.sp,
                    color = ContainerColor
                )
            }
        }
    )
}