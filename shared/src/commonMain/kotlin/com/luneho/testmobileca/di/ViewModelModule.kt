package com.luneho.testmobileca.di

import com.luneho.testmobileca.domain.GetSortedBanksUseCase
import com.luneho.testmobileca.presentation.accounts.AccountsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        val useCase = get<GetSortedBanksUseCase>()
        AccountsViewModel(useCase)
    }
}
