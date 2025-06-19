package com.example.seeya.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.data.model.Participant
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.event.EventViewModel
import com.example.seeya.viewmodel.search.SearchViewModel

@Composable
fun EventUsersPage(
    eventViewModel: EventViewModel,
    bottomBarViewModel: BottomBarViewModel,
    authViewModel: AuthViewModel,
    navController: NavController,
    searchViewModel: SearchViewModel,
) {
    LaunchedEffect(Unit) {
        bottomBarViewModel.onActivePageChange("eventUsers")
    }
    MainScaffold(
        title = "",
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "Nav Icon",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 10.dp)
            )
        },
        navController = navController,
        bottomBarViewModel = bottomBarViewModel,
        authViewModel = authViewModel,
        content = { mod ->
            EventUsersContent(
                users = emptyList(),
                modifier = mod
            )
        },
        searchViewModel = searchViewModel
    )
}

@Composable
fun EventUsersContent(
    users: List<Participant>?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        if (users.isNullOrEmpty()) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Seems like no one here!",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    itemsIndexed(users) { index, user ->
                        EventUserCard(
                            index = index + 1,
                            user = user,
                            modifier = modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EventUserCard(
    user: Participant,
    index: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "$index",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondaryContainer
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp)
            ) {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "${user.name} ${user.surname}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                )
            }
        }
    }
}