package com.example.seeya.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seeya.R
import com.example.seeya.viewmodel.auth.AuthViewModel

@Composable
fun RegistrationPasswordCheck(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomTextField(
            text = authViewModel.registerPassword,
            onValueChange = {
                authViewModel.onRegisterPasswordChange(it)
            },
            placeholder = "Password",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(25.dp))

        CustomTextField(
            text = authViewModel.registerConfirmPassword,
            onValueChange = {
                authViewModel.onRegisterConfirmPasswordChange(it)
            },
            placeholder = "Confirm Password",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(15.dp))

        Text(
            text = authViewModel.registerIsPasswordConfirmedIndicator,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth().padding(start = 5.dp),
            fontSize = 14.sp,
            color = authViewModel.registerIsPasswordConfirmedIndicatorColor
        )
    }
}