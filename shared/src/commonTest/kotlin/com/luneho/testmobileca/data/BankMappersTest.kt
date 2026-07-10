package com.luneho.testmobileca.data

import kotlin.test.Test
import kotlin.test.assertEquals

class BankMappersTest {

    @Test
    fun `OperationDto toDomain should map fields correctly`() {
        val dto = OperationDto(
            id = "1",
            title = "Test",
            amount = "-15,99",
            date = "1700000000",
            category = "Cat"
        )
        val domain = dto.toDomain()

        assertEquals("1", domain.id)
        assertEquals("Test", domain.title)
        assertEquals(-15.99, domain.amount)
        assertEquals(1700000000L, domain.date)
    }

    @Test
    fun `OperationDto toDomain should handle invalid amount`() {
        val dto = OperationDto(
            id = "1",
            title = "Test",
            amount = "invalid",
            date = "1700000000",
            category = "Cat"
        )
        val domain = dto.toDomain()

        assertEquals(0.0, domain.amount)
    }

    @Test
    fun `AccountDto toDomain should map fields and sort operations`() {
        val dto = AccountDto(
            id = "acc1",
            label = "Account 1",
            balance = 100.0,
            contractNumber = "123",
            holder = "Me",
            order = 1,
            productCode = "PC",
            role = 1,
            operations = listOf(
                OperationDto("1", "Op 1", "10.0", "1700000000", "Cat"),
                OperationDto("2", "Op 2", "20.0", "1700000001", "Cat")
            )
        )
        val domain = dto.toDomain()

        assertEquals("acc1", domain.id)
        assertEquals("Account 1", domain.label)
        assertEquals(100.0, domain.balance)
        assertEquals(2, domain.operations.size)
        assertEquals("Op 1", domain.operations[0].title)
    }

    @Test
    fun `BankDto toDomain should map fields and sort accounts alphabetically`() {
        val dto = BankDto(
            name = "Bank 1",
            isCa = 1,
            accounts = listOf(
                createAccountDto("z", "Z Account"),
                createAccountDto("a", "A Account")
            )
        )
        val domain = dto.toDomain()

        assertEquals("Bank 1", domain.name)
        assertEquals(true, domain.isCA)
        assertEquals(2, domain.accounts.size)
        assertEquals("A Account", domain.accounts[0].label)
        assertEquals("Z Account", domain.accounts[1].label)
    }

    private fun createAccountDto(id: String, label: String) = AccountDto(
        id = id,
        label = label,
        balance = 0.0,
        contractNumber = "",
        holder = "",
        order = 0,
        productCode = "",
        role = 0,
        operations = emptyList()
    )
}
