package com.example.seeya.ui.theme.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.seeya.data.model.PostRequestModel
import com.example.seeya.ui.theme.Poppins
import com.example.seeya.ui.theme.components.SimpleTopBar
import com.example.seeya.viewmodel.event.EventViewModel

@Composable
fun AddPostScreen(
    eventViewModel: EventViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var postHeader by remember { mutableStateOf("") }
    var postContent by remember { mutableStateOf("") }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        selectedImages = (selectedImages + uris).distinct().take(3)
    }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = "New Post",
                onClick = { navController.popBackStack() },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .fillMaxWidth(0.9f)
                    .height(600.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PostTextField(
                    text = postHeader,
                    onTextChange = { postHeader = it },
                    placeholder = "Header...",
                    fontSize = 16,
                    fontWeight = FontWeight.Medium,
                    limit = 60
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    PostTextField(
                        text = postContent,
                        onTextChange = { postContent = it },
                        placeholder = "Content...",
                        fontSize = 14,
                        fontWeight = FontWeight.Normal,
                        limit = 300,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                if (selectedImages.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        items(selectedImages.size) { index ->
                            Box {
                                Image(
                                    painter = rememberAsyncImagePainter(selectedImages[index]),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(10.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove image",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .align(Alignment.TopEnd)
                                        .clickable {
                                            selectedImages = selectedImages.toMutableList().also {
                                                it.removeAt(index)
                                            }
                                        }
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PostButton(
                        title = "Attach",
                        onClick = { imagePicker.launch("image/*") },
                        bgColor = MaterialTheme.colorScheme.secondaryContainer,
                        textColor = MaterialTheme.colorScheme.primaryContainer,
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.AttachFile,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primaryContainer
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )

                    PostButton(
                        title = "Send",
                        onClick = {
                            eventViewModel.addPostMediaToCloudinary(
                                media = selectedImages,
                                header = postHeader,
                                content = postContent,
                                eventId = eventViewModel.event?.eventId ?: "Something else",
                                onSuccess = {
                                    navController.navigate("event/${eventViewModel.event?.eventId}") {
                                        popUpTo("main") { inclusive = false }
                                    }
                                },
                                onError = { errorMessage ->
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        bgColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.primaryContainer,
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.Send,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primaryContainer
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}



@Composable
fun PostTextField(
    text: String,
    onTextChange: (String) -> Unit,
    fontSize: Int,
    fontWeight: FontWeight,
    placeholder: String,
    limit: Int,
    modifier: Modifier = Modifier
) {
    TextField(
        value = text,
        onValueChange = { if (text.length <= limit) onTextChange(it) },
        placeholder = {
            Text(
                text = placeholder,
                fontSize = fontSize.sp,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
        },
        textStyle = TextStyle(
            fontSize = fontSize.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = fontWeight,
            fontFamily = Poppins
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier
    )
}

@Composable
fun PostButton(
    title: String,
    bgColor: Color,
    textColor: Color,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = textColor
            )
            Spacer(Modifier.width(10.dp))
            icon()
        }
    }
}