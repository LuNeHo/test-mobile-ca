package com.luneho.testmobileca.di

import com.luneho.testmobileca.domain.GetSortedBanksUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetSortedBanksUseCase(get()) }
}