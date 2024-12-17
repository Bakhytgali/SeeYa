package com.example.seeya.seeYaView

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seeya.ui.theme.InterFont

@Composable
fun SeeYaLogo(fontSize: Int = 32) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF088F8F), // Teal
                            Color(0xFFA78BFA) // Light Teal
                        )
                    ),
                    fontFamily = InterFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize.sp,
                    letterSpacing = 1.2.sp
                )
            ) {
                append("Seeya")
            }
        },
        modifier = Modifier.padding(8.dp)
    )
}