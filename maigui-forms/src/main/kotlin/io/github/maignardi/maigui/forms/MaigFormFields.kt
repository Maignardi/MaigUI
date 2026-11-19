package io.github.maignardi.maigui.forms

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import io.github.maignardi.maigui.components.inputs.MaigMoneyField
import io.github.maignardi.maigui.components.inputs.MaigPasswordField
import io.github.maignardi.maigui.components.inputs.MaigPhoneField
import io.github.maignardi.maigui.components.inputs.MaigTextArea
import io.github.maignardi.maigui.components.inputs.MaigTextField
import io.github.maignardi.maigui.components.inputs.MaigTextFieldColors
import io.github.maignardi.maigui.components.inputs.MaigTextFieldDefaults
import io.github.maignardi.maigui.components.inputs.MaigTextFieldVariant

/**
 * Form-connected [MaigTextField] binding directly to a [MaigFormField] with automatic
 * focus-loss validation and `ImeAction.Next` / `ImeAction.Done` navigation.
 */
@Composable
public fun MaigTextField(
    field: MaigFormField,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    helperText: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: String? = null,
    suffix: String? = null,
    maxLength: Int? = null,
    showCounter: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    variant: MaigTextFieldVariant = MaigTextFieldVariant.Outlined,
    colors: MaigTextFieldColors = MaigTextFieldDefaults.colors(variant = variant),
    onSubmit: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    val focusManager = LocalFocusManager.current
    val isLast = field.isLastField()
    val imeAction = if (isLast) ImeAction.Done else ImeAction.Next

    MaigTextField(
        value = field.value,
        onValueChange = field::onValueChange,
        modifier = modifier
            .focusRequester(field.focusRequester)
            .onFocusChanged { field.onFocusChanged(it.isFocused) },
        enabled = enabled,
        readOnly = readOnly,
        label = label,
        placeholder = placeholder,
        helperText = helperText,
        errorText = field.error,
        isError = field.error != null,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        maxLength = maxLength,
        showCounter = showCounter,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onNext = {
                if (!field.moveFocusNext()) {
                    focusManager.clearFocus()
                }
            },
            onDone = {
                focusManager.clearFocus()
                onSubmit?.invoke()
            },
        ),
        variant = variant,
        colors = colors,
        interactionSource = interactionSource,
    )
}

/**
 * Form-connected [MaigPasswordField] binding directly to a [MaigFormField].
 */
@Composable
public fun MaigPasswordField(
    field: MaigFormField,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = "Senha",
    placeholder: String? = "Digite sua senha",
    helperText: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    variant: MaigTextFieldVariant = MaigTextFieldVariant.Outlined,
    colors: MaigTextFieldColors = MaigTextFieldDefaults.colors(variant = variant),
    onSubmit: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    val focusManager = LocalFocusManager.current
    val isLast = field.isLastField()
    val imeAction = if (isLast) ImeAction.Done else ImeAction.Next

    MaigPasswordField(
        value = field.value,
        onValueChange = field::onValueChange,
        modifier = modifier
            .focusRequester(field.focusRequester)
            .onFocusChanged { field.onFocusChanged(it.isFocused) },
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        helperText = helperText,
        errorText = field.error,
        isError = field.error != null,
        leadingIcon = leadingIcon,
        variant = variant,
        colors = colors,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onNext = {
                if (!field.moveFocusNext()) {
                    focusManager.clearFocus()
                }
            },
            onDone = {
                focusManager.clearFocus()
                onSubmit?.invoke()
            },
        ),
        interactionSource = interactionSource,
    )
}

/**
 * Form-connected [MaigTextArea] binding directly to a [MaigFormField].
 */
@Composable
public fun MaigTextArea(
    field: MaigFormField,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    helperText: String? = null,
    maxLength: Int? = null,
    showCounter: Boolean = maxLength != null,
    variant: MaigTextFieldVariant = MaigTextFieldVariant.Outlined,
    colors: MaigTextFieldColors = MaigTextFieldDefaults.colors(variant = variant),
    interactionSource: MutableInteractionSource? = null,
) {
    MaigTextArea(
        value = field.value,
        onValueChange = field::onValueChange,
        modifier = modifier
            .focusRequester(field.focusRequester)
            .onFocusChanged { field.onFocusChanged(it.isFocused) },
        enabled = enabled,
        readOnly = readOnly,
        label = label,
        placeholder = placeholder,
        helperText = helperText,
        errorText = field.error,
        isError = field.error != null,
        maxLength = maxLength,
        showCounter = showCounter,
        variant = variant,
        colors = colors,
        interactionSource = interactionSource,
    )
}

/**
 * Form-connected [MaigMoneyField] binding directly to a [MaigFormField].
 */
@Composable
public fun MaigMoneyField(
    field: MaigFormField,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = "Valor",
    placeholder: String? = "0,00",
    helperText: String? = null,
    variant: MaigTextFieldVariant = MaigTextFieldVariant.Outlined,
    colors: MaigTextFieldColors = MaigTextFieldDefaults.colors(variant = variant),
    onSubmit: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    val focusManager = LocalFocusManager.current
    val isLast = field.isLastField()
    val imeAction = if (isLast) ImeAction.Done else ImeAction.Next

    MaigMoneyField(
        value = field.value,
        onValueChange = field::onValueChange,
        modifier = modifier
            .focusRequester(field.focusRequester)
            .onFocusChanged { field.onFocusChanged(it.isFocused) },
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        helperText = helperText,
        errorText = field.error,
        isError = field.error != null,
        variant = variant,
        colors = colors,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onNext = {
                if (!field.moveFocusNext()) {
                    focusManager.clearFocus()
                }
            },
            onDone = {
                focusManager.clearFocus()
                onSubmit?.invoke()
            },
        ),
        interactionSource = interactionSource,
    )
}

/**
 * Form-connected [MaigPhoneField] binding directly to a [MaigFormField].
 */
@Composable
public fun MaigPhoneField(
    field: MaigFormField,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = "Telefone",
    placeholder: String? = "(11) 99999-9999",
    helperText: String? = null,
    variant: MaigTextFieldVariant = MaigTextFieldVariant.Outlined,
    colors: MaigTextFieldColors = MaigTextFieldDefaults.colors(variant = variant),
    onSubmit: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    val focusManager = LocalFocusManager.current
    val isLast = field.isLastField()
    val imeAction = if (isLast) ImeAction.Done else ImeAction.Next

    MaigPhoneField(
        value = field.value,
        onValueChange = field::onValueChange,
        modifier = modifier
            .focusRequester(field.focusRequester)
            .onFocusChanged { field.onFocusChanged(it.isFocused) },
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        helperText = helperText,
        errorText = field.error,
        isError = field.error != null,
        variant = variant,
        colors = colors,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onNext = {
                if (!field.moveFocusNext()) {
                    focusManager.clearFocus()
                }
            },
            onDone = {
                focusManager.clearFocus()
                onSubmit?.invoke()
            },
        ),
        interactionSource = interactionSource,
    )
}
