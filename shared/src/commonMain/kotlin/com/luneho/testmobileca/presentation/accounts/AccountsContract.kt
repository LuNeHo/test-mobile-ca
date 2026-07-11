package com.luneho.testmobileca.presentation.accounts

import com.luneho.testmobileca.domain.model.Account
import com.luneho.testmobileca.domain.model.Bank
import com.luneho.testmobileca.presentation.UiEffect
import com.luneho.testmobileca.presentation.UiIntent
import com.luneho.testmobileca.presentation.UiState

data class AccountsState(
    val isLoading: Boolean = false,
    val caBanks: List<Bank> = emptyList(),
    val otherBanks: List<Bank> = emptyList(),
    val error: String? = null
) : UiState

sealed interface AccountsIntent : UiIntent {
    data object LoadBanks : AccountsIntent
    data class SelectAccount(val account: Account) : AccountsIntent
}

sealed interface AccountsEffect : UiEffect {
    data class NavigateToOperations(val account: Account) : AccountsEffect
}