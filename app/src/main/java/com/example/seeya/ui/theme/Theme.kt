package com.example.seeya.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    primary = secondaryColor, // который Фиолетовый
    onBackground = primaryColor, // который Черно-Синий
    primaryContainer = primaryContainerColor,
    secondaryContainer = grayText,
    background = bgColor
)

private val LightColorScheme = lightColorScheme(
    primary = secondaryColor, // который Фиолетовый
    onBackground = primaryColor, // который Черно-Синий
    primaryContainer = primaryContainerColor,
    secondaryContainer = grayText,
    background = bgColor
)

@Composable
fun SeeYaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val systemUIController = rememberSystemUiController()

    SideEffect {
        systemUIController.setSystemBarsColor(
            color = Color.White
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SeeYaTypography,
        content = content
    )
}