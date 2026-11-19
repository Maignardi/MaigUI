package io.github.maignardi.maigui.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Default configuration for [MaigTextArea].
 */
public object MaigTextAreaDefaults {
    /** Default minimum number of visible lines. */
    public const val MinLines: Int = 4

    /** Default maximum number of visible lines before scrolling. */
    public const val MaxLines: Int = 8

    /** Default minimum height for a text area. */
    public val MinHeight: Dp = 112.dp
}

/**
 * Multiline text input area built on [MaigTextField].
 */
@Composable
public fun MaigTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    helperText: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    maxLength: Int? = null,
    showCounter: Boolean = maxLength != null,
    minLines: Int = MaigTextAreaDefaults.MinLines,
    maxLines: Int = MaigTextAreaDefaults.MaxLines,
    minHeight: Dp = MaigTextAreaDefaults.MinHeight,
    variant: MaigTextFieldVariant = MaigTextFieldVariant.Outlined,
    colors: MaigTextFieldColors = MaigTextFieldDefaults.colors(variant = variant),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource? = null,
) {
    MaigTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        label = label,
        placeholder = placeholder,
        helperText = helperText,
        errorText = errorText,
        isError = isError,
        maxLength = maxLength,
        showCounter = showCounter,
        singleLine = false,
        minLines = minLines,
        maxLines = maxLines,
        minHeight = minHeight,
        variant = variant,
        colors = colors,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
    )
}

@Preview(showBackground = true)
@Composable
private fun MaigTextAreaLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigTextArea(
                value = "Biblioteca de componentes Jetpack Compose pronta para produção.",
                onValueChange = {},
                label = "Descrição",
                maxLength = 200,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigTextAreaDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigTextArea(
                value = "",
                onValueChange = {},
                label = "Observações",
                placeholder = "Escreva aqui...",
            )
        }
    }
}
