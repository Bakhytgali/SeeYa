package com.example.seeya.seeYaView

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.seeya.ui.theme.ContainerColor
import com.example.seeya.ui.theme.TitleColor

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        label = {
            Text(
                text = label
            )
        },
        onValueChange = {
            // Todo
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedLabelColor = TitleColor,
            focusedLabelColor = TitleColor,

            focusedBorderColor = TitleColor,
            unfocusedTextColor = ContainerColor
        )
    )
}

@Preview
@Composable
private fun CustomTextFieldPreview() {
    CustomTextField(value = "password", label = "Email")
}