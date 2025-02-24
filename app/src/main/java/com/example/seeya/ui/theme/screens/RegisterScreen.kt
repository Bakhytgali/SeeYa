package com.example.seeya.ui.theme.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.R
import com.example.seeya.ui.theme.Poppins
import com.example.seeya.ui.theme.Unbounded
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.ui.theme.components.CustomTextField
import com.example.seeya.ui.theme.components.Dialog
import com.example.seeya.ui.theme.grayText
import com.example.seeya.ui.theme.primaryColor
import com.example.seeya.ui.theme.secondaryColor
import com.example.seeya.viewmodel.auth.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val name = remember {
        mutableStateOf("")
    }
    val surname = remember {
        mutableStateOf("")
    }
    val username = remember {
        mutableStateOf("")
    }
    val isError = remember {
        mutableStateOf(false)
    }

    val openAlertDialog = remember {
        mutableStateOf(false)
    }

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

            if (openAlertDialog.value) {
                Dialog(
                    openAlertDialog = openAlertDialog,
                    onConfirm = {
                        openAlertDialog.value = true
                        navController.navigate("login") {
                            popUpTo(0)
                        }
                    },
                    onDismiss = {
                        openAlertDialog.value = true
                    },
                    dialogText = "You'll be directed to the Login page where you can enter your credentials and authorize",
                    dialogTitle = "Successfully Registered"
                )
            }

            // SeeYa Logo
            Image(
                painter = painterResource(R.drawable.seeya_logo_mono),
                contentDescription = "SeeYa Logo",
                modifier = Modifier.size(90.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Sign Up",
                fontSize = 32.sp,
                color = primaryColor,
                fontFamily = Unbounded,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.login_greet),
                fontSize = 16.sp,
                color = primaryColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            CustomTextField(
                text = email,
                placeholder = "Email",
                onValueChange = {
                    email.value = it
                    isError.value = false
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            CustomTextField(
                text = password,
                placeholder = "Password",
                onValueChange = {
                    password.value = it
                    isError.value = false
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            CustomTextField(
                text = name,
                placeholder = "Your Name",
                onValueChange = {
                    name.value = it
                    isError.value = false
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            CustomTextField(
                text = surname,
                placeholder = "Your Surname",
                onValueChange = {
                    surname.value = it
                    isError.value = false
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            CustomTextField(
                text = username,
                placeholder = "Username",
                onValueChange = {
                    username.value = it
                    isError.value = false
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            if (isError.value) {
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
                        email.value.isBlank() ||
                        password.value.isBlank() ||
                        name.value.isBlank() ||
                        surname.value.isBlank() ||
                        username.value.isBlank()
                    ) {
                        isError.value = true
                    } else {
                        authViewModel.register(
                            name = name.value,
                            surname = surname.value,
                            email = email.value,
                            password = password.value,
                            username = username.value,
                            onSuccess =  {
                                openAlertDialog.value = true
                            },
                            onError = {
                                Toast.makeText(context, "Successfully joined!", Toast.LENGTH_SHORT).show()
                            }
                        )
                        Log.d("AuthorizeScreen", "Authorizing for ${email.value}...")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
            ) {
                Text(
                    "Continue",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = Unbounded,
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
                    color = grayText
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
                        color = secondaryColor,
                        fontSize = 16.sp,
                        fontFamily = Poppins
                    )
                }
            }
        }
    }
}
