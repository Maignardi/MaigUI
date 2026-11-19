package io.github.maignardi.maigui.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.MaigCloseIcon
import io.github.maignardi.maigui.components.internal.MaigSearchIcon
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Default configuration for [MaigSearchField].
 */
public object MaigSearchFieldDefaults {
    /** Default placeholder text. */
    public const val DefaultPlaceholder: String = "Buscar..."

    /** Default clear button accessibility label. */
    public const val ClearDescription: String = "Limpar busca"
}

/**
 * Search input field with leading magnifying glass icon and clear button when non-empty.
 */
@Composable
public fun MaigSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = MaigSearchFieldDefaults.DefaultPlaceholder,
    onSearch: ((String) -> Unit)? = null,
    variant: MaigTextFieldVariant = MaigTextFieldVariant.Filled,
    colors: MaigTextFieldColors = MaigTextFieldDefaults.colors(variant = variant),
    interactionSource: MutableInteractionSource? = null,
) {
    MaigTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        placeholder = placeholder,
        leadingIcon = { MaigSearchIcon() },
        trailingIcon = if (value.isNotEmpty() && enabled) {
            {
                val clearInteraction = remember { MutableInteractionSource() }
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clickable(
                            interactionSource = clearInteraction,
                            indication = rememberMaigRipple(bounded = false),
                            role = Role.Button,
                            onClick = { onValueChange("") },
                        )
                        .semantics { contentDescription = MaigSearchFieldDefaults.ClearDescription },
                    contentAlignment = Alignment.Center,
                ) {
                    MaigCloseIcon(size = 14.dp)
                }
            }
        } else {
            null
        },
        shape = MaigTheme.shapes.full,
        variant = variant,
        colors = colors,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch?.invoke(value) }),
        interactionSource = interactionSource,
    )
}

@Preview(showBackground = true)
@Composable
private fun MaigSearchFieldLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigSearchField(value = "MaigButton", onValueChange = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigSearchFieldDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigSearchField(value = "", onValueChange = {})
        }
    }
}
