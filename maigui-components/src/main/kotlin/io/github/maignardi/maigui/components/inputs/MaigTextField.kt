package io.github.maignardi.maigui.components.inputs

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.MaigSearchIcon
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Visual container styles for [MaigTextField].
 */
public enum class MaigTextFieldVariant {
    /** Crisp bordered field on surface background. */
    Outlined,

    /** Softly tinted filled field with subtle focus border. */
    Filled,
}

/**
 * Color tokens for [MaigTextField] and derived input components.
 */
@Immutable
public class MaigTextFieldColors(
    public val containerColor: Color,
    public val disabledContainerColor: Color,
    public val textColor: Color,
    public val disabledTextColor: Color,
    public val placeholderColor: Color,
    public val labelColor: Color,
    public val errorLabelColor: Color,
    public val borderColor: Color,
    public val focusedBorderColor: Color,
    public val errorBorderColor: Color,
    public val disabledBorderColor: Color,
    public val helperColor: Color,
    public val errorColor: Color,
    public val iconColor: Color,
    public val cursorColor: Color,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigTextFieldColors) return false
        return containerColor == other.containerColor &&
            disabledContainerColor == other.disabledContainerColor &&
            textColor == other.textColor &&
            disabledTextColor == other.disabledTextColor &&
            placeholderColor == other.placeholderColor &&
            labelColor == other.labelColor &&
            errorLabelColor == other.errorLabelColor &&
            borderColor == other.borderColor &&
            focusedBorderColor == other.focusedBorderColor &&
            errorBorderColor == other.errorBorderColor &&
            disabledBorderColor == other.disabledBorderColor &&
            helperColor == other.helperColor &&
            errorColor == other.errorColor &&
            iconColor == other.iconColor &&
            cursorColor == other.cursorColor
    }

    override fun hashCode(): Int {
        var result = containerColor.hashCode()
        result = 31 * result + disabledContainerColor.hashCode()
        result = 31 * result + textColor.hashCode()
        result = 31 * result + disabledTextColor.hashCode()
        result = 31 * result + placeholderColor.hashCode()
        result = 31 * result + labelColor.hashCode()
        result = 31 * result + errorLabelColor.hashCode()
        result = 31 * result + borderColor.hashCode()
        result = 31 * result + focusedBorderColor.hashCode()
        result = 31 * result + errorBorderColor.hashCode()
        result = 31 * result + disabledBorderColor.hashCode()
        result = 31 * result + helperColor.hashCode()
        result = 31 * result + errorColor.hashCode()
        result = 31 * result + iconColor.hashCode()
        result = 31 * result + cursorColor.hashCode()
        return result
    }
}

/**
 * Default configuration values for [MaigTextField].
 */
public object MaigTextFieldDefaults {
    /** Minimum interactive height of a single-line input box. */
    public val MinHeight: Dp = 48.dp

    /** Default corner shape for text fields. */
    @Composable
    public fun shape(): Shape = MaigTheme.shapes.md

    /** Resolves [MaigTextFieldColors] for [variant]. */
    @Composable
    public fun colors(
        variant: MaigTextFieldVariant = MaigTextFieldVariant.Outlined,
    ): MaigTextFieldColors {
        val c = MaigTheme.colors
        return when (variant) {
            MaigTextFieldVariant.Outlined -> MaigTextFieldColors(
                containerColor = c.surface,
                disabledContainerColor = c.disabled,
                textColor = c.textPrimary,
                disabledTextColor = c.textDisabled,
                placeholderColor = c.textTertiary,
                labelColor = c.textPrimary,
                errorLabelColor = c.dangerDefault,
                borderColor = c.border,
                focusedBorderColor = c.borderFocus,
                errorBorderColor = c.dangerDefault,
                disabledBorderColor = c.borderSubtle,
                helperColor = c.textSecondary,
                errorColor = c.dangerDefault,
                iconColor = c.textSecondary,
                cursorColor = c.primaryDefault,
            )

            MaigTextFieldVariant.Filled -> MaigTextFieldColors(
                containerColor = c.surfaceVariant,
                disabledContainerColor = c.disabled,
                textColor = c.textPrimary,
                disabledTextColor = c.textDisabled,
                placeholderColor = c.textTertiary,
                labelColor = c.textPrimary,
                errorLabelColor = c.dangerDefault,
                borderColor = Color.Transparent,
                focusedBorderColor = c.borderFocus,
                errorBorderColor = c.dangerDefault,
                disabledBorderColor = Color.Transparent,
                helperColor = c.textSecondary,
                errorColor = c.dangerDefault,
                iconColor = c.textSecondary,
                cursorColor = c.primaryDefault,
            )
        }
    }
}

/**
 * Foundation-based text input field supporting labels, placeholders, helper/error text,
 * leading/trailing icons, character counters, and Outlined/Filled variants.
 */
