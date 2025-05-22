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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seeya.viewmodel.auth.AuthViewModel

@Composable
fun RegistrationUsernameCheck(
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
            text = authViewModel.registerUsername,
            onValueChange = {
                authViewModel.onRegisterUsernameChange(it)
            },
            placeholder = "Username",
        )
        Spacer(Modifier.height(15.dp))
        Text(
            text = "Username must be in lowercase",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth().padding(start = 5.dp),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondaryContainer
        )
        Spacer(Modifier.height(15.dp))

        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            text = authViewModel.registerName,
            onValueChange = {
                authViewModel.onRegisterNameChange(it)
            },
            placeholder = "First Name",
        )

        Spacer(Modifier.height(15.dp))

        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            text = authViewModel.registerSurname,
            onValueChange = {
                authViewModel.onRegisterSurnameChange(it)
            },
            placeholder = "Last Name",
        )
    }
}