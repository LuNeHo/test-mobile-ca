package com.luneho.testmobileca.presentation.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.luneho.testmobileca.domain.model.Account
import com.luneho.testmobileca.domain.model.Bank
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import testmobileca.shared.generated.resources.Res
import testmobileca.shared.generated.resources.main_bank_title
import testmobileca.shared.generated.resources.other_bank_title
import testmobileca.shared.generated.resources.screen_title

@Composable
fun AccountsScreen(viewModel: AccountsViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AccountsContent(state)
}

@Composable
private fun AccountsContent(state: AccountsState) {
    Scaffold(containerColor = Color.LightGray) { padding ->
        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }

            state.error != null -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text(state.error)
            }

            else -> LazyColumn(contentPadding = padding) {
                item {
                    Text(
                        text = stringResource(Res.string.screen_title),
                        modifier = Modifier.padding(start = 16.dp, top = 42.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                item { SectionHeader(title = stringResource(Res.string.main_bank_title)) }
                state.caBanks.forEach { bank ->
                    item(key = bank.name) {
                        BankRow(bank.name, bank.accounts.first().balance)
                        if (state.caBanks.last() != bank) HorizontalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
                item { SectionHeader(title = stringResource(Res.string.other_bank_title)) }
                state.otherBanks.forEach { bank ->
                    item(key = bank.name) {
                        BankRow(bank.name, bank.accounts.first().balance)
                        if (state.caBanks.last() != bank) HorizontalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = Color.Gray, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun BankRow(bankName: String, accountBalance: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 32.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(bankName, style = MaterialTheme.typography.titleMedium)
        Text(
            "$accountBalance €",
            color = Color.Gray,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview
@Composable
fun AccountsScreenPreview() {
    MaterialTheme {
        AccountsContent(
            AccountsState(
                caBanks = listOf(
                    Bank("CA", true, listOf(Account("", "", 0.0, emptyList())))
                ),
                otherBanks = listOf(
                    Bank("Other", false, listOf(Account("", "", 0.0, emptyList())))
                )
            )
        )
    }
}