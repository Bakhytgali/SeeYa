package com.example.seeya.ui.theme.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.seeya.data.model.EventTags
import com.example.seeya.viewmodel.auth.AuthViewModel

@Composable
fun RegisterTagsCheck(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    Spacer(Modifier.height(20.dp))
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        CustomTextField(
            text = "Search",
            onValueChange = {

            },
            placeholder = "Search",
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Tags Icon",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Scroll to see more options",
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    style = MaterialTheme.typography.bodySmall
                )

                TextButton(
                    onClick = {
                        authViewModel.registerPrefs.clear()
                    }
                ) {
                    Text(
                        text = "Clear",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            ListOfTags(
                authViewModel = authViewModel
            )
        }
    }
}

@Composable
fun ListOfTags(
    authViewModel: AuthViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        EventTags.entries.forEach { eventTag ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        authViewModel.onRegisterPrefsChange(eventTag.title)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = eventTag.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
                if (authViewModel.checkIfPrefsAdded(eventTag.title)) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Tag is Chosen",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
