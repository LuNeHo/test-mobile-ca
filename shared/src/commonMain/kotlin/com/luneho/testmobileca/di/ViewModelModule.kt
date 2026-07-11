package com.luneho.testmobileca.di

import com.luneho.testmobileca.domain.model.Operation
import com.luneho.testmobileca.domain.usecase.GetSortedBanksUseCase
import com.luneho.testmobileca.presentation.accounts.AccountsViewModel
import com.luneho.testmobileca.presentation.operations.OperationsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        val useCase = get<GetSortedBanksUseCase>()
        AccountsViewModel(useCase)
    }
    viewModel { params ->
        val accountLabel = params.get<String>()
        val accountBalance = params.get<Double>()
        val operations = params.get<List<Operation>>()

        OperationsViewModel(
            accountLabel = accountLabel,
            accountBalance = accountBalance,
            operations = operations,
            getSortedOperations = get(),
        )
    }
}
