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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.MaigEyeIcon
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Default values for [MaigPasswordField].
 */
public object MaigPasswordFieldDefaults {
    /** Default content description when password characters are hidden. */
    public const val ShowPasswordDescription: String = "Mostrar senha"

    /** Default content description when password characters are visible. */
    public const val HidePasswordDescription: String = "Ocultar senha"
}

/**
 * Password input field with an accessible visibility toggle button.
 */
@Composable
public fun MaigPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = "Senha",
    placeholder: String? = "Digite sua senha",
    helperText: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    leadingIcon: @Composable (() -> Unit)? = null,
    variant: MaigTextFieldVariant = MaigTextFieldVariant.Outlined,
    colors: MaigTextFieldColors = MaigTextFieldDefaults.colors(variant = variant),
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource? = null,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    MaigTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        helperText = helperText,
        errorText = errorText,
        isError = isError,
        leadingIcon = leadingIcon,
        trailingIcon = {
            val description = if (passwordVisible) {
                MaigPasswordFieldDefaults.HidePasswordDescription
            } else {
                MaigPasswordFieldDefaults.ShowPasswordDescription
            }
            val toggleInteraction = remember { MutableInteractionSource() }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clickable(
                        interactionSource = toggleInteraction,
                        indication = rememberMaigRipple(bounded = false),
                        enabled = enabled,
                        role = Role.Button,
                        onClick = { passwordVisible = !passwordVisible },
                    )
                    .semantics { contentDescription = description },
                contentAlignment = Alignment.Center,
            ) {
                MaigEyeIcon(visible = passwordVisible)
            }
        },
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        variant = variant,
        colors = colors,
        interactionSource = interactionSource,
    )
}

@Preview(showBackground = true)
@Composable
private fun MaigPasswordFieldLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigPasswordField(value = "supersecret", onValueChange = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigPasswordFieldDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigPasswordField(
                value = "123",
                onValueChange = {},
                errorText = "Mínimo de 8 caracteres",
            )
        }
    }
}
