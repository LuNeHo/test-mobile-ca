package com.luneho.testmobileca.presentation.accounts

import androidx.lifecycle.viewModelScope
import com.luneho.testmobileca.domain.usecase.GetSortedBanksUseCase
import com.luneho.testmobileca.presentation.BaseViewModel
import kotlinx.coroutines.launch

class AccountsViewModel(private val getSortedBanks: GetSortedBanksUseCase) :
    BaseViewModel<AccountsState, AccountsIntent, AccountsEffect>(AccountsState()) {

    init {
        handleIntent(AccountsIntent.LoadBanks)
    }

    override fun handleIntent(event: AccountsIntent) {
        when (event) {
            AccountsIntent.LoadBanks -> loadBanks()
        }
    }
    private fun loadBanks() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            getSortedBanks()
                .onSuccess { sorted ->
                    updateState {
                        copy(
                            isLoading = false,
                            caBanks = sorted.mainBankAccounts,
                            otherBanks = sorted.otherBankAccounts
                        )
                    }
                }
                .onFailure { e ->
                    updateState { copy(isLoading = false, error = e.message) }
                }
        }
    }
}