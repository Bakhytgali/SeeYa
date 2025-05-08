package com.example.seeya.ui.theme.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CreateEventSoothe(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit,
    currentStep: Int,
    onButtonClick: () -> Unit,
    titleFilled: Boolean,
    tagsChosen: Boolean,
    pictureChosen: Boolean,
    locationAndDescriptionFilled: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondaryContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(300.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                content()
            }
            Spacer(modifier = Modifier.height(20.dp))
            if (currentStep == 1 ) {
                CustomTitleButton(
                    title = "Continue",
                    onClick = onButtonClick,
                    isActive = titleFilled
                )
            } else if(currentStep == 2) {
                CustomTitleButton(
                    title = "Continue",
                    onClick = onButtonClick,
                    isActive = tagsChosen
                )
            } else if(currentStep == 3) {
                CustomTitleButton(
                    title = "Continue",
                    onClick = onButtonClick,
                    isActive = pictureChosen
                )
            } else if(currentStep == 4) {
                CustomTitleButton(
                    title = "Continue",
                    onClick = onButtonClick,
                    isActive = locationAndDescriptionFilled
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            CustomProgressIndicator(
                steps = 4,
                activeSteps = currentStep
            )
        }
    }
}