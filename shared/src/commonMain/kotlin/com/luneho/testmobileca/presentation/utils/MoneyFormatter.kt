package com.luneho.testmobileca.presentation.utils

import kotlin.math.round

fun Double.formatAsMoney(): String {
    val rounded = round(this * 100) / 100.0
    val parts = rounded.toString().split('.')
    val integers = parts[0]
    val decimals = parts.getOrNull(1) ?: "00"
    val paddedDecimals = decimals.padEnd(2, '0').take(2)
    return "$integers,$paddedDecimals €"
}
