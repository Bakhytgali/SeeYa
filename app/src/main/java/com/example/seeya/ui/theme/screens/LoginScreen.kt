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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seeya.R
import com.example.seeya.ui.theme.*
import com.example.seeya.ui.theme.components.CustomTextField
import com.example.seeya.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isError = remember { mutableStateOf(false) }

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

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Sign In",
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
                    isError.value = false // Сбрасываем ошибку при вводе
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            CustomTextField(
                text = password,
                placeholder = "Password",
                onValueChange = {
                    password.value = it
                    isError.value = false // Сбрасываем ошибку при вводе
                },
                modifier = Modifier.fillMaxWidth()
            )

            if (isError.value) {
                Text(
                    text = "Email and Password cannot be empty!",
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (email.value.isBlank() || password.value.isBlank()) {
                        isError.value = true
                    } else {
                        authViewModel.login(email.value, password.value)
                        Log.d("LoginScreen", "Login successful for ${email.value}")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
            ) {
                Text(
                    "Login",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = Unbounded,
                    modifier = Modifier.padding(5.dp),
                )
            }
        }
    }
}

