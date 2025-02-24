package com.example.seeya.ui.theme.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.data.model.Event
import com.example.seeya.ui.theme.Poppins
import com.example.seeya.ui.theme.Unbounded
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.ui.theme.components.EventTitleCard
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.viewmodel.event.EventViewModel


@Composable
fun EventScreen(
    navController: NavController,
    eventId: String,
    eventViewModel: EventViewModel
) {
    val event = remember {
        mutableStateOf<Event?>(null)
    }

    LaunchedEffect(Unit) {
        eventViewModel.getEvent(
            eventId,
            onSuccess = { fetchedEvent ->
                event.value = fetchedEvent
            },
            onError = {
                Log.e("Event Screen", "Failed to fetch an event!")
            }
        )
    }

    val context = LocalContext.current

    MainScaffold(
        title = "Events",
        navController = navController
    ) { mod ->

        Box(
            contentAlignment = Alignment.Center,
            modifier = mod.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(modifier = Modifier.height(20.dp))

                if(event.value != null) {
                    EventTitleCard(event.value!!)
                } else {
                    Text(
                        text = "Failed to fetch the event",
                        color = Color.Red,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        event.value?.let {
                            eventViewModel.joinEvent(
                                eventId = it.eventId,
                                onSuccess = {
                                    Toast.makeText(context, "Successfully joined!", Toast.LENGTH_SHORT).show()
                                },
                                onError = { errorMsg ->
                                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Participate",
                        color = bgColor,
                        fontFamily = Unbounded,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }

            }
        }
    }
}