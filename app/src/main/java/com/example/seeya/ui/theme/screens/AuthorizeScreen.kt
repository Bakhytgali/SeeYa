package com.example.seeya.ui.theme.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun AuthorizeScreen(
    modifier: Modifier = Modifier
) {

}

@Composable
fun AuthorizeScreenState(modifier: Modifier = Modifier) {
    val email = remember {
        mutableStateOf("")
    }
}