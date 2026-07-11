package com.luneho.testmobileca.presentation.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.luneho.testmobileca.presentation.utils.formatAsMoney
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import testmobileca.shared.generated.resources.Res
import testmobileca.shared.generated.resources.main_bank_title
import testmobileca.shared.generated.resources.other_bank_title
import testmobileca.shared.generated.resources.screen_title

@Composable
fun AccountsScreen(
    viewModel: AccountsViewModel = koinViewModel(),
    onNavigateToOperations: (Account) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is AccountsEffect.NavigateToOperations -> onNavigateToOperations(effect.account)
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(Res.string.screen_title)) }) },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { padding ->
        AccountsContent(padding, state, viewModel::handleIntent)
    }
}

@Composable
private fun AccountsContent(
    padding: PaddingValues,
    state: AccountsState,
    doOnIntent: (AccountsIntent) -> Unit
) {
    when {
        state.isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }

        state.error != null -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text(state.error)
        }

        else -> LazyColumn(contentPadding = padding) {
            item(key = "header_ca") { SectionHeader(title = stringResource(Res.string.main_bank_title)) }
            itemsIndexed(
                items = state.caBanks,
                key = { index, bank -> "ca_${bank.name}_$index" }) { _, bank ->
                BankRow(bank, state.caBanks.lastOrNull() != bank, doOnIntent)
            }
            item(key = "header_other") { SectionHeader(title = stringResource(Res.string.other_bank_title)) }
            itemsIndexed(
                items = state.otherBanks,
                key = { index, bank -> "other_${bank.name}_$index" }) { _, bank ->
                BankRow(bank, state.otherBanks.lastOrNull() != bank, doOnIntent)
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
        Text(
            text = title,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun BankRow(bank: Bank, hasDivider: Boolean, doOnIntent: (AccountsIntent) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }
    val totalBalance = bank.accounts.sumOf { it.balance }

    Column(modifier = Modifier.background(Color.White)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(start = 32.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(bank.name, style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = totalBalance.formatAsMoney(),
                    color = MaterialTheme.colorScheme.secondaryFixedDim,
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondaryFixedDim
                )
            }
        }
        if (hasDivider || isExpanded) HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        if (isExpanded) bank.accounts.forEachIndexed { index, account ->
            AccountRow(account, index != bank.accounts.lastIndex, doOnIntent)
        }
    }
}

@Composable
private fun AccountRow(
    account: Account,
    hasDivider: Boolean,
    doOnIntent: (AccountsIntent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { doOnIntent(AccountsIntent.SelectAccount(account)) }
            .padding(start = 48.dp, end = 32.dp, top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(account.label, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = account.balance.formatAsMoney(),
            color = MaterialTheme.colorScheme.secondaryFixedDim,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )
    }
    if (hasDivider) HorizontalDivider(
        modifier = Modifier.padding(start = 48.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant
    )
}

@Preview
@Composable
fun AccountsScreenPreview() {
    val accounts = Account("", "", 0.0, emptyList())
    val caBanks = listOf(Bank(name = "CA", isCA = true, accounts = listOf(accounts)))
    val otherBanks = listOf(Bank(name = "CA", isCA = false, accounts = listOf(accounts)))
    MaterialTheme {
        AccountsContent(
            PaddingValues(),
            AccountsState(caBanks = caBanks, otherBanks = otherBanks)
        ) {}
    }
}