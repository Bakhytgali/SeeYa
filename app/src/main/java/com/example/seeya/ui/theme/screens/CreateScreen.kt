package com.example.seeya.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seeya.data.model.CreateOptions
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel

@Composable
fun CreateScreen(
    navController: NavController,
    bottomBarViewModel: BottomBarViewModel,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val onClick: (CreateOptions) -> Unit = {
        if (it.option == "Event") {
            navController.navigate("createEvent")
        } else {
            navController.navigate("createClub")
        }
    }

    MainScaffold(
        title = "Create",
        navController = navController,
        bottomBarViewModel = bottomBarViewModel,
        authViewModel = authViewModel
    ) { mod ->
        Box(
            modifier = mod
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.9f),
                verticalArrangement = Arrangement.spacedBy(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Choose what to create",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )

                Spacer(Modifier.height(30.dp))

                CreateOptions.entries.forEach { option ->
                    CreateOptionsCard(
                        createOptions = option,
                        onClick = {
                            onClick(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CreateOptionsCard(
    createOptions: CreateOptions,
    onClick: (CreateOptions) -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        onClick = { onClick(createOptions) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 15.dp)
                .height(90.dp)
        ) {
            Image(
                painter = painterResource(createOptions.icon),
                contentDescription = "${createOptions.option} icon"
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = createOptions.option,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = createOptions.optionDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                )
            }
        }
    }
}