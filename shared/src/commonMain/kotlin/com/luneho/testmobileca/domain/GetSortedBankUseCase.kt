package com.luneho.testmobileca.domain

import com.luneho.testmobileca.domain.model.Bank

class GetSortedBanksUseCase(private val repository: BankRepository) {
    suspend operator fun invoke(): Result<SortedBanks> =
        repository.getBanks().map { banks ->
            SortedBanks(
                mainBankAccounts = banks
                    .filter { it.isCA }
                    .sortedBy { it.name.lowercase() },
                otherBankAccounts = banks
                    .filter { !it.isCA }
                    .sortedBy { it.name.lowercase() }
            )
        }
}

data class SortedBanks(
    val mainBankAccounts: List<Bank>,
    val otherBankAccounts: List<Bank>
)