package com.luneho.testmobileca.presentation.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class MoneyFormatterTest {

    @Test
    fun `formatAsMoney should format simple double`() {
        assertEquals("12,00 €", 12.0.formatAsMoney())
    }

    @Test
    fun `formatAsMoney should format double with one decimal`() {
        assertEquals("12,50 €", 12.5.formatAsMoney())
    }

    @Test
    fun `formatAsMoney should format double with two decimals`() {
        assertEquals("12,54 €", 12.54.formatAsMoney())
    }

    @Test
    fun `formatAsMoney should round to two decimals`() {
        assertEquals("12,55 €", 12.546.formatAsMoney())
    }

    @Test
    fun `formatAsMoney should format negative values`() {
        assertEquals("-12,50 €", (-12.5).formatAsMoney())
    }
}
