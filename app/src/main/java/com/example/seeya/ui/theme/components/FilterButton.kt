package com.example.seeya.ui.theme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
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
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if(isActive) secondaryColor else primaryContainerColor,
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = if(isActive) bgColor else grayText,
            fontFamily = Poppins
        )
    }
}