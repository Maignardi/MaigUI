package io.github.maignardi.maigui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.maigTouchTarget
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Color tokens for [MaigButtonGroup].
 */
@Immutable
public class MaigButtonGroupColors(
    public val containerColor: Color,
    public val selectedContainerColor: Color,
    public val contentColor: Color,
    public val selectedContentColor: Color,
    public val borderColor: Color,
    public val disabledContentColor: Color,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigButtonGroupColors) return false
        return containerColor == other.containerColor &&
            selectedContainerColor == other.selectedContainerColor &&
            contentColor == other.contentColor &&
            selectedContentColor == other.selectedContentColor &&
            borderColor == other.borderColor &&
            disabledContentColor == other.disabledContentColor
    }

    override fun hashCode(): Int {
        var result = containerColor.hashCode()
        result = 31 * result + selectedContainerColor.hashCode()
        result = 31 * result + contentColor.hashCode()
        result = 31 * result + selectedContentColor.hashCode()
        result = 31 * result + borderColor.hashCode()
        result = 31 * result + disabledContentColor.hashCode()
        return result
    }
}

/**
 * Default values for [MaigButtonGroup].
 */
public object MaigButtonGroupDefaults {
    /** Default corner shape for [MaigButtonGroup]. */
    @Composable
    public fun shape(): Shape = MaigTheme.shapes.md

    /** Default colors for [MaigButtonGroup]. */
    @Composable
    public fun colors(
        containerColor: Color = MaigTheme.colors.surface,
        selectedContainerColor: Color = MaigTheme.colors.primaryDefault,
        contentColor: Color = MaigTheme.colors.textPrimary,
        selectedContentColor: Color = MaigTheme.colors.onPrimary,
        borderColor: Color = MaigTheme.colors.border,
        disabledContentColor: Color = MaigTheme.colors.textDisabled,
    ): MaigButtonGroupColors = MaigButtonGroupColors(
        containerColor = containerColor,
        selectedContainerColor = selectedContainerColor,
        contentColor = contentColor,
        selectedContentColor = selectedContentColor,
        borderColor = borderColor,
        disabledContentColor = disabledContentColor,
    )
}

/**
 * Connected segmented group of horizontal action/selection buttons sharing a unified border.
 */
@Composable
public fun MaigButtonGroup(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaigButtonGroupDefaults.shape(),
    colors: MaigButtonGroupColors = MaigButtonGroupDefaults.colors(),
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .clip(shape)
            .background(MaigTheme.colors.surfaceVariant)
            .border(1.dp, colors.borderColor, shape)
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val itemShape = MaigTheme.shapes.sm
        options.forEachIndexed { index, label ->
            val isSelected = index == selectedIndex
            val bg = if (isSelected) colors.selectedContainerColor else Color.Transparent
            val fg = when {
                !enabled -> colors.disabledContentColor
                isSelected -> colors.selectedContentColor
                else -> colors.contentColor
            }
            val interactionSource = remember { MutableInteractionSource() }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .defaultMinSize(minHeight = 38.dp)
                    .clip(itemShape)
                    .background(bg)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = rememberMaigRipple(bounded = true, color = fg.copy(alpha = 0.16f)),
                        enabled = enabled,
                        role = Role.RadioButton,
                        onClick = { onOptionSelected(index) },
                    )
                    .semantics { selected = isSelected }
                    .padding(horizontal = MaigTheme.spacing.md, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                BasicText(
                    text = label,
                    style = MaigTheme.typography.label.copy(color = fg),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigButtonGroupLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigButtonGroup(
                options = listOf("Dia", "Semana", "Mês"),
                selectedIndex = 1,
                onOptionSelected = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigButtonGroupDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigButtonGroup(
                options = listOf("Lista", "Grade"),
                selectedIndex = 0,
                onOptionSelected = {},
            )
        }
    }
}
