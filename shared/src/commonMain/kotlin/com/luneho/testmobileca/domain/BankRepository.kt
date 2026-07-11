package com.luneho.testmobileca.domain

import com.luneho.testmobileca.domain.model.Bank

interface BankRepository {
    suspend fun getBanks(): Result<List<Bank>>
}