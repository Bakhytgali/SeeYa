package com.example.seeya.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.R
import com.example.seeya.ui.theme.*
import com.example.seeya.ui.theme.components.CustomTextField
import com.example.seeya.ui.theme.components.CustomTitleButton
import com.example.seeya.ui.theme.components.SeeYaLogo
import com.example.seeya.viewmodel.auth.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var showErrorDialog by remember { mutableStateOf(false) }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Login Failed") },
            text = { Text("Incorrect email or password. Please try again.") },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.9f)
        ) {
            SeeYaLogo()

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Sign In",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Welcome back!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondaryContainer,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            CustomTextField(
                text = authViewModel.loginText,
                placeholder = "Email",
                onValueChange = {
                    authViewModel.onLoginTextChange(it)
                    authViewModel.setErrorValue(false)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            CustomTextField(
                text = authViewModel.loginPassword,
                placeholder = "Password",
                onValueChange = { input ->
                    val noSpaces = input.replace(" ", "")
                    authViewModel.onLoginPasswordChange(noSpaces)
                    authViewModel.setErrorValue(false)
                },
                modifier = Modifier.fillMaxWidth(),
                isPassword = true
            )

            if (authViewModel.isError) {
                Text(
                    text = "Email and Password cannot be empty!",
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }

            TextButton(
                onClick = {
                    navController.navigate("forgotPassword") {
                        restoreState = true
                    }
                }
            ) {
                Text(
                    text = "Forgot password?",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            CustomTitleButton(
                title = "Login",
                onClick = {
                    if (authViewModel.loginText.isBlank() || authViewModel.loginPassword.isBlank()) {
                        authViewModel.setErrorValue(true)
                    } else {
                        authViewModel.login(
                            onSuccess = {
                                authViewModel.onLoginTextChange("")
                                authViewModel.onLoginPasswordChange("")
                                navController.navigate("main") {
                                    popUpTo(0) { inclusive = true }
                                    restoreState = false
                                }
                            },
                            onFailure = {
                                showErrorDialog = true
                            }
                        )
                        Log.d("LoginScreen", "Login attempted for ${authViewModel.loginText}")
                    }
                }
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )

                TextButton(
                    onClick = {
                        navController.navigate("register") {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.padding(0.dp)
                ) {
                    Text(
                        text = "Create one!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}


