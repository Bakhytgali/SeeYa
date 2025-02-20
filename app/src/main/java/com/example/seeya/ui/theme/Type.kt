package com.example.seeya.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.seeya.R

// "SeeYa" mobile application main font
val Roboto = FontFamily(
    Font(R.font.roboto_bold, FontWeight.Bold),
    Font(R.font.roboto_regular, FontWeight.Normal)
)

val SeeYaTypography = Typography(
    bodyLarge = TextStyle(fontFamily = Roboto, fontSize = 16.sp),
)