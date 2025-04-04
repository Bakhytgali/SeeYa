package com.example.seeya.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seeya.ui.theme.Poppins
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.ui.theme.grayText
import com.example.seeya.ui.theme.primaryContainerColor
import com.example.seeya.ui.theme.secondaryColor

@Composable
fun OptionButton(
    text: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if(isActive) secondaryColor else primaryContainerColor
        )
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontFamily = Poppins,
            color = if(isActive) bgColor else grayText,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}