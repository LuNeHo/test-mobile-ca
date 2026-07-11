package com.luneho.testmobileca.domain.usecase

import com.luneho.testmobileca.domain.BankRepository
import com.luneho.testmobileca.domain.model.Bank
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetSortedBanksUseCaseTest {

    private val fakeRepository = FakeBankRepository()
    private val useCase = GetSortedBanksUseCase(fakeRepository)

    @Test
    fun `invoke should return banks sorted and separated by CA status`() = runTest {
        // Given
        val banks = listOf(
            Bank("Caisse d'épargne", isCA = false, accounts = emptyList()),
            Bank("Crédit Agricole Languedoc", isCA = true, accounts = emptyList()),
            Bank("Banque Populaire", isCA = false, accounts = emptyList()),
            Bank("Crédit Agricole Bordeaux", isCA = true, accounts = emptyList())
        )
        fakeRepository.setBanks(banks)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        val sortedBanks = result.getOrThrow()

        // Check CA Banks sorted alphabetically
        assertEquals(2, sortedBanks.mainBankAccounts.size)
        assertEquals("Crédit Agricole Bordeaux", sortedBanks.mainBankAccounts[0].name)
        assertEquals("Crédit Agricole Languedoc", sortedBanks.mainBankAccounts[1].name)

        // Check Other Banks sorted alphabetically
        assertEquals(2, sortedBanks.otherBankAccounts.size)
        assertEquals("Banque Populaire", sortedBanks.otherBankAccounts[0].name)
        assertEquals("Caisse d'épargne", sortedBanks.otherBankAccounts[1].name)
    }

    @Test
    fun `invoke should handle empty repository`() = runTest {
        // Given
        fakeRepository.setBanks(emptyList())

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        val sortedBanks = result.getOrThrow()
        assertTrue(sortedBanks.mainBankAccounts.isEmpty())
        assertTrue(sortedBanks.otherBankAccounts.isEmpty())
    }

    @Test
    fun `invoke should handle repository error`() = runTest {
        // Given
        fakeRepository.setError(Exception("Network error"))

        // When
        val result = useCase()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}

class FakeBankRepository : BankRepository {
    private var banks: List<Bank> = emptyList()
    private var error: Throwable? = null

    fun setBanks(banks: List<Bank>) {
        this.banks = banks
        this.error = null
    }

    fun setError(error: Throwable) {
        this.error = error
    }

    override suspend fun getBanks(): Result<List<Bank>> {
        return error?.let { Result.failure(it) } ?: Result.success(banks)
    }
}
