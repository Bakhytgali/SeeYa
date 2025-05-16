package com.example.seeya.ui.theme.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seeya.ui.theme.Poppins
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.ui.theme.grayText
import com.example.seeya.ui.theme.primaryContainerColor
import com.example.seeya.ui.theme.secondaryColor

@Composable
fun FilterButton(
    text: String,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if(isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = if(isActive) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.secondaryContainer
        )
    }
}