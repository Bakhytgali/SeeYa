package com.example.seeya.seeYaView

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seeya.models.Event
import com.example.seeya.ui.theme.InterFont

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(modifier: Modifier = Modifier) {
    val alertDialogOpened = remember {
        mutableStateOf(false)
    }

    val events = remember {
        mutableStateOf(
            arrayListOf(
                Event(
                    title = "MBDTF Listening Session",
                    author = "Rakhat B.",
                    location = "AITU Ballroom",
                    subtitle = "My Beautiful Dark Twisted Fantasy is a genre-bending masterpiece that explores the darker sides of celebrity, fame, and love."
                ),
                Event(
                    title = "Linear Math Studying",
                    author = "Merey I.",
                    location = "AITU, C1.2.235K",
                    subtitle = "Studying of the most essential Math subjects for programmers and preparing for exams."
                ),
                Event(
                    title = "Barcelona - Real Madrid Watching",
                    author = "Azamat Y.",
                    location = "AITU, Open Space",
                    subtitle = "Watching El Classico and rooting for our favourite teams."
                )
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.Transparent
                ),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        SeeYaLogo()
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent,
                modifier = Modifier.fillMaxWidth(),
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }

                        IconButton(
                            onClick = {
                                alertDialogOpened.value = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "",
                                tint = Color.White,
                                modifier = Modifier.size(125.dp)
                            )
                        }

                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF000000), // Vibrant blue
                            Color(0xFF232b2b)  // Muted teal
                        )
                    )
                )
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 30.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                if(alertDialogOpened.value) {
                    CustomAlertDialog(
                        eventList = events,
                        alertDialogOpened = alertDialogOpened
                    )
                }

                Text(
                    text = "Events",
                    fontFamily = InterFont,
                    fontSize = 28.sp,
                    color = Color.White.copy(0.8f),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(30.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    items(events.value.reversed()) { event ->
                        EventCard(
                            eventTitle = event.title,
                            eventSubtitle = event.subtitle,
                            eventAuthorName = event.author,
                            eventLocation = event.location
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}