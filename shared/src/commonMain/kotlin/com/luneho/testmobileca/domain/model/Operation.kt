package com.luneho.testmobileca.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Operation(
    val id: String,
    val title: String,
    val amount: Double,
    val date: Long
)