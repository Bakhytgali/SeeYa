package com.example.seeya.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.background
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
                authViewModel = authViewModel
            )
        },
    )
}

@Composable
fun SearchScreenContent(
    searchViewModel: SearchViewModel,
    onClick: (String) -> Unit,
    authViewModel: AuthViewModel,
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
                authViewModel = authViewModel
            )
        }
    }
}

@Composable
fun SearchResultDisplay(
    authViewModel: AuthViewModel,
    onClick: (String) -> Unit,
    searchViewModel: SearchViewModel,
    modifier: Modifier = Modifier
) {
    val searchState by searchViewModel.searchState.collectAsState()
    Box(
        modifier = modifier
    ) {
        when (searchState) {
            is SearchState.Idle -> {
                Text(
                    text = "Nothing here!\n" + "Try typing 3 symbols to search!",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    textAlign = TextAlign.Center
                )
            }

            is SearchState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is SearchState.Success -> {
                val users = (searchState as SearchState.Success).users

                val currentUserId = authViewModel.currentUser.value?.id

                val filteredUsers = if (currentUserId != null) {
                    users.filter { it.id != currentUserId }
                } else {
                    users
                }

                if (filteredUsers.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Nothing here!\n" + "Try searching for other!",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(filteredUsers) { index, user ->
                            SearchUserItem(user = user, index = index + 1, onClick = onClick)
                        }
                    }
                }
            }

            else -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Nothing here!\n" + "Try searching for other!",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun SearchUserItem(
    onClick: (String) -> Unit,
    user: SearchUser,
    index: Int,
    modifier: Modifier = Modifier
) {
    Log.d("Search User", user.id)
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
}