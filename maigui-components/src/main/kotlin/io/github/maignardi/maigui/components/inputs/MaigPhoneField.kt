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
 * Defaults and mask formatting for [MaigPhoneField].
 */
public object MaigPhoneFieldDefaults {
    /** Maximum digits allowed for a Brazilian landline/mobile phone number. */
    public const val MaxDigits: Int = 11

    /** Formats up to 11 digits into `(XX) XXXXX-XXXX` or `(XX) XXXX-XXXX`. */
    public fun formatBrazilianPhone(digits: String): String {
        val clean = digits.filter { it.isDigit() }.take(MaxDigits)
        if (clean.isEmpty()) return ""
        val isMobile = clean.length > 10
        val sb = StringBuilder()
        clean.forEachIndexed { index, c ->
            when (index) {
                0 -> sb.append('(').append(c)
                2 -> sb.append(") ").append(c)
                6 -> if (!isMobile) sb.append('-').append(c) else sb.append(c)
                7 -> if (isMobile) sb.append('-').append(c) else sb.append(c)
                else -> sb.append(c)
            }
        }
        return sb.toString()
    }
}

private class BrazilianPhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text.filter { it.isDigit() }.take(MaigPhoneFieldDefaults.MaxDigits)
        val formatted = MaigPhoneFieldDefaults.formatBrazilianPhone(raw)
        val isMobile = raw.length > 10

        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val clamped = offset.coerceIn(0, raw.length)
                if (clamped == 0) return 0
                var extra = 1 // '('
                if (clamped > 2) extra += 2 // ") "
                val dashThreshold = if (isMobile) 7 else 6
                if (clamped > dashThreshold) extra += 1 // '-'
                return (clamped + extra).coerceAtMost(formatted.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                val clamped = offset.coerceIn(0, formatted.length)
                var digits = 0
                for (i in 0 until clamped) {
                    if (formatted[i].isDigit()) digits++
                }
                return digits.coerceAtMost(raw.length)
            }
        }
        return TransformedText(AnnotatedString(formatted), mapping)
    }
}

/**
 * Brazilian phone number input field applying `(XX) XXXXX-XXXX` / `(XX) XXXX-XXXX` masking.
 */
@Composable
public fun MaigPhoneField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = "Telefone",
    placeholder: String? = "(11) 99999-9999",
    helperText: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    leadingIcon: @Composable (() -> Unit)? = null,
    variant: MaigTextFieldVariant = MaigTextFieldVariant.Outlined,
    colors: MaigTextFieldColors = MaigTextFieldDefaults.colors(variant = variant),
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource? = null,
) {
    val transformation = remember { BrazilianPhoneVisualTransformation() }

    MaigTextField(
        value = value.filter { it.isDigit() }.take(MaigPhoneFieldDefaults.MaxDigits),
        onValueChange = { next ->
            onValueChange(next.filter { it.isDigit() }.take(MaigPhoneFieldDefaults.MaxDigits))
        },
        modifier = modifier,
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        helperText = helperText,
        errorText = errorText,
        isError = isError,
        leadingIcon = leadingIcon,
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
private fun MaigPhoneFieldLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigPhoneField(value = "11987654321", onValueChange = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigPhoneFieldDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigPhoneField(value = "1133334444", onValueChange = {})
        }
    }
}
