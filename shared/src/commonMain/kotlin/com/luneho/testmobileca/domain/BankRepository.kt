package com.luneho.testmobileca.domain

import com.luneho.testmobileca.data.BankApiService
import com.luneho.testmobileca.data.toDomain
import com.luneho.testmobileca.domain.model.Bank

interface BankRepository {
    suspend fun getBanks(): Result<List<Bank>>
}

class BankRepositoryImpl(private val api: BankApiService) : BankRepository {
    override suspend fun getBanks(): Result<List<Bank>> =
        runCatching { api.getBanks().map { it.toDomain() } }
}