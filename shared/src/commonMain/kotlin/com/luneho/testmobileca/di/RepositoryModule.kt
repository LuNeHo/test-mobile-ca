package com.luneho.testmobileca.di

import com.luneho.testmobileca.data.BankRepositoryImpl
import com.luneho.testmobileca.domain.BankRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<BankRepository> { BankRepositoryImpl(get()) }
}