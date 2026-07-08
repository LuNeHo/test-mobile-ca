package com.luneho.testmobileca.presentation.operations

import com.luneho.testmobileca.domain.model.Operation
import com.luneho.testmobileca.domain.usecase.GetSortedOperationsUseCase
import com.luneho.testmobileca.presentation.BaseViewModel

class OperationsViewModel(
    accountLabel: String,
    operations: List<Operation>,
    getSortedOperations: GetSortedOperationsUseCase
) : BaseViewModel<OperationsState, OperationsIntent, OperationsEffect>(
    OperationsState(accountLabel, getSortedOperations(operations))
) {
    override fun handleIntent(event: OperationsIntent) {}
}