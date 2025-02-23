package com.example.seeya.ui.theme.screens

import DashedBorderBox
import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.data.model.Creator
import com.example.seeya.ui.theme.*
import com.example.seeya.ui.theme.components.CustomTextField
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.ui.theme.components.OptionButton
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.event.EventViewModel
import java.io.ByteArrayOutputStream
import java.util.Calendar
import java.util.Date

@Composable
fun CreateScreen(
    navController: NavController,
    eventViewModel: EventViewModel,
    authViewModel: AuthViewModel, // Используем для получения текущего юзера
    modifier: Modifier = Modifier
) {
    val options = listOf("Event", "Club")
    val activeOption = remember { mutableStateOf(options[0]) }

    val title = remember { mutableStateOf("") }
    val category = remember { mutableStateOf("") }
    val location = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    val eventOptions = listOf("Open", "Closed")
    val eventType = remember { mutableStateOf(eventOptions[0]) }

    val context = LocalContext.current
    val selectedDate = remember { mutableStateOf("Select Date") }

    // DatePicker Dialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            selectedDate.value = "$day/${month + 1}/$year"
        },
        Calendar.getInstance().get(Calendar.YEAR),
        Calendar.getInstance().get(Calendar.MONTH),
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    )

    // Choosing image from gallery
    val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }
    val imageBase64 = remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val bytes = inputStream?.readBytes()
            inputStream?.close()

            if (bytes != null) {
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                val croppedBitmap = cropCenter(bitmap)

                imageBitmap.value = croppedBitmap
                imageBase64.value = encodeToBase64(croppedBitmap)
            }
        }
    }

    // Получаем текущего пользователя из ViewModel
    val currentUser = authViewModel.user.value

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
                            onClick = { activeOption.value = option }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    DashedBorderBox(
                        modifier = Modifier.size(200.dp),
                        borderColor = secondaryColor,
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (imageBitmap.value != null) {
                                Image(
                                    bitmap = imageBitmap.value!!.asImageBitmap(),
                                    contentDescription = "Selected Image",
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Text(
                                    text = activeOption.value + "\nPicture",
                                    color = secondaryColor,
                                    fontFamily = Poppins,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    TextButton(
                        onClick = {
                            launcher.launch("image/*")
                        }
                    ) {
                        Text(
                            text = "Choose from Gallery",
                            fontSize = 14.sp,
                            fontFamily = Poppins,
                            color = grayText,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                CustomTextField(
                    text = title,
                    placeholder = if (activeOption.value == "Event") "Event Title" else "Club Name",
                    onValueChange = { title.value = it },
                    modifier = Modifier.fillMaxWidth()
                )

                if (activeOption.value == "Event") {
                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { datePickerDialog.show() },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryContainerColor),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = selectedDate.value,
                            fontSize = 16.sp,
                            fontFamily = Unbounded,
                            color = secondaryColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(
                    text = category,
                    placeholder = "Category",
                    onValueChange = { category.value = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(
                    text = location,
                    placeholder = "Location",
                    onValueChange = { location.value = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(
                    text = description,
                    onValueChange = { description.value = it },
                    placeholder = "Description",
                    numberOfLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        if (title.value.isBlank() || category.value.isBlank() ||
                            location.value.isBlank() || description.value.isBlank() ||
                            selectedDate.value == "Select Date" || currentUser == null
                        ) {
                            // TODO: Show an error message
                        } else {
                            eventViewModel.createEvent(
                                name = title.value,
                                description = description.value,
                                category = category.value,
                                eventPicture = imageBase64.value,
                                isClosed = eventType.value == "Closed",
                                location = location.value,
                                startDate = parseDate(selectedDate.value),
                                creator = Creator(
                                    id = currentUser.id,
                                    name = currentUser.name,
                                    surname = currentUser.surname,
                                    username = currentUser.username,
                                    rating = null
                                ),
                                onSuccess = {
                                    navController.navigate("main") {
                                        popUpTo("main") {inclusive = false}
                                    }
                                },
                                onError = {
                                    Log.d("EventViewModel", it)
                                }
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = secondaryColor),
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

fun encodeToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun cropCenter(bitmap: Bitmap): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val newSize = minOf(width, height)

    val xOffset = (width - newSize) / 2
    val yOffset = (height - newSize) / 2

    return Bitmap.createBitmap(bitmap, xOffset, yOffset, newSize, newSize)
}

fun parseDate(dateStr: String): Date {
    val parts = dateStr.split("/")
    val day = parts[0].toInt()
    val month = parts[1].toInt() - 1
    val year = parts[2].toInt()

    val calendar = Calendar.getInstance()
    calendar.set(year, month, day)

    return calendar.time
}
