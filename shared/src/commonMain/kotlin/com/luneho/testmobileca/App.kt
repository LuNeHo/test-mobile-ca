package com.luneho.testmobileca

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.luneho.testmobileca.domain.model.Operation
import com.luneho.testmobileca.presentation.accounts.AccountsScreen
import com.luneho.testmobileca.presentation.operations.OperationsScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

@Composable
fun App() {
    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }

    MaterialTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Star, contentDescription = null) },
                        label = { Text("Mes Comptes") },
                        selected = selectedItem == 0,
                        onClick = { selectedItem = 0 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Star, contentDescription = null) },
                        label = { Text("Simulation") },
                        selected = selectedItem == 1,
                        onClick = { selectedItem = 1 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Star, contentDescription = null) },
                        label = { Text("À vous de jouer") },
                        selected = selectedItem == 2,
                        onClick = { selectedItem = 2 }
                    )
                }
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Accounts,
                modifier = Modifier.padding(padding)
            ) {
                composable<Screen.Accounts> {
                    AccountsScreen(onNavigateToOperations = { account ->
                        navController.navigate(
                            Screen.Operations(
                                account.label,
                                account.balance,
                                json.encodeToString(account.operations)
                            )
                        )
                    })
                }
                composable<Screen.Operations> { backStackEntry ->
                    val screen: Screen.Operations = backStackEntry.toRoute()
                    val operations = json.decodeFromString<List<Operation>>(screen.operationsJson)

                    OperationsScreen(
                        accountLabel = screen.accountLabel,
                        accountBalance = screen.accountBalance,
                        operations = operations,
                    ) { navController.popBackStack() }
                }
            }
        }
    }
}


@Serializable
sealed class Screen {

    @Serializable
    data object Accounts : Screen()

    @Serializable
    data class Operations(
        val accountLabel: String,
        val accountBalance: Double,
        val operationsJson: String
    ) : Screen()
}