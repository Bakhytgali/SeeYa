package com.example.seeya.ui.theme.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.ModeEdit
import androidx.compose.material.icons.outlined.Nightlight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBottomModal(
    onClose: () -> Unit,
    onEditProfile: () -> Unit,
    onExitProfile: () -> Unit,
    onThemeSwitch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    val optionButtons = listOf(onEditProfile, onExitProfile, onThemeSwitch)
    val optionTitles = listOf("Edit", "Exit", "Switch Themes")
    val optionSubtitles =
        listOf("Change the profile info", "Exit from account", "Switch Themes")
    val optionIcons = listOf<@Composable () -> Unit>(
        {
            Icon(
                imageVector = Icons.Outlined.ModeEdit,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(20.dp)
            )
        },
        {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(20.dp)
            )
        },
        {
            Icon(
                imageVector = Icons.Outlined.Nightlight,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(20.dp)
            )
        }
    )
    val optionTitleColors = listOf(
        MaterialTheme.colorScheme.onBackground,
        MaterialTheme.colorScheme.onBackground,
        MaterialTheme.colorScheme.onBackground,
    )

    ModalBottomSheet(
        onDismissRequest = {
            coroutineScope.launch {
                sheetState.hide()
                onClose()
            }
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 5.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Profile Options",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            for (i in 0..2) {
                OptionRow(
                    optionTitle = optionTitles[i],
                    optionTitleColor = optionTitleColors[i],
                    optionSubtitle = optionSubtitles[i],
                    optionOnClick = optionButtons[i],
                    icon = optionIcons[i]
                )
            }
        }
    }
}

@Composable
fun OptionRow(
    optionTitle: String,
    optionTitleColor: Color,
    optionSubtitle: String,
    optionOnClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(0.9f).clickable { optionOnClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        icon()
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = optionTitle,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = optionTitleColor
            )

            Text(
                text = optionSubtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
        }
    }
}