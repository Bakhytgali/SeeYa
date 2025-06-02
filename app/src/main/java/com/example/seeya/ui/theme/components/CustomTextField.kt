package com.example.seeya.ui.theme.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seeya.ui.theme.Poppins

@Composable
fun CustomTextField(
    text: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    numberOfLines: Int = 1,
    isActive: Boolean = true,
    onClick: () -> Unit = {},
    trailingIcon: (@Composable (() -> Unit))? = null,
    leadingIcon: (@Composable (() -> Unit))? = null,
    isPassword: Boolean = false,
    limit: Int = 100,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    val visualTransformation = if (isPassword && !isPasswordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    OutlinedTextField(
        value = text,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
        },
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontFamily = Poppins
        ),
        onValueChange = {
            if (it.length <= limit) {
                onValueChange(it)
            }
        },
        maxLines = numberOfLines,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledTextColor = MaterialTheme.colorScheme.onBackground,
        ),
        enabled = isActive,
        modifier = modifier.clickable { onClick() },
        trailingIcon = {
            when {
                isPassword -> {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                        tint = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable { isPasswordVisible = !isPasswordVisible }
                    )
                }

                trailingIcon != null -> trailingIcon()
            }
        },
        leadingIcon = leadingIcon,
        visualTransformation = visualTransformation
    )
}
