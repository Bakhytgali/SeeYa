package com.example.seeya.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.ui.theme.components.CustomTextField
import com.example.seeya.ui.theme.components.CustomTitleButton
import com.example.seeya.viewmodel.auth.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var isDialogShown by remember { mutableStateOf(false) }
    val success by authViewModel.restoreSuccess.collectAsState()
    val loading by authViewModel.restoreLoading.collectAsState()
    val error by authViewModel.restoreErrorMessage.collectAsState()

    LaunchedEffect(success) {
        if (success == true) {
            isDialogShown = true
        }
    }

    if (isDialogShown) {
        AlertDialog(
            onDismissRequest = {
                isDialogShown = false
                navController.popBackStack()
            },
            title = {
                Text(
                    "Check your email",
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            text = {
                Text(
                    "A password reset link has been sent to your email.",
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    isDialogShown = false
                    navController.popBackStack()
                }) {
                    Text("OK", color = MaterialTheme.colorScheme.onBackground)
                }
            },
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                "Forgot Password",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomTextField(
                text = email,
                placeholder = "Enter your email",
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (loading) {
                CircularProgressIndicator()
            } else {
                CustomTitleButton(
                    title = "Send Reset Link",
                    onClick = {
                        authViewModel.restorePassword(email)
                    }
                )
            }

            if (error != null) {
                Text(
                    text = error ?: "",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.popBackStack() }) {
                Text("Back to login", color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}
