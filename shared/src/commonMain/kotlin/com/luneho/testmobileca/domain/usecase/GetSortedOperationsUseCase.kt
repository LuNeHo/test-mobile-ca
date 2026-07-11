package com.luneho.testmobileca.domain.usecase

import com.luneho.testmobileca.domain.model.Operation

class GetSortedOperationsUseCase {
    operator fun invoke(operations: List<Operation>): List<Operation> =
        operations.sortedWith(
            compareByDescending<Operation> { it.date }
                .thenBy { it.title.lowercase() }
        )
}