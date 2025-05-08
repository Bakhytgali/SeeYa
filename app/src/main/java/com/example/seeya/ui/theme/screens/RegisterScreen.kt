package com.example.seeya.ui.theme.screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.R
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.ui.theme.components.CustomTextField
import com.example.seeya.ui.theme.components.Dialog
import com.example.seeya.ui.theme.components.SeeYaLogo
import com.example.seeya.viewmodel.auth.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
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

            SeeYaLogo()

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.login_greet),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            CustomTextField(
                text = authViewModel.registerEmail,
                placeholder = "Email",
                onValueChange = {
                    authViewModel.onRegisterEmailChange(it)
                    authViewModel.setErrorValue(false)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            CustomTextField(
                text = authViewModel.registerPassword,
                placeholder = "Password",
                onValueChange = {
                    authViewModel.onRegisterPasswordChange(it)
                    authViewModel.setErrorValue(false)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            CustomTextField(
                text = authViewModel.registerName,
                placeholder = "Your Name",
                onValueChange = {
                    authViewModel.onRegisterNameChange(it)
                    authViewModel.setErrorValue(false)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            CustomTextField(
                text = authViewModel.registerSurname,
                placeholder = "Your Surname",
                onValueChange = {
                    authViewModel.onRegisterSurnameChange(it)
                    authViewModel.setErrorValue(false)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            CustomTextField(
                text = authViewModel.registerUsername,
                placeholder = "Username",
                onValueChange = {
                    authViewModel.onRegisterUsernameChange(it)
                    authViewModel.setErrorValue(false)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            if (authViewModel.isError) {
                Text(
                    text = "Make sure to fill all the spaces.",
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (
                        authViewModel.registerEmail.isBlank() ||
                        authViewModel.registerPassword.isBlank() ||
                        authViewModel.registerName.isBlank() ||
                        authViewModel.registerSurname.isBlank() ||
                        authViewModel.registerUsername.isBlank()
                    ) {
                        authViewModel.setErrorValue(true)
                    } else {
                        authViewModel.register(
                            onSuccess = {
                                authViewModel.setDialogOpen(true)
                            },
                            onError = {
                                Toast.makeText(
                                    authViewModel.getApplication(),
                                    "Successfully joined!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                        Log.d(
                            "AuthorizeScreen",
                            "Authorizing for ${authViewModel.registerEmail}..."
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Continue",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(5.dp),
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )

                TextButton(
                    onClick = {
                        navController.navigate("login") {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.padding(0.dp)
                ) {
                    Text(
                        text = "Login!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
