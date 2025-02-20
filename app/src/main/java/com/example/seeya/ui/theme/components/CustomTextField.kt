package com.example.seeya.ui.theme.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seeya.ui.theme.grayText
import com.example.seeya.ui.theme.primaryColor
import com.example.seeya.ui.theme.primaryContainerColor
import com.example.seeya.ui.theme.secondaryColor

@Composable
fun CustomTextField(
    text: MutableState<String>,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    numberOfLines: Int = 1,
    ) {

    OutlinedTextField(
        value = text.value,
        textStyle = TextStyle(
            fontSize = 16.sp
        ),
        placeholder = {
            Text(
                text = placeholder,
                color = grayText,
                fontSize = 16.sp
            )
        },
        onValueChange = onValueChange,
        maxLines = numberOfLines,
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = primaryContainerColor,
            unfocusedTextColor = primaryColor,
            focusedTextColor = secondaryColor,
            focusedIndicatorColor = secondaryColor,
            unfocusedIndicatorColor = primaryContainerColor,
            cursorColor = secondaryColor,
        ),
        modifier = modifier
    )
}
