package com.example.seeya.ui.theme.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seeya.R
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.ui.theme.components.CustomTextField
import com.example.seeya.ui.theme.primaryContainerColor
import com.example.seeya.ui.theme.grayText
import com.example.seeya.ui.theme.primaryColor
import com.example.seeya.ui.theme.secondaryColor

@Composable
fun LoginScreen(
    email: MutableState<String>,
    password: MutableState<String>,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
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
                .fillMaxWidth(0.8f)
        ) {

            // SeeYa Logo
            Image(
                painter = painterResource(R.drawable.seeya_logo_mono),
                contentDescription = "SeeYa Logo",
                modifier = Modifier.size(90.dp)
            )

            // Login Page title + subtitle section

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Sign In",
                fontSize = 32.sp,
                color = primaryColor,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.login_greet),
                fontSize = 16.sp,
                color = primaryColor,
                textAlign = TextAlign.Center
            )


            // Google Auth Button
            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    // TODO
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryContainerColor
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(5.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.google_logo),
                        contentDescription = "Google Logo Icon",
                        modifier = Modifier.size(30.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Google",
                        color = grayText,
                        fontSize = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "or",
                fontSize = 16.sp,
                color = grayText
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Login with email and password fields
            CustomTextField(
                text = email,
                placeholder = "Email",
                onValueChange = onEmailChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )

            Spacer(modifier = Modifier.height(15.dp))

            CustomTextField(
                text = password,
                placeholder = "Password",
                onValueChange = onPasswordChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )

            // Forgot password link

            TextButton(
                onClick = {
                    // TODO
                }
            ) {
                Text(
                    text = "Forgot password?",
                    color = grayText,
                    fontSize = 14.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Normal
                )
            }

            // Login Button
            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    // TODO
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = secondaryColor,
                )
            ) {
                Text(
                    "Login",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(5.dp),
                )
            }

            // Create Account Link

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account?",
                    color = grayText
                )

                TextButton(
                    onClick = {
                        // TODO
                    },
                ) {
                    Text(
                        text = "Create one!",
                        color = secondaryColor
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun LoginScreenState() {
    val email = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }

    LoginScreen(
        email = email,
        password = password,
        onEmailChange = {
            email.value = it
        },
        onPasswordChange = {
            password.value = it
        }
    )
}