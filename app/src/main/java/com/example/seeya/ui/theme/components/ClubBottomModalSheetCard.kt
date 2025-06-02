package com.example.seeya.ui.theme.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.seeya.data.model.ClubTypes
import com.example.seeya.viewmodel.clubs.ClubsViewModel

@Composable
fun ClubBottomModalSheetCard(
    onClick: () -> Unit,
    clubTypes: ClubTypes,
    clubsViewModel: ClubsViewModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.clickable {
            onClick()
        }.fillMaxWidth()
    ) {
        RadioButton(
            selected = clubTypes.type == clubsViewModel.createNewClubType,
            onClick = onClick
        )
        Column {
            Text(
                text = clubTypes.type,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = clubTypes.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
        }
    }
}