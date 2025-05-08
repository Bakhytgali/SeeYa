package com.example.seeya.ui.theme.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.seeya.ui.theme.Poppins
import com.example.seeya.ui.theme.Unbounded
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.ui.theme.secondaryColor

@Composable
fun Dialog(
    openAlertDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    modifier: Modifier = Modifier
) {
    if (openAlertDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    Text(
                        text = dialogTitle,
                        fontFamily = Unbounded,
                        color = secondaryColor
                    )
                }
            },
            text = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = dialogText,
                        fontFamily = Poppins
                    )
                }
            },
            containerColor = bgColor,
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                            // openAlertDialog = false
                    }
                ) {
                    Text("Login", fontFamily = Poppins)

                }
            },
            modifier = modifier
        )
    }
}
