package com.luneho.testmobileca.data

import de.jensklingenberg.ktorfit.http.GET

interface BankApiService {
    @GET("banks.json")
    suspend fun getBanks(): List<BankDto>
}