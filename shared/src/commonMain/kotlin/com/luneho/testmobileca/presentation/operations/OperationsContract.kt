package com.luneho.testmobileca.presentation.operations

import com.luneho.testmobileca.domain.model.Operation
import com.luneho.testmobileca.presentation.UiEffect
import com.luneho.testmobileca.presentation.UiIntent
import com.luneho.testmobileca.presentation.UiState

data class OperationsState(
    val accountLabel: String = "",
    val operations: List<Operation> = emptyList()
) : UiState

sealed interface OperationsIntent : UiIntent

sealed interface OperationsEffect : UiEffect