package com.example.seeya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.seeya.seeYaView.LoginPage
import com.example.seeya.seeYaView.MainPage
import com.example.seeya.seeYaView.RegistrationPage
import com.example.seeya.ui.theme.SeeYaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeeYaTheme {
                RegistrationPage()
            }
        }
    }
}