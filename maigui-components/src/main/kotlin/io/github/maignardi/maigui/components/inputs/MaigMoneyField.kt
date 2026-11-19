package io.github.maignardi.maigui.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Defaults and formatting helpers for [MaigMoneyField].
 */
public object MaigMoneyFieldDefaults {
    /** Currency prefix displayed in BRL money fields. */
    public const val CurrencyPrefix: String = "R$"

    /** Formats raw cent digits (e.g. `"12345"`) into Brazilian Real notation (`"123,45"`). */
    public fun formatBrlAmount(digits: String): String {
        val clean = digits.filter { it.isDigit() }.trimStart('0')
        if (clean.isEmpty()) return "0,00"
        val padded = clean.padStart(3, '0')
        val cents = padded.takeLast(2)
        val integerPart = padded.dropLast(2)
        val grouped = integerPart.reversed().chunked(3).joinToString(".").reversed()
        return "$grouped,$cents"
    }
}

private class BrlMoneyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatted = MaigMoneyFieldDefaults.formatBrlAmount(text.text)
        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = formatted.length
            override fun transformedToOriginal(offset: Int): Int = text.text.length
        }
        return TransformedText(AnnotatedString(formatted), mapping)
    }
}

/**
 * Brazilian Real (`R$`) currency input field storing raw cent digits (e.g. `"1590"` = R$ 15,90).
 */
@Composable
public fun MaigMoneyField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = "Valor",
    placeholder: String? = "0,00",
    helperText: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    maxDigits: Int = 12,
    variant: MaigTextFieldVariant = MaigTextFieldVariant.Outlined,
    colors: MaigTextFieldColors = MaigTextFieldDefaults.colors(variant = variant),
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource? = null,
) {
    val transformation = remember { BrlMoneyVisualTransformation() }

    MaigTextField(
        value = value.filter { it.isDigit() }.take(maxDigits),
        onValueChange = { next ->
            val clean = next.filter { it.isDigit() }.trimStart('0').take(maxDigits)
            onValueChange(clean)
        },
        modifier = modifier,
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        helperText = helperText,
        errorText = errorText,
        isError = isError,
        prefix = MaigMoneyFieldDefaults.CurrencyPrefix,
        visualTransformation = transformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        variant = variant,
        colors = colors,
        interactionSource = interactionSource,
    )
}

@Preview(showBackground = true)
@Composable
private fun MaigMoneyFieldLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigMoneyField(value = "125990", onValueChange = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigMoneyFieldDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigMoneyField(value = "4990", onValueChange = {}, helperText = "Taxa inclusa")
        }
    }
}
