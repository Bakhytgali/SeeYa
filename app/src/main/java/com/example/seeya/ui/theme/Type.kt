package com.example.seeya.ui.theme

import  androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.seeya.R

// "SeeYa" mobile application main font
val Poppins = FontFamily(
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_regular, FontWeight.Normal)
)

val Unbounded = FontFamily(
    Font(R.font.unbounded_bold, FontWeight.Bold),
    Font(R.font.unbounded_regular, FontWeight.Normal)
)

val SeeYaTypography = Typography(
    bodyLarge = TextStyle(fontFamily = Poppins, fontSize = 16.sp, color = primaryColor),
)
