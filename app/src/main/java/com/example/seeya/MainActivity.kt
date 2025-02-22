package com.example.seeya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seeya.data.repository.AuthRepository
import com.example.seeya.ui.theme.SeeYaTheme
import com.example.seeya.ui.theme.screens.AuthorizeScreen
import com.example.seeya.ui.theme.screens.LoginScreen
import com.example.seeya.ui.theme.screens.MainScreen
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.auth.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(application, AuthRepository(application))
        )[AuthViewModel::class.java]

        setContent {
            SeeYaTheme {
                AppNavigation(authViewModel)
            }
        }
    }
}


@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("authorize") { AuthorizeScreen(navController, authViewModel) }
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("main") { MainScreen(navController) }
    }
}