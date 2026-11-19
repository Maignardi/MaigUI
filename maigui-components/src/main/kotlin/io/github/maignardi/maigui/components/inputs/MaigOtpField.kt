package io.github.maignardi.maigui.components.inputs

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Color tokens for [MaigOtpField].
 */
@Immutable
public class MaigOtpFieldColors(
    public val cellContainerColor: Color,
    public val disabledContainerColor: Color,
    public val textColor: Color,
    public val borderColor: Color,
    public val focusedBorderColor: Color,
    public val errorBorderColor: Color,
    public val errorColor: Color,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigOtpFieldColors) return false
        return cellContainerColor == other.cellContainerColor &&
            disabledContainerColor == other.disabledContainerColor &&
            textColor == other.textColor &&
            borderColor == other.borderColor &&
            focusedBorderColor == other.focusedBorderColor &&
            errorBorderColor == other.errorBorderColor &&
            errorColor == other.errorColor
    }

    override fun hashCode(): Int {
        var result = cellContainerColor.hashCode()
        result = 31 * result + disabledContainerColor.hashCode()
        result = 31 * result + textColor.hashCode()
        result = 31 * result + borderColor.hashCode()
        result = 31 * result + focusedBorderColor.hashCode()
        result = 31 * result + errorBorderColor.hashCode()
        result = 31 * result + errorColor.hashCode()
        return result
    }
}

/**
 * Default values for [MaigOtpField].
 */
public object MaigOtpFieldDefaults {
    /** Default number of OTP digits. */
    public const val DefaultLength: Int = 6

    /** Default width/height of each digit box. */
    public val CellSize: Dp = 48.dp

    /** Default shape of each digit cell. */
    @Composable
    public fun cellShape(): Shape = MaigTheme.shapes.md

    /** Default colors for [MaigOtpField]. */
    @Composable
    public fun colors(): MaigOtpFieldColors {
        val c = MaigTheme.colors
        return MaigOtpFieldColors(
            cellContainerColor = c.surface,
            disabledContainerColor = c.disabled,
            textColor = c.textPrimary,
            borderColor = c.border,
            focusedBorderColor = c.borderFocus,
            errorBorderColor = c.dangerDefault,
            errorColor = c.dangerDefault,
        )
    }
}

/**
 * Segmented One-Time Password (OTP) verification code input field.
 */
@Composable
public fun MaigOtpField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = MaigOtpFieldDefaults.DefaultLength,
    enabled: Boolean = true,
    mask: Boolean = false,
    label: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    onComplete: ((String) -> Unit)? = null,
    cellSize: Dp = MaigOtpFieldDefaults.CellSize,
    cellShape: Shape = MaigOtpFieldDefaults.cellShape(),
    colors: MaigOtpFieldColors = MaigOtpFieldDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    val resolvedInteraction = interactionSource ?: remember { MutableInteractionSource() }
    val isFocused by resolvedInteraction.collectIsFocusedAsState()
    val focusRequester = remember { FocusRequester() }
    val sanitized = value.filter { it.isDigit() }.take(length)

    Column(
        modifier = modifier.semantics {
            contentDescription = label ?: "Código de verificação de $length dígitos"
            if (isError) {
                error(errorText ?: "Código inválido")
            }
        },
        verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.xs),
    ) {
        if (label != null) {
            BasicText(
                text = label,
                style = MaigTheme.typography.label.copy(
                    color = if (isError) colors.errorColor else MaigTheme.colors.textPrimary,
                ),
            )
        }

        Box(
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = enabled,
                onClick = { focusRequester.requestFocus() },
            ),
        ) {
            BasicTextField(
                value = sanitized,
                onValueChange = { next ->
                    val filtered = next.filter { it.isDigit() }.take(length)
                    onValueChange(filtered)
                    if (filtered.length == length && filtered != sanitized) {
                        onComplete?.invoke(filtered)
                    }
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .size(1.dp)
                    .alpha(0.01f),
                enabled = enabled,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                interactionSource = resolvedInteraction,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(length) { index ->
                    val char = sanitized.getOrNull(index)
                    val isCellActive = isFocused && (index == sanitized.length || (sanitized.length == length && index == length - 1))
                    val targetBorder = when {
                        isError -> colors.errorBorderColor
                        isCellActive -> colors.focusedBorderColor
                        char != null -> colors.focusedBorderColor.copy(alpha = 0.55f)
                        else -> colors.borderColor
                    }
                    val borderColor by animateColorAsState(
                        targetValue = targetBorder,
                        animationSpec = tween(150),
                        label = "MaigOtpCellBorder",
                    )
                    val cellBg = when {
                        !enabled -> colors.disabledContainerColor
                        char != null && !isError -> MaigTheme.colors.primarySubtle.copy(alpha = if (MaigTheme.colors.isDark) 0.40f else 0.45f)
                        else -> colors.cellContainerColor
                    }

                    Box(
                        modifier = Modifier
                            .size(width = cellSize, height = cellSize + 4.dp)
                            .clip(cellShape)
                            .background(cellBg)
                            .border(if (isCellActive || isError) 1.5.dp else 1.dp, borderColor, cellShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        val display = when {
                            char == null -> if (isCellActive) "|" else ""
                            mask -> "•"
                            else -> char.toString()
                        }
                        BasicText(
                            text = display,
                            style = MaigTheme.typography.headline.copy(
                                color = if (char == null && isCellActive) {
                                    colors.focusedBorderColor
                                } else {
                                    colors.textColor
                                },
                            ),
                        )
                    }
                }
            }
        }

        if (isError && errorText != null) {
            BasicText(
                text = errorText,
                style = MaigTheme.typography.caption.copy(color = colors.errorColor),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigOtpFieldLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigOtpField(
                value = "482",
                onValueChange = {},
                label = "Código SMS",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigOtpFieldDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigOtpField(
                value = "123456",
                onValueChange = {},
                label = "PIN",
                mask = true,
            )
        }
    }
}
