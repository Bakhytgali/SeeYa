package com.example.seeya.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.seeya.viewmodel.auth.AuthViewModel

@Composable
fun RegisterCodeVerify(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            text = authViewModel.verifyCodeText,
            onValueChange = {
                authViewModel.onVerifyCodeTextChange(it)
            },
            placeholder = "Verify Code",
        )
    }
}