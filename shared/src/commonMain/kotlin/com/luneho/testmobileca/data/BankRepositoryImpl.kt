package com.luneho.testmobileca.data

import com.luneho.testmobileca.domain.BankRepository
import com.luneho.testmobileca.domain.model.Bank

class BankRepositoryImpl(private val api: BankApiService) : BankRepository {
    override suspend fun getBanks(): Result<List<Bank>> =
        runCatching { api.getBanks().map { it.toDomain() } }
}