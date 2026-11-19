package io.github.maignardi.maigui.forms

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MaigValidatorsTest {

    @Test
    fun `required validator rejects blank strings and accepts text`() {
        val validator = MaigValidators.required("Obrigatório")
        assertEquals("Obrigatório", validator.validate("", emptyMap()))
        assertEquals("Obrigatório", validator.validate("   ", emptyMap()))
        assertNull(validator.validate("ok", emptyMap()))
    }

    @Test
    fun `email validator validates standard emails`() {
        val validator = MaigValidators.email()
        assertNull(validator.validate("", emptyMap()))
        assertNull(validator.validate("dev@maigui.io", emptyMap()))
        assertNotNull(validator.validate("invalido", emptyMap()))
        assertNotNull(validator.validate("a@b", emptyMap()))
    }

    @Test
    fun `minLength maxLength numeric pattern matches and custom rules work`() {
        assertNotNull(MaigValidators.minLength(5).validate("abc", emptyMap()))
        assertNull(MaigValidators.minLength(5).validate("abcde", emptyMap()))

        assertNotNull(MaigValidators.maxLength(3).validate("abcd", emptyMap()))
        assertNull(MaigValidators.maxLength(3).validate("abc", emptyMap()))

        assertNotNull(MaigValidators.numeric().validate("12a", emptyMap()))
        assertNull(MaigValidators.numeric().validate("12345", emptyMap()))

        assertNotNull(MaigValidators.pattern(Regex("^[A-Z]+$")).validate("abc", emptyMap()))
        assertNull(MaigValidators.pattern(Regex("^[A-Z]+$")).validate("ABC", emptyMap()))

        val match = MaigValidators.matches("senha")
        assertNotNull(match.validate("123", mapOf("senha" to "456")))
        assertNull(match.validate("456", mapOf("senha" to "456")))

        val even = MaigValidators.custom("Par") { (it.toIntOrNull() ?: 1) % 2 == 0 }
        assertNotNull(even.validate("3", emptyMap()))
        assertNull(even.validate("4", emptyMap()))
    }

    @Test
    fun `cpf validator validates checksum digits and rejects repeated digits`() {
        assertTrue(MaigValidators.isValidCpf("52998224725"))
        assertTrue(MaigValidators.isValidCpf("529.982.247-25"))
        assertFalse(MaigValidators.isValidCpf("52998224726"))
        assertFalse(MaigValidators.isValidCpf("11111111111"))
        assertFalse(MaigValidators.isValidCpf("1234567890"))
    }

    @Test
    fun `cnpj validator validates checksum digits and rejects repeated digits`() {
        assertTrue(MaigValidators.isValidCnpj("11222333000181"))
        assertTrue(MaigValidators.isValidCnpj("11.222.333/0001-81"))
        assertFalse(MaigValidators.isValidCnpj("11222333000182"))
        assertFalse(MaigValidators.isValidCnpj("00000000000000"))
    }

    @Test
    fun `cep and brazilian phone validators validate length and DDD`() {
        assertTrue(MaigValidators.isValidCep("01310100"))
        assertTrue(MaigValidators.isValidCep("01310-100"))
        assertFalse(MaigValidators.isValidCep("0131010"))

        assertTrue(MaigValidators.isValidBrazilianPhone("11987654321"))
        assertTrue(MaigValidators.isValidBrazilianPhone("1133334444"))
        assertFalse(MaigValidators.isValidBrazilianPhone("11887654321")) // 11 digits without 9 prefix
        assertFalse(MaigValidators.isValidBrazilianPhone("00987654321")) // invalid DDD
    }
}
