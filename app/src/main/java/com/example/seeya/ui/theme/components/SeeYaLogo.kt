package com.example.seeya.ui.theme.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.seeya.ui.theme.primaryColor

@Composable
fun SeeYaLogo(
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary
) {
    Text(
        text = "Sy!",
        style = MaterialTheme.typography.titleLarge,
        color = color
    )
}