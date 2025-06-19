package com.example.seeya.ui.theme.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.seeya.data.model.UpdateProfileRequest
import com.example.seeya.ui.theme.components.CustomTextField
import com.example.seeya.ui.theme.components.CustomTitleButton
import com.example.seeya.ui.theme.components.SeeYaLogo
import com.example.seeya.ui.theme.components.SimpleTopBar
import com.example.seeya.viewmodel.auth.AuthViewModel


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
            authViewModel = authViewModel,
            navController = navController
        )
    }
}

@Composable
fun EditMyProfileScreenContent(
    authViewModel: AuthViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val user = authViewModel.currentUser

    var username by remember { mutableStateOf(user.value?.username ?: "") }
    var name by remember { mutableStateOf(user.value?.name ?: "") }
    var surname by remember { mutableStateOf(user.value?.surname ?: "") }
    var password by remember { mutableStateOf("") }

    val picture by remember {
        mutableStateOf(user.value?.profilePicture)
    }

    var selectedUri: Uri? by remember { mutableStateOf(null) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedUri = it
            }
        }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        var showSuccessDialog by remember { mutableStateOf(false) }
        var showErrorDialog by remember { mutableStateOf<String?>(null) }

        if (showSuccessDialog) {
            AlertDialog(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                textContentColor = MaterialTheme.colorScheme.onBackground,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                onDismissRequest = { showSuccessDialog = false },
                title = { Text("Success") },
                text = { Text("Your profile was updated successfully!") },
                confirmButton = {
                    TextButton(onClick = {
                        showSuccessDialog = false
                        navController.popBackStack()
                    }) {
                        Text("OK")
                    }
                }
            )
        }

        showErrorDialog?.let { errorMsg ->
            AlertDialog(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                textContentColor = MaterialTheme.colorScheme.onBackground,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                onDismissRequest = { showErrorDialog = null },
                title = { Text("Error") },
                text = { Text(errorMsg) },
                confirmButton = {
                    TextButton(onClick = {
                        showErrorDialog = null
                        navController.popBackStack()
                    }) {
                        Text("OK")
                    }
                }
            )
        }


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
                when {
                    selectedUri != null -> {
                        AsyncImage(
                            model = selectedUri,
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    !picture.isNullOrBlank() -> {
                        AsyncImage(
                            model = picture,
                            contentDescription = "Current Profile Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    else -> {
                        SeeYaLogo(fontSize = 74)
                    }
                }
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
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    username = it.lowercase()
                }
            )

            Spacer(Modifier.height(25.dp))

            CustomTextFieldWithCounter(
                placeholder = "new name",
                text = name,
                limit = 20,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { name = it }
            )

            Spacer(Modifier.height(25.dp))

            CustomTextFieldWithCounter(
                placeholder = "new surname",
                text = surname,
                limit = 20,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { surname = it }

            )

            Spacer(Modifier.height(25.dp))

            CustomTextFieldWithCounter(
                placeholder = "new password (optional)",
                text = password,
                limit = 32,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { password = it }
            )

            Spacer(Modifier.weight(1f))

            val context = LocalContext.current

            CustomTitleButton(
                title = "Edit",
                onClick = {
                    val usernameUpdate = if (user.value?.username == username) null else username
                    val nameUpdate = if (user.value?.name == name) null else name
                    val surnameUpdate = if (user.value?.surname == surname) null else surname
                    val passwordUpdate = password.ifBlank { null }

                    if (selectedUri != null) {
                        authViewModel.updatePhoto(
                            uri = selectedUri!!,
                            context = context,
                            onSuccess = {
                                val request = UpdateProfileRequest(
                                    username = usernameUpdate,
                                    name = nameUpdate,
                                    surname = surnameUpdate,
                                    password = passwordUpdate,
                                    profilePicture = null
                                )
                                authViewModel.updateAccountInfo(
                                    request = request,
                                    onSuccess = { showSuccessDialog = true },
                                    onError = { msg -> showErrorDialog = msg }
                                )
                            },
                            onError = { msg -> showErrorDialog = msg }
                        )
                    } else {
                        val request = UpdateProfileRequest(
                            username = usernameUpdate,
                            name = nameUpdate,
                            surname = surnameUpdate,
                            password = passwordUpdate,
                            profilePicture = null
                        )
                        authViewModel.updateAccountInfo(
                            request = request,
                            onSuccess = { showSuccessDialog = true },
                            onError = { msg -> showErrorDialog = msg }
                        )
                    }
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
    onValueChange: (String) -> Unit,
    maxLines: Int = 1,
    fieldHeight: Int? = null
) {
    val textFieldModifier = Modifier
        .fillMaxWidth()
        .then(
            if (fieldHeight != null) Modifier.height(fieldHeight.dp)
            else Modifier
        )

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
            modifier = textFieldModifier,
            numberOfLines = maxLines
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
