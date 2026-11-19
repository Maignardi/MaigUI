package io.github.maignardi.maigui.forms

import androidx.compose.ui.text.AnnotatedString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MaigMasksTest {

    @Test
    fun `all Brazilian formatting helpers produce expected masks`() {
        assertEquals("529.982.247-25", MaigMasks.formatCpf("52998224725"))
        assertEquals("11.222.333/0001-81", MaigMasks.formatCnpj("11222333000181"))
        assertEquals("01310-100", MaigMasks.formatCep("01310100"))
        assertEquals("(11) 98765-4321", MaigMasks.formatPhone("11987654321"))
        assertEquals("(11) 3333-4444", MaigMasks.formatPhone("1133334444"))
        assertEquals("19/11/2026", MaigMasks.formatDate("19112026"))
        assertEquals("4111 1111 1111 1111", MaigMasks.formatCreditCard("4111111111111111"))
        assertEquals("R$ 1.234,56", MaigMasks.formatCurrencyBrl("123456"))
        assertEquals("R$ 0,00", MaigMasks.formatCurrencyBrl(""))
    }

    @Test
    fun `visual transformations map offsets safely across partial and full inputs`() {
        val transformations = listOf(
            MaigMasks.cpf() to "52998224725",
            MaigMasks.cnpj() to "11222333000181",
            MaigMasks.cep() to "01310100",
            MaigMasks.phone() to "11987654321",
            MaigMasks.date() to "19112026",
            MaigMasks.creditCard() to "4111111111111111",
            MaigMasks.currencyBrl() to "12345",
        )

        transformations.forEach { (transformation, input) ->
            for (len in 0..input.length) {
                val slice = input.take(len)
                val result = transformation.filter(AnnotatedString(slice))
                for (origOffset in 0..slice.length) {
                    val mapped = result.offsetMapping.originalToTransformed(origOffset)
                    assertTrue(mapped in 0..result.text.length)
                }
                for (transOffset in 0..result.text.length) {
                    val back = result.offsetMapping.transformedToOriginal(transOffset)
                    assertTrue(back in 0..slice.length)
                }
            }
        }
    }
}