@Composable
public fun MaigTextField(
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
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: String? = null,
    suffix: String? = null,
    maxLength: Int? = null,
    showCounter: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minHeight: Dp = MaigTextFieldDefaults.MinHeight,
    textStyle: TextStyle = MaigTheme.typography.body,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    variant: MaigTextFieldVariant = MaigTextFieldVariant.Outlined,
    shape: Shape = MaigTextFieldDefaults.shape(),
    colors: MaigTextFieldColors = MaigTextFieldDefaults.colors(variant = variant),
    interactionSource: MutableInteractionSource? = null,
) {
    val resolvedInteraction = interactionSource ?: remember { MutableInteractionSource() }
    val isFocused by resolvedInteraction.collectIsFocusedAsState()

    val targetBorderColor = when {
        !enabled -> colors.disabledBorderColor
        isError -> colors.errorBorderColor
        isFocused -> colors.focusedBorderColor
        else -> colors.borderColor
    }
    val borderColor by animateColorAsState(
        targetValue = targetBorderColor,
        animationSpec = tween(150),
        label = "MaigTextFieldBorder",
    )
    val borderWidth = if (isFocused || isError) 1.5.dp else 1.dp
    val containerColor = when {
        !enabled -> colors.disabledContainerColor
        isError && variant == MaigTextFieldVariant.Filled -> MaigTheme.colors.dangerSubtle.copy(alpha = 0.55f)
        else -> colors.containerColor
    }
    val inputTextColor = if (enabled) colors.textColor else colors.disabledTextColor
    val labelColor = if (isError) colors.errorLabelColor else colors.labelColor

    val handleValueChange: (String) -> Unit = remember(onValueChange, maxLength) {
        { next ->
            val clamped = if (maxLength != null && next.length > maxLength) {
                next.take(maxLength)
            } else {
                next
            }
            onValueChange(clamped)
        }
    }

    Column(
        modifier = modifier.semantics(mergeDescendants = false) {
            if (isError) {
                error(errorText ?: "Campo inválido")
            }
        },
        verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.xs),
    ) {
        if (label != null) {
            BasicText(
                text = label,
                style = MaigTheme.typography.label.copy(color = labelColor),
            )
        }

        BasicTextField(
            value = value,
            onValueChange = handleValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle.copy(color = inputTextColor),
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = resolvedInteraction,
            cursorBrush = SolidColor(if (isError) colors.errorColor else colors.cursorColor),
            decorationBox = { innerTextField ->
                val focusRingColor = when {
                    isError && isFocused -> colors.errorColor.copy(alpha = 0.22f)
                    isFocused -> MaigTheme.colors.focusRing
                    else -> Color.Transparent
                }
                val boxModifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (focusRingColor != Color.Transparent) {
                            Modifier
                                .border(3.dp, focusRingColor, shape)
                                .padding(2.dp)
                        } else {
                            Modifier
                        },
                    )
                    .defaultMinSize(minHeight = minHeight)
                    .clip(shape)
                    .background(containerColor)
                    .then(
                        if (borderColor != Color.Transparent) {
                            Modifier.border(borderWidth, borderColor, shape)
                        } else {
                            Modifier
                        },
                    )
                    .padding(
                        horizontal = MaigTheme.spacing.lg,
                        vertical = if (singleLine) MaigTheme.spacing.sm else MaigTheme.spacing.md,
                    )

                Row(
                    modifier = boxModifier,
                    verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top,
                ) {
                    if (leadingIcon != null) {
                        CompositionLocalProvider(LocalMaigContentColor provides colors.iconColor) {
                            Box(
                                modifier = Modifier.padding(top = if (singleLine) 0.dp else 2.dp),
                            ) {
                                leadingIcon()
                            }
                        }
                        Spacer(modifier = Modifier.width(MaigTheme.spacing.sm))
                    }

                    if (prefix != null) {
                        BasicText(
                            text = prefix,
                            style = textStyle.copy(color = colors.placeholderColor),
                            modifier = Modifier.padding(end = MaigTheme.spacing.xs),
                        )
                    }

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (value.isEmpty() && placeholder != null) {
                            BasicText(
                                text = placeholder,
                                style = textStyle.copy(color = colors.placeholderColor),
                            )
                        }
                        innerTextField()
                    }

                    if (suffix != null) {
                        BasicText(
                            text = suffix,
                            style = textStyle.copy(color = colors.placeholderColor),
                            modifier = Modifier.padding(start = MaigTheme.spacing.xs),
                        )
                    }

                    if (trailingIcon != null) {
                        Spacer(modifier = Modifier.width(MaigTheme.spacing.sm))
                        CompositionLocalProvider(LocalMaigContentColor provides colors.iconColor) {
                            Box(
                                modifier = Modifier.padding(top = if (singleLine) 0.dp else 2.dp),
                            ) {
                                trailingIcon()
                            }
                        }
                    }
                }
            },
        )

        val bottomMessage = if (isError && errorText != null) errorText else helperText
        if (bottomMessage != null || showCounter) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (bottomMessage != null) {
                    BasicText(
                        text = bottomMessage,
                        modifier = Modifier.weight(1f, fill = false),
                        style = MaigTheme.typography.caption.copy(
                            color = if (isError) colors.errorColor else colors.helperColor,
                        ),
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                if (showCounter) {
                    val counterText = if (maxLength != null) {
                        "${value.length}/$maxLength"
                    } else {
                        "${value.length}"
                    }
                    BasicText(
                        text = counterText,
                        style = MaigTheme.typography.caption.copy(
                            color = if (isError) colors.errorColor else colors.helperColor,
                        ),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigTextFieldLightPreview() {
    MaigTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
        ) {
            MaigTextField(
                value = "matheus@maigui.dev",
                onValueChange = {},
                label = "E-mail",
                helperText = "Usaremos para notificações",
                leadingIcon = { MaigSearchIcon() },
            )
            MaigTextField(
                value = "inválido",
                onValueChange = {},
                label = "Usuário",
                errorText = "Nome de usuário indisponível",
                variant = MaigTextFieldVariant.Filled,
                maxLength = 20,
                showCounter = true,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigTextFieldDarkPreview() {
    MaigTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
        ) {
            MaigTextField(
                value = "",
                onValueChange = {},
                label = "Nome completo",
                placeholder = "Digite seu nome",
            )
            MaigTextField(
                value = "Texto bloqueado",
                onValueChange = {},
                label = "Desabilitado",
                enabled = false,
            )
        }
    }
}
