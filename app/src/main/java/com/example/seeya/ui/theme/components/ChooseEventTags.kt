package com.example.seeya.ui.theme.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.seeya.data.model.EventTags
import com.example.seeya.ui.theme.grayText
import com.example.seeya.viewmodel.event.EventViewModel

@Composable
fun ChooseEventTags(
    eventViewModel: EventViewModel
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(30.dp),
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
            Text(
                text = "Scroll to see more options",
                color = MaterialTheme.colorScheme.secondaryContainer,
                style = MaterialTheme.typography.bodySmall
            )
            ListOfTags(
                eventViewModel = eventViewModel
            )
        }
    }
}

@Composable
fun ListOfTags(
    eventViewModel: EventViewModel
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(EventTags.entries) { eventTag ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        eventViewModel.onNewEventTags(eventTag.title)
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
                if (eventViewModel.eventTags == eventTag.title) {
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
