package com.luneho.testmobileca.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class BankApiService(private val client: HttpClient) {

    suspend fun getBanks(): List<BankDto> =
        client.get("banks.json").body()
}