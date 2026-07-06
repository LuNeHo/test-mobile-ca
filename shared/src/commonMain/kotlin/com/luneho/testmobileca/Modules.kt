package com.luneho.testmobileca

import com.luneho.testmobileca.data.BankApiService
import com.luneho.testmobileca.domain.BankRepository
import com.luneho.testmobileca.domain.BankRepositoryImpl
import com.luneho.testmobileca.domain.GetSortedBanksUseCase
import com.luneho.testmobileca.presentation.accounts.AccountsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private const val BASE_URL =
    "https://cdf-test-mobile-default-rtdb.europe-west1.firebasedatabase.app/"

val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(Logging) {
                level = LogLevel.BODY
            }
            defaultRequest {
                url(BASE_URL)
            }
        }

    }
    single<BankApiService> {
        BankApiService(get())
    }
}

val repositoryModule = module {
    single<BankRepository> { BankRepositoryImpl(get()) }
}

val useCaseModule = module {
    factory { GetSortedBanksUseCase(get()) }
}

val viewModelModule = module {
    viewModel {
        val useCase = get<GetSortedBanksUseCase>()
        AccountsViewModel(useCase)
    }
}
val appModules = listOf(
    networkModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)