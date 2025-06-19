package com.example.seeya.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.data.model.SearchUser
import com.example.seeya.ui.theme.components.CustomTextField
import com.example.seeya.ui.theme.components.EventCard
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.search.SearchState
import com.example.seeya.viewmodel.search.SearchViewModel

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    navController: NavController,
    bottomBarViewModel: BottomBarViewModel,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    MainScaffold(
        title = "Search",
        navController = navController,
        bottomBarViewModel = bottomBarViewModel,
        searchViewModel = searchViewModel,
        authViewModel = authViewModel,
        content = { mod ->
            SearchScreenContent(
                searchViewModel,
                onClick = { userId ->
                    Log.d("Search User", userId)
                    navController.navigate("profile/$userId") {
                        launchSingleTop = true
                    }
                },
                modifier = mod,
                authViewModel = authViewModel,
                navController = navController
            )
        },
    )
}

@Composable
fun SearchScreenContent(
    searchViewModel: SearchViewModel,
    onClick: (String) -> Unit,
    authViewModel: AuthViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(20.dp))
            CustomTextField(
                text = searchViewModel.searchText,
                onValueChange = {
                    searchViewModel.onSearchTextChange(it)
                },
                placeholder = "Search",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.size(30.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(15.dp))

            SearchResultDisplay(
                searchViewModel = searchViewModel,
                onClick = onClick,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                authViewModel = authViewModel,
                navController = navController
            )
        }
    }
}

@Composable
fun SearchResultDisplay(
    navController: NavController,
    authViewModel: AuthViewModel,
    onClick: (String) -> Unit,
    searchViewModel: SearchViewModel,
    modifier: Modifier = Modifier
) {
    val searchState by searchViewModel.searchUserState.collectAsState()
    val events by searchViewModel.searchEventsResults.collectAsState()
    val isLoading by searchViewModel.loading.collectAsState()
    val error by searchViewModel.error.collectAsState()

    Box(modifier = modifier) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            error != null -> {
                Text(
                    text = "Error: $error",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            else -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    when (searchState) {
                        is SearchState.Idle -> {
                            Text(
                                text = "Nothing here!\nTry typing 3 symbols to search!",
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                textAlign = TextAlign.Center
                            )
                        }

                        is SearchState.Success -> {
                            val users = (searchState as SearchState.Success).users
                            val currentUserId = authViewModel.currentUser.value?.id
                            val filteredUsers = users.filterNot { it.id == currentUserId }

                            if (filteredUsers.isEmpty()) {
                                Text(
                                    text = "No users found",
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                )
                            } else {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    contentPadding = PaddingValues(vertical = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    item {
                                        Text(
                                            text = "Users",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.secondaryContainer,
                                            fontSize = 14.sp,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Start
                                        )
                                    }
                                    itemsIndexed(filteredUsers) { index, user ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "${index + 1}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.secondaryContainer
                                            )
                                            SearchUserItem(user = user, onClick = onClick)
                                        }
                                    }
                                    item {
                                        Text(
                                            text = "Events",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.secondaryContainer,
                                            fontSize = 14.sp,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Start
                                        )
                                    }
                                    itemsIndexed(events) { index, event ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "${index + 1}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.secondaryContainer
                                            )
                                            EventCard(
                                                event = event,
                                                modifier = Modifier.weight(1f)
                                                    .clickable { navController.navigate("event/${event.eventId}") }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        is SearchState.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }

                        else -> {
                            Text(
                                text = "Nothing here!\nTry searching for other!",
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchUserItem(
    onClick: (String) -> Unit,
    user: SearchUser,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        onClick = { onClick(user.id) }
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