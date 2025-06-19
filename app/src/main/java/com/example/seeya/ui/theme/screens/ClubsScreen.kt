package com.example.seeya.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.R
import com.example.seeya.data.model.Club
import com.example.seeya.data.model.Creator
import com.example.seeya.ui.theme.components.CustomTextField
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.clubs.ClubsState
import com.example.seeya.viewmodel.clubs.ClubsViewModel
import com.example.seeya.viewmodel.search.SearchViewModel
import java.time.LocalDate

@Composable
fun ClubsScreen(
    navController: NavController,
    bottomBarViewModel: BottomBarViewModel,
    authViewModel: AuthViewModel,
    clubsViewModel: ClubsViewModel,
    searchViewModel: SearchViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        clubsViewModel.getMyClubs()
    }

    MainScaffold(
        title = "Clubs",
        bottomBarViewModel = bottomBarViewModel,
        navController = navController,
        authViewModel = authViewModel,
        content = { mod ->
            ClubsScreenContent(
                clubsViewModel = clubsViewModel,
                modifier = mod,
                navController = navController,
            )
        },
        searchViewModel = searchViewModel,
    )
}

@Composable
fun ClubsScreenContent(
    navController: NavController,
    clubsViewModel: ClubsViewModel,
    modifier: Modifier = Modifier
) {

    var searchClubsValue by remember {
        mutableStateOf("")
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight()
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(20.dp))

            CustomTextField(
                text = searchClubsValue,
                onValueChange = {
                    searchClubsValue = it
                },
                placeholder = "Search Clubs",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search Clubs Icon",
                        tint = MaterialTheme.colorScheme.secondaryContainer
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            if(searchClubsValue.isBlank()) {
                when (clubsViewModel.myClubs) {
                    is ClubsState.Idle -> {}
                    is ClubsState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is ClubsState.Empty -> {
                        Text(
                            "Super Empty Here!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                    }

                    is ClubsState.Success -> {
                        FollowingClubsBlock(
                            clubs = (clubsViewModel.myClubs as ClubsState.Success).clubs,
                            modifier = Modifier.fillMaxWidth(),
                            navController = navController
                        )
                    }

                    is ClubsState.Error -> {
                        val error = (clubsViewModel.myClubs as ClubsState.Error)
                        Text(
                            text = error.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Red
                        )
                    }

                    else -> {}
                }

                Spacer(Modifier.height(20.dp))

                RecommendedGroupsBlock(modifier = Modifier.fillMaxWidth(), navController = navController)
            } else {
                SearchClubsList(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }

        }
    }
}

@Composable
fun SearchClubsList(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Nothing Here!"
        )
    }
}

@Composable
fun ClubCard(
    club: Club,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 20.dp, top = 15.dp, bottom = 15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.95f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = club.clubTags,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )

                Text(
                    text = "${club.participants.size} Joined",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = club.name,
                style = MaterialTheme.typography.titleSmall,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        }
    }
}

@Composable
fun FollowingClubsBlock(
    navController: NavController,
    clubs: List<Club>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 15.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Following",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                TextButton(
                    onClick = {
                        // TODO
                    },
                    modifier = Modifier.padding(0.dp)
                ) {
                    Text(
                        text = "See All",
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            BriefListOfClub(clubs = clubs, navController = navController)
        }
    }
}

@Composable
fun BriefListOfClub(
    clubs: List<Club>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(30.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        items(clubs) { club ->
            BriefClubCard(
                club = club,
                onClick = {
                    Log.d("Navigating", club.id)
                    navController.navigate("clubs/$it")
                }
            )
        }
    }
}

@Composable
fun BriefClubCard(
    onClick: (String) -> Unit,
    club: Club,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        modifier = Modifier.height(100.dp),
        onClick = { onClick(club.id) }
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier.padding(10.dp)
        ) {
//            club.clubPicture?.let { base64 ->
//                val bitmap = decodeBase64ToBitmap(base64)
//                bitmap?.let {
//                    Image(
//                        bitmap = it.asImageBitmap(),
//                        contentDescription = "Club Logo",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .size(70.dp)
//                            .clip(RoundedCornerShape(8.dp))
//                    )
//                }
//            }
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = club.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = club.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }

}

@Composable
fun RecommendedGroupsBlock(modifier: Modifier = Modifier, navController: NavController) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 15.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recommended",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                TextButton(
                    onClick = {
                        // TODO
                    },
                    modifier = Modifier.padding(0.dp)
                ) {
                    Text(
                        text = "See All",
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            BriefRecommendedClubs()
        }
    }
}

@Composable
fun BriefRecommendedClubs(modifier: Modifier = Modifier) {
    val club1 = Club(
        "",
        "F1 Astana",
        "Something",
        "lmaooo",
        false,
        Creator("dsa", "", "", 0.4, ""),
        emptyList(),
        LocalDate.now(),
        clubTags = "Something",
        category = "Something"
    )
    val listOfClubs = listOf(club1, club1, club1, club1, club1, club1, club1, club1, club1)

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(listOfClubs) { club ->
            BriefRecommendedClubCard(
                club = club
            )
        }
    }
}

@Composable
fun BriefRecommendedClubCard(
    club: Club,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.google_logo),
                contentDescription = "Testing Logo",
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = club.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = club.description,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
            Button(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(7.dp)
            ) {
                Text(
                    text = "Join",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}