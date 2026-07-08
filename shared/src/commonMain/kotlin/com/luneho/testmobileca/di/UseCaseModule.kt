package com.luneho.testmobileca.di

import com.luneho.testmobileca.domain.usecase.GetSortedBanksUseCase
import com.luneho.testmobileca.domain.usecase.GetSortedOperationsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetSortedBanksUseCase(get()) }
    factory { GetSortedOperationsUseCase() }
}