package com.luneho.testmobileca.presentation.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlin.math.round

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
                    item(key = bank.name) { BankRow(bank, state.caBanks.last() != bank) }
                }
                item { SectionHeader(title = stringResource(Res.string.other_bank_title)) }
                state.otherBanks.forEach { bank ->
                    item(key = bank.name) { BankRow(bank, state.otherBanks.last() != bank) }
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
private fun BankRow(bank: Bank, hasDivider: Boolean) {
    var isExpanded by remember { mutableStateOf(false) }
    val totalBalance = bank.accounts.sumOf { it.balance }

    Column(modifier = Modifier.background(Color.White)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(bank.name, style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${formatBalance(totalBalance)} €",
                    color = Color.Gray,
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    modifier = Modifier.clickable { isExpanded = !isExpanded },
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
        if (hasDivider || isExpanded) HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        if (isExpanded) bank.accounts.forEach { account ->
            AccountRow(account, bank.accounts.last() != account)
        }
    }
}

@Composable
private fun AccountRow(account: Account, hasDivider: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 48.dp, end = 32.dp, top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(account.label, style = MaterialTheme.typography.bodyMedium)
        Text(
            "${formatBalance(account.balance)} €",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
    if (hasDivider) HorizontalDivider(
        modifier = Modifier.padding(start = 48.dp),
        thickness = 2.dp,
        color = MaterialTheme.colorScheme.outlineVariant
    )
}

private fun formatBalance(balance: Double): String = (round(balance * 100) / 100.0).toString().let {
    val dotIndex = it.indexOf('.')
    if (dotIndex == -1) "$it.00"
    else it.padEnd(dotIndex + 3, '0')
}

@Preview
@Composable
fun AccountsScreenPreview() {
    val accounts = Account("", "", 0.0, emptyList())
    val caBanks = listOf(Bank("CA", true, listOf(accounts)))
    val otherBanks = listOf(Bank("CA", false, listOf(accounts)))
    MaterialTheme {
        AccountsContent(AccountsState(caBanks = caBanks, otherBanks = otherBanks))
    }
}