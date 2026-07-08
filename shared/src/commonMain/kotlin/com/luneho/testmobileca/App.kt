package com.luneho.testmobileca

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.luneho.testmobileca.domain.model.Operation
import com.luneho.testmobileca.domain.usecase.GetSortedOperationsUseCase
import com.luneho.testmobileca.presentation.operations.OperationsScreen
import com.luneho.testmobileca.presentation.operations.OperationsViewModel

@Composable
@Preview
fun App() {
    val accountLabel = "Compte de dépôt"
    val operations = listOf(
        Operation("2", "Prélèvement Netflix", -15.99, 1644870724),
        Operation("4", "CB Amazon", -95.99, 1644870724)
    )

    MaterialTheme {
        OperationsScreen(
            viewModel = OperationsViewModel(
                accountLabel,
                operations,
                GetSortedOperationsUseCase()
            )
        ) {}
    }
//    MaterialTheme { AccountsScreen() }
}