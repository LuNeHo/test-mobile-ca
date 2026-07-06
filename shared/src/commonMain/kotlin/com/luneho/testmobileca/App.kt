package com.luneho.testmobileca

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.luneho.testmobileca.presentation.accounts.AccountsScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        AccountsScreen()
    }
}