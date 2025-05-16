package com.example.seeya.ui.theme.screens

import android.widget.Space
import androidx.compose.animation.Animatable
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.R
import com.example.seeya.data.model.Club
import com.example.seeya.ui.theme.components.CustomTextField
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.viewmodel.BottomBarViewModel
import java.time.LocalDate

@Composable
fun ClubsScreen(
    navController: NavController,
    bottomBarViewModel: BottomBarViewModel,
    modifier: Modifier = Modifier
) {
    MainScaffold(
        title = "Clubs",
        bottomBarViewModel = bottomBarViewModel,
        navController = navController,
        content = { mod -> ClubsScreenContent(modifier = mod) }
    )
}

@Composable
fun ClubsScreenContent(
    modifier: Modifier = Modifier
) {
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
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(20.dp))

            CustomTextField(
                text = "",
                onValueChange = {

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

            FollowingClubsBlock(modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(20.dp))

            RecommendedGroupsBlock(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun FollowingClubsBlock(modifier: Modifier = Modifier) {
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

            BriefListOfClub()
        }
    }
}

@Composable
fun BriefListOfClub(modifier: Modifier = Modifier) {
    val club1 = Club(
        "F1 Astana",
        "Something",
        "lmaooo",
        false,
        "somewhere",
        emptyList(),
        LocalDate.now()
    )
    val listOfClubs = listOf(club1, club1, club1, club1, club1, club1, club1, club1, club1)

    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(30.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        items(listOfClubs) { club ->
            BriefClubCard(
                club = club
            )
        }
    }
}

@Composable
fun BriefClubCard(
    club: Club,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        modifier = Modifier.height(100.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.google_logo),
                contentDescription = "Test Image",
                modifier = Modifier.size(50.dp)
            )
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
fun RecommendedGroupsBlock(modifier: Modifier = Modifier) {
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
        "F1 Astana",
        "Something",
        "lmaooo",
        false,
        "somewhere",
        emptyList(),
        LocalDate.now()
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