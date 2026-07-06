package com.luneho.testmobileca.data

import com.luneho.testmobileca.domain.model.Account
import com.luneho.testmobileca.domain.model.Bank
import com.luneho.testmobileca.domain.model.Operation


fun BankDto.toDomain() = Bank(
    name = name,
    isCA = isCa == 1,
    accounts = accounts
        .map { it.toDomain() }
        .sortedBy { it.label.lowercase() }
)

fun AccountDto.toDomain() = Account(
    id = id,
    label = label,
    balance = balance,
    operations = operations.map { it.toDomain() }
)

fun OperationDto.toDomain() = Operation(
    id = id,
    title = title,
    amount = amount
        .replace(",", ".")   // Replace: "-15,99" → "-15.99"
        .replace(" ", "")    // guard against any space
        .toDoubleOrNull() ?: 0.0,
    date = date.toLongOrNull() ?: 0L
)
