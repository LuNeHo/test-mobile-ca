package com.luneho.testmobileca.di

import com.luneho.testmobileca.data.createBankApiService
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
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
        }

    }
    single {
        Ktorfit.Builder()
            .httpClient(get<HttpClient>())
            .baseUrl(BASE_URL)
            .build()
            .createBankApiService()
    }
}