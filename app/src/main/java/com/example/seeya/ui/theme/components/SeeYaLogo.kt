package com.example.seeya.ui.theme.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun SeeYaLogo(
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    fontSize: Int = 48
) {
    Text(
        text = "Sy!",
        style = MaterialTheme.typography.titleLarge,
        color = color,
        fontSize = fontSize.sp,
        modifier = modifier
    )
}