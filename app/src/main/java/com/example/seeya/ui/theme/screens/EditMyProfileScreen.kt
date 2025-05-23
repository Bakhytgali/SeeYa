package com.example.seeya.ui.theme.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.ui.theme.components.CustomTextField
import com.example.seeya.ui.theme.components.CustomTitleButton
import com.example.seeya.ui.theme.components.SeeYaLogo
import com.example.seeya.ui.theme.components.SimpleTopBar
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import java.io.ByteArrayOutputStream


@Composable
fun EditMyProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
) {
    var confirmLeavingIsOpen by remember {
        mutableStateOf(false)
    }

    BackHandler {
        confirmLeavingIsOpen = true
    }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = "Edit Info",
                onClick = {
                    confirmLeavingIsOpen = true
                }
            )
        }
    ) { paddingValues ->
        if (confirmLeavingIsOpen) {
            ConfirmLeavingDialog(
                onConfirm = {
                    confirmLeavingIsOpen = false
                    navController.popBackStack()
                },
                onCancel = { confirmLeavingIsOpen = false },
            )
        }

        EditMyProfileScreenContent(
            modifier = Modifier.padding(paddingValues),
            authViewModel = authViewModel
        )
    }
}

@Composable
fun EditMyProfileScreenContent(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val user = authViewModel.currentUser

    var username by remember { mutableStateOf(user.value?.username ?: "") }
    var name by remember { mutableStateOf(user.value?.name ?: "") }
    var surname by remember { mutableStateOf(user.value?.surname ?: "") }

    var picture by remember {
        mutableStateOf(user.value?.profilePicture)
    }

    var selectedBitmap by remember { mutableStateOf(picture?.let { decodeBase64ToBitmap(it) }) }

    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val bitmap = handleImageUri(context, it)
                selectedBitmap = bitmap
                bitmap?.let { newBitmap ->
                    picture = encodeToBase64(newBitmap)
                }
            }
        }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight()
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                selectedBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } ?: SeeYaLogo(
                    fontSize = 74
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Tap to choose",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondaryContainer
            )

            CustomTextFieldWithCounter(
                placeholder = "new username",
                text = username,
                limit = 20,
                modifier = Modifier.fillMaxWidth()
            ) { username = it.lowercase() }
            Spacer(Modifier.height(25.dp))
            CustomTextFieldWithCounter(
                placeholder = "new name",
                text = name,
                limit = 20,
                modifier = Modifier.fillMaxWidth()
            ) { name = it }
            Spacer(Modifier.height(25.dp))
            CustomTextFieldWithCounter(
                placeholder = "new surname",
                text = surname,
                limit = 20,
                modifier = Modifier.fillMaxWidth()
            ) { surname = it }

            Spacer(Modifier.weight(1f))

            CustomTitleButton(
                title = "Edit",
                onClick = {

                },
                isActive = true
            )

            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
fun CustomTextFieldWithCounter(
    modifier: Modifier = Modifier,
    placeholder: String,
    text: String,
    limit: Int,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "${text.length}/$limit",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )

        CustomTextField(
            text = text,
            onValueChange = { newValue -> if (newValue.length <= limit) onValueChange(newValue) },
            placeholder = placeholder,
            limit = limit,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ConfirmLeavingDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onCancel,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        confirmButton = {
            TextButton(
                onClick = onConfirm,
            ) {
                Text(
                    text = "Confirm",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel,
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                )
            }
        },
        title = {
            Text(
                text = "Leave without editing?",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = "Are you sure you want to quit editing your account?",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 25.dp),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    )
}

fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun handleImageUri(context: android.content.Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        bytes?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }
    } catch (e: Exception) {
        null
    }
}

fun encodeToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}
