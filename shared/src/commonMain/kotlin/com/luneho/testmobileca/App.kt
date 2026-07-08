package com.luneho.testmobileca

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.luneho.testmobileca.domain.model.Operation
import com.luneho.testmobileca.domain.usecase.GetSortedOperationsUseCase
import com.luneho.testmobileca.presentation.accounts.AccountsScreen
import com.luneho.testmobileca.presentation.operations.OperationsScreen
import com.luneho.testmobileca.presentation.operations.OperationsViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject

@Composable
fun App() {
    val navController = rememberNavController()
    val json = Json { ignoreUnknownKeys = true }

    MaterialTheme {
        NavHost(navController, Screen.Accounts) {
            composable<Screen.Accounts> {
                AccountsScreen(onNavigateToOperations = { account ->
                    navController.navigate(
                        Screen.Operations(
                            account.label,
                            json.encodeToString(account.operations)
                        )
                    )
                })
            }
            composable<Screen.Operations> { backStackEntry ->
                val screen: Screen.Operations = backStackEntry.toRoute()
                val operations = json.decodeFromString<List<Operation>>(screen.operationsJson)
                val getSortedOperations = koinInject<GetSortedOperationsUseCase>()

                OperationsScreen(
                    viewModel = OperationsViewModel(
                        accountLabel = screen.accountLabel,
                        operations = operations,
                        getSortedOperations = getSortedOperations
                    )
                ) { navController.popBackStack() }
            }
        }
    }
}


@Serializable
sealed class Screen {

    @Serializable
    data object Accounts : Screen()

    @Serializable
    data class Operations(val accountLabel: String, val operationsJson: String) : Screen()
}
