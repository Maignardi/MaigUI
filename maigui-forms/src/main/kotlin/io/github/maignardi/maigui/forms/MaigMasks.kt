package io.github.maignardi.maigui.forms

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Ready-to-use Compose [VisualTransformation] masks and formatting helpers for Brazilian
 * documents, postal codes, phones, dates, credit cards, and BRL currency.
 */
public object MaigMasks {

    /** Formats digits as CPF (`000.000.000-00`). */
    public fun cpf(): VisualTransformation = PatternMaskTransformation(
        pattern = "###.###.###-##",
    )

    /** Formats digits as CNPJ (`00.000.000/0000-00`). */
    public fun cnpj(): VisualTransformation = PatternMaskTransformation(
        pattern = "##.###.###/####-##",
    )

    /** Formats digits as CEP (`00000-000`). */
    public fun cep(): VisualTransformation = PatternMaskTransformation(
        pattern = "#####-###",
    )

    /** Formats 10 or 11 digits as Brazilian phone (`(00) 0000-0000` or `(00) 00000-0000`). */
    public fun phone(): VisualTransformation = PhoneMaskTransformation

    /** Formats 8 digits as Brazilian date (`00/00/0000`). */
    public fun date(): VisualTransformation = PatternMaskTransformation(
        pattern = "##/##/####",
    )

    /** Formats 16 digits as credit card groups (`0000 0000 0000 0000`). */
    public fun creditCard(): VisualTransformation = PatternMaskTransformation(
        pattern = "#### #### #### ####",
    )

    /** Formats raw cent digits as Brazilian Real (`R$ 1.234,56`). */
    public fun currencyBrl(): VisualTransformation = CurrencyBrlMaskTransformation

    /** Formats a raw CPF string into `000.000.000-00`. */
    public fun formatCpf(raw: String): String = applyPattern(raw.filter { it.isDigit() }.take(11), "###.###.###-##")

    /** Formats a raw CNPJ string into `00.000.000/0000-00`. */
    public fun formatCnpj(raw: String): String = applyPattern(raw.filter { it.isDigit() }.take(14), "##.###.###/####-##")

    /** Formats a raw CEP string into `00000-000`. */
    public fun formatCep(raw: String): String = applyPattern(raw.filter { it.isDigit() }.take(8), "#####-###")

    /** Formats a raw date string into `00/00/0000`. */
    public fun formatDate(raw: String): String = applyPattern(raw.filter { it.isDigit() }.take(8), "##/##/####")

    /** Formats a raw card number string into `0000 0000 0000 0000`. */
    public fun formatCreditCard(raw: String): String = applyPattern(raw.filter { it.isDigit() }.take(16), "#### #### #### ####")

    /** Formats a raw Brazilian phone string into `(00) 00000-0000` or `(00) 0000-0000`. */
    public fun formatPhone(raw: String): String {
        val digits = raw.filter { it.isDigit() }.take(11)
        val pattern = if (digits.length > 10) "(##) #####-####" else "(##) ####-####"
        return applyPattern(digits, pattern)
    }

    /** Formats raw cent digits (e.g. `"123456"`) into `"R$ 1.234,56"`. */
    public fun formatCurrencyBrl(rawDigits: String): String {
        val clean = rawDigits.filter { it.isDigit() }.trimStart('0')
        if (clean.isEmpty()) return "R$ 0,00"
        val padded = clean.padStart(3, '0')
        val cents = padded.takeLast(2)
        val intPart = padded.dropLast(2)
        val grouped = intPart.reversed().chunked(3).joinToString(".").reversed()
        return "R$ $grouped,$cents"
    }

    internal fun applyPattern(digits: String, pattern: String): String {
        if (digits.isEmpty()) return ""
        val sb = StringBuilder()
        var digitIdx = 0
        for (ch in pattern) {
            if (digitIdx >= digits.length) break
            if (ch == '#') {
                sb.append(digits[digitIdx++])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }
}

private class PatternMaskTransformation(
    private val pattern: String,
) : VisualTransformation {
    private val maxDigits = pattern.count { it == '#' }

    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }.take(maxDigits)
        val transformed = MaigMasks.applyPattern(digits, pattern)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val targetDigitCount = text.text
                    .take(offset.coerceIn(0, text.text.length))
                    .count { it.isDigit() }
                    .coerceAtMost(digits.length)
                if (targetDigitCount == 0) return 0
                var seen = 0
                for (i in transformed.indices) {
                    if (patternDigitPositions(transformed, i)) {
                        seen++
                        if (seen == targetDigitCount) {
                            return i + 1
                        }
                    }
                }
                return transformed.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                val clamped = offset.coerceIn(0, transformed.length)
                var digitCount = 0
                for (i in 0 until clamped) {
                    if (patternDigitPositions(transformed, i)) {
                        digitCount++
                    }
                }
                var origDigits = 0
                for (i in text.text.indices) {
                    if (origDigits == digitCount) return i
                    if (text.text[i].isDigit()) origDigits++
                }
                return text.text.length
            }

            private fun patternDigitPositions(str: String, index: Int): Boolean {
                var patIndex = 0
                var strIndex = 0
                while (patIndex < pattern.length && strIndex <= index) {
                    if (strIndex == index) {
                        return pattern[patIndex] == '#'
                    }
                    patIndex++
                    strIndex++
                }
                return false
            }
        }
        return TransformedText(AnnotatedString(transformed), offsetMapping)
    }
}

private object PhoneMaskTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }.take(11)
        val pattern = if (digits.length > 10) "(##) #####-####" else "(##) ####-####"
        return PatternMaskTransformation(pattern).filter(AnnotatedString(digits))
    }
}

private object CurrencyBrlMaskTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatted = MaigMasks.formatCurrencyBrl(text.text)
        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = formatted.length
            override fun transformedToOriginal(offset: Int): Int = text.text.length
        }
        return TransformedText(AnnotatedString(formatted), mapping)
    }
}
