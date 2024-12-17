package com.example.seeya.seeYaView

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seeya.ui.theme.BackgroundBlack
import com.example.seeya.ui.theme.ContainerColor
import com.example.seeya.ui.theme.InterFont
import com.example.seeya.ui.theme.TitleColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.Transparent
                ),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp, bottom = 15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        SeeYaLogo()
                    }
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF000000), // Vibrant blue
                            Color(0xFF232b2b)  // Muted teal
                        )
                    )
                )
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(120.dp))

                Row {
                    Text(
                        text = "Make event creating ",
                        fontFamily = InterFont,
                        color = ContainerColor,
                        fontSize = 24.sp,
                        letterSpacing = 1.2.sp
                    )

                    Text(
                        text = "easy.",
                        fontFamily = InterFont,
                        color = TitleColor,
                        fontSize = 24.sp,
                        letterSpacing = 1.2.sp
                    )
                }

                Spacer(modifier = Modifier.height(80.dp))

                Text(
                    text = "Login",
                    fontFamily = InterFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(0.85f),
                    fontSize = 28.sp,
                    letterSpacing = 1.2.sp
                )

                Spacer(modifier = Modifier.height(30.dp))

                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "Email",
                    label = "Email"
                )

                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "Email",
                    label = "Email"
                )

                Spacer(modifier = Modifier.height(15.dp))

                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .align(Alignment.Start)
                    ,
                    onClick = {
                        // TODO
                    }
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Forgot your password?",
                            fontFamily = InterFont,
                            fontSize = 13.sp,
                            color = TitleColor.copy(alpha = 0.8f),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = {
                        // TODO
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TitleColor
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        text = "Sign In",
                        fontSize = 18.sp,
                        fontFamily = InterFont,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
                        letterSpacing = 1.2.sp
                    )
                }
            }
        }
    }
}