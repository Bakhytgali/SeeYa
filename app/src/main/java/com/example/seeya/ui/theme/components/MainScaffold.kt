package com.example.seeya.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.seeya.ui.theme.bgColor
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    title: String,
    navController: NavController,
    icon: @Composable () -> Unit  = {},
    bottomBarViewModel: BottomBarViewModel,
    authViewModel: AuthViewModel,
    content: @Composable (Modifier) -> Unit,
) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    navigationIcon = {
                        icon()
                    }
                )
            }
        },
        bottomBar = {
            CustomBottomAppBar(
                navController = navController,
                bottomBarViewModel = bottomBarViewModel,
                authViewModel = authViewModel,
                modifier = Modifier.fillMaxWidth()
            )
        },
    ) { paddingValues ->
        content(Modifier.padding(paddingValues))
    }
}



