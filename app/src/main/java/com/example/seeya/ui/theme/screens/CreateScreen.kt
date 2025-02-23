package com.example.seeya.ui.theme.screens

import DashedBorderBox
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.ui.theme.Poppins
import com.example.seeya.ui.theme.Unbounded
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.ui.theme.components.CustomTextField
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.ui.theme.components.OptionButton
import com.example.seeya.ui.theme.grayText
import com.example.seeya.ui.theme.secondaryColor

@Composable
fun CreateScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val options = listOf("Event", "Club")
    val activeOption = remember {
        mutableStateOf(options[0])
    }

    val title = remember {
        mutableStateOf("")
    }

    val category = remember {
        mutableStateOf("")
    }

    val location = remember {
        mutableStateOf("")
    }

    val description = remember {
        mutableStateOf("")
    }

    val eventOptions = listOf("Open", "Closed")
    val eventType = remember {
        mutableStateOf(eventOptions[0])
    }

    MainScaffold(
        title = "Create",
        navController = navController
    ) { mod ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = mod
                .fillMaxSize()
                .background(bgColor)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    options.forEach { option ->
                        OptionButton(
                            text = option,
                            isActive = option == activeOption.value,
                            onClick = {
                                activeOption.value = option
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                DashedBorderBox(
                    modifier = Modifier
                        .size(120.dp)
                        .clickable {
                            // TODO
                        },
                    borderColor = secondaryColor,
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = activeOption.value + "\nPicture",
                            color = secondaryColor,
                            fontFamily = Poppins,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))

                CustomTextField(
                    text = title,
                    placeholder = if(activeOption.value == "Event") "Event Title" else "Club Name",
                    onValueChange = {
                        title.value = it
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(
                    text = category,
                    placeholder = "Category",
                    onValueChange = {
                        category.value = it
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(
                    text = location,
                    placeholder = "Location",
                    onValueChange = {
                        location.value = it
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                if(activeOption.value == "Event") {
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Event Type",
                            fontSize = 14.sp,
                            color = grayText
                        )

                        eventOptions.forEach { option ->
                            OptionButton(
                                text = option,
                                isActive = option == eventType.value,
                                onClick = {
                                    eventType.value = option
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(
                    text = description,
                    onValueChange = {
                        description.value = it
                    },
                    placeholder = "Description",
                    numberOfLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        // TODO
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = secondaryColor,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Create",
                        fontSize = 20.sp,
                        fontFamily = Unbounded,
                        fontWeight = FontWeight.Bold,
                        color = bgColor,
                        modifier = Modifier.padding(vertical = 7.dp)
                    )
                }
            }
        }
    }
}

