package com.luneho.testmobileca.domain.model

data class Account(
    val id: String,
    val label: String,
    val balance: Double,
    val operations: List<Operation>
)
