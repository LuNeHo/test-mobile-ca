package com.luneho.testmobileca.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BankDto(
    @SerialName("name") val name: String,
    @SerialName("isCA") val isCa: Int,
    @SerialName("accounts") val accounts: List<AccountDto>
)

@Serializable
data class AccountDto(
    @SerialName("id") val id: String,
    @SerialName("label") val label: String,
    @SerialName("balance") val balance: Double,
    @SerialName("contract_number") val contractNumber: String,
    @SerialName("holder") val holder: String,
    @SerialName("order") val order: Int,
    @SerialName("product_code") val productCode: String,
    @SerialName("role") val role: Int,
    @SerialName("operations") val operations: List<OperationDto>
)

@Serializable
data class OperationDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("amount") val amount: String,
    @SerialName("date") val date: String,
    @SerialName("category") val category: String
)