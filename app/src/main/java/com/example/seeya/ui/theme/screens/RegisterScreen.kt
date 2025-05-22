package com.example.seeya.ui.theme.screens

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.R
import com.example.seeya.ui.theme.components.CustomTitleButton
import com.example.seeya.ui.theme.components.Dialog
import com.example.seeya.ui.theme.components.RegisterCodeVerify
import com.example.seeya.ui.theme.components.RegisterTagsCheck
import com.example.seeya.ui.theme.components.RegistrationEmailCheck
import com.example.seeya.ui.theme.components.RegistrationImageCheck
import com.example.seeya.ui.theme.components.RegistrationPasswordCheck
import com.example.seeya.ui.theme.components.RegistrationUsernameCheck
import com.example.seeya.ui.theme.components.SeeYaLogo
import com.example.seeya.viewmodel.auth.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {

    BackHandler {
        when (authViewModel.registrationStep) {
            1 -> {}
            2, 3, 4, 5 -> authViewModel.onRegistrationStepChange(authViewModel.registrationStep - 1)
            else -> {}
        }
    }

    val onClickHandler = {
        when (authViewModel.registrationStep) {
            1 -> {
                Log.d("auth", "${authViewModel.registrationStep}")
                if (authViewModel.registerEmail.isNotEmpty()) {
                    authViewModel.verifyEmail { success, error ->
                        if (success) {
                            authViewModel.onRegistrationStepChange(nextStep = authViewModel.registrationStep + 1)
                        } else {
                            Log.d("Register Email", error)
                        }
                    }
                } else {

                }
            }

            2 -> {
                if (authViewModel.verifyCodeText.isNotEmpty()) {
                    authViewModel.verifyCode { success, error ->
                        if (success) {
                            authViewModel.onRegistrationStepChange(nextStep = authViewModel.registrationStep + 1)
                        } else {
                            Log.d("Register Code", error)
                        }
                    }
                }
                Log.d("auth", "${authViewModel.registrationStep}")
            }

            3 -> {
                Log.d("auth", "${authViewModel.registrationStep}")
                // TODO
                authViewModel.onRegistrationStepChange(nextStep = authViewModel.registrationStep + 1)

            }

            4 -> {
                Log.d("auth", "${authViewModel.registrationStep}")
                // TODO
                authViewModel.onRegistrationStepChange(nextStep = authViewModel.registrationStep + 1)

            }

            5 -> {
                Log.d("auth", "${authViewModel.registrationStep}")
                authViewModel.onRegistrationStepChange(nextStep = authViewModel.registrationStep + 1)
            }

            6 -> {
                Log.d("auth", "${authViewModel.registrationStep}")
                authViewModel.register { isSuccess, isError ->
                    if (isSuccess) {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = false }
                            launchSingleTop = true
                        }
                    } else {
                        Log.d("Register User", isError)
                    }
                }
            }

            else -> {

            }
        }
    }

    val registerStepTitle = when (authViewModel.registrationStep) {
        1 -> stringResource(R.string.register_title_step_1)
        3 -> stringResource(R.string.register_title_step_2)
        4 -> stringResource(R.string.register_title_step_3)
        5 -> stringResource(R.string.register_title_step_4)
        6 -> stringResource(R.string.register_title_step_5)
        2 -> stringResource(R.string.register_title_step_6)
        else -> ""
    }

    Scaffold { paddingValues ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
        ) {

            when (authViewModel.registrationStep) {
                1 -> {}
                2, 3, 4, 5 -> {
                    IconButton(
                        onClick = {
                            authViewModel.onRegistrationStepChange(authViewModel.registrationStep - 1)
                        },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Register Navigation Icon",
                            tint = MaterialTheme.colorScheme.secondaryContainer,
                        )
                    }
                }

                else -> {}
            }

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.9f)
            ) {

                if (authViewModel.registerDialogIsOpen) {
                    Dialog(
                        openAlertDialog = authViewModel.registerDialogIsOpen,
                        onConfirm = {
                            authViewModel.setDialogOpen(true)
                            navController.navigate("login") {
                                popUpTo(0)
                            }
                        },
                        onDismiss = {
                            authViewModel.setDialogOpen(true)
                        },
                        dialogText = "You'll be directed to the Login page where you can enter your credentials and authorize",
                        dialogTitle = "Successfully Registered"
                    )
                }

                Spacer(Modifier.height(30.dp))

                SeeYaLogo()

                Spacer(modifier = Modifier.height(50.dp))

                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = registerStepTitle,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    color = MaterialTheme.colorScheme.secondaryContainer
                )

                when (authViewModel.registrationStep) {
                    1 -> {
                        RegistrationEmailCheck(
                            authViewModel = authViewModel,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    2 -> {
                        RegisterCodeVerify(
                            authViewModel = authViewModel,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    3 -> {
                        RegistrationPasswordCheck(
                            authViewModel = authViewModel,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    4 -> {
                        RegistrationUsernameCheck(
                            authViewModel = authViewModel,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    5 -> {
                        RegisterTagsCheck(
                            authViewModel = authViewModel,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    6 -> {
                        RegistrationImageCheck(
                            authViewModel = authViewModel,
                            onSkipStep = {
                                authViewModel.register { isSuccess, isError ->
                                    if(isSuccess) {
                                        navController.navigate("login") {
                                            popUpTo(0) { inclusive = false }
                                            launchSingleTop = true
                                        }
                                    } else {
                                        Log.d("Register User", isError)
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    else -> {

                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                CustomTitleButton(
                    title = when (authViewModel.registrationStep) {
                        1, 3, 4, 5 -> "Continue"
                        6 -> "Sign Up!"
                        2 -> "Verify!"
                        else -> "Error occurred, sorry!"
                    },
                    onClick = { onClickHandler() }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Already have an account?",
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    TextButton(
                        onClick = {
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = false }
                                launchSingleTop = true
                            }
                            authViewModel.clearRegisterEntries()
                        }
                    ) {
                        Text(
                            text = "Login!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}
