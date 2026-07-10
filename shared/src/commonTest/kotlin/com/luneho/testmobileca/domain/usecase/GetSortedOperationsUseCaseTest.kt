package com.luneho.testmobileca.domain.usecase

import com.luneho.testmobileca.domain.model.Operation
import kotlin.test.Test
import kotlin.test.assertEquals

class GetSortedOperationsUseCaseTest {

    private val useCase = GetSortedOperationsUseCase()

    @Test
    fun `invoke should sort operations by date descending then by title`() {
        // Given
        val operations = listOf(
            Operation("1", "B Op", 10.0, 1000L),
            Operation("2", "A Op", 20.0, 1000L),
            Operation("3", "C Op", 30.0, 2000L),
            Operation("4", "D Op", 40.0, 500L)
        )

        // When
        val result = useCase(operations)

        // Then
        // Should be:
        // 1. C Op (2000L)
        // 2. A Op (1000L, starts with A)
        // 3. B Op (1000L, starts with B)
        // 4. D Op (500L)

        assertEquals(4, result.size)
        assertEquals("3", result[0].id)
        assertEquals("2", result[1].id)
        assertEquals("1", result[2].id)
        assertEquals("4", result[3].id)
    }

    @Test
    fun `invoke should handle empty list`() {
        val result = useCase(emptyList())
        assertEquals(0, result.size)
    }
}
