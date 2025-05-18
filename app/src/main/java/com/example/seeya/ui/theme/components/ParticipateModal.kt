package com.example.seeya.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seeya.data.model.Event
import com.example.seeya.ui.theme.Poppins

@Composable
fun ParticipateModal(
    event: Event,
    onOk: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onOk,
        dismissButton = {},
        confirmButton = {
            TextButton(
                onClick = onOk
            ) {
                Text(
                    text = "Ok",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        title = {
            Text(
                text = "You're in!",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "\uD83D\uDC4B\uD83C\uDFFB See Ya",
                    fontSize = 18.sp,
                    fontFamily = Poppins,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                fontSize = 18.sp,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Normal,
                            )
                        ) {
                            append("on ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold,
                            )
                        ) {
                            append("10.05 22:00")
                        }
                    },
                    textAlign = TextAlign.Center

                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                fontSize = 18.sp,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Normal,
                            )
                        ) {
                            append("here: ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 18.sp,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold,
                            )
                        ) {
                            append(event.location)
                        }
                    },
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}