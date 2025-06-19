package com.example.seeya.ui.theme.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.seeya.viewmodel.auth.AuthViewModel

@Composable
fun RegistrationImageCheck(
    authViewModel: AuthViewModel,
    onSkipStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                authViewModel.handleImageUri(it)
            }
        }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable {
                    launcher.launch("image/*")
                },
            contentAlignment = Alignment.Center
        ) {
            authViewModel.imageUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: SeeYaLogo(
                color = MaterialTheme.colorScheme.secondaryContainer
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Tap to choose",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondaryContainer
        )

        Spacer(Modifier.height(30.dp))

        TextButton(
            onClick = onSkipStep
        ) {
            Text(
                text = "Skip",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondaryContainer,
                fontWeight = FontWeight.Medium
            )
        }
    }
}