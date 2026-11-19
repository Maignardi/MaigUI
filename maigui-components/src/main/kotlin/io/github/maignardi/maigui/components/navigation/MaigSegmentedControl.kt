package io.github.maignardi.maigui.components.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Defaults for [MaigSegmentedControl].
 */
public object MaigSegmentedControlDefaults {
    @Composable
    public fun shape(): Shape = CircleShape
}

/**
 * Smooth iOS/Linear-inspired sliding pill segmented control with spring thumb animation.
 */
@Composable
public fun MaigSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaigSegmentedControlDefaults.shape(),
) {
    if (options.isEmpty()) return
    val safeIndex = selectedIndex.coerceIn(0, options.lastIndex)
    val c = MaigTheme.colors

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(shape)
            .background(c.surfaceVariant)
            .border(1.dp, c.borderSubtle, shape)
            .padding(4.dp),
    ) {
        val segmentWidth = maxWidth / options.size
        val thumbOffset by animateDpAsState(
            targetValue = segmentWidth * safeIndex,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMedium,
            ),
            label = "MaigSegmentedThumb",
        )

        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .width(segmentWidth)
                .fillMaxHeight()
                .shadow(2.dp, shape, ambientColor = c.shadow, spotColor = c.shadow)
                .clip(shape)
                .background(c.surfaceElevated)
                .border(0.75.dp, c.borderSubtle, shape),
        )

        Row(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            options.forEachIndexed { index, title ->
                val isSelected = index == safeIndex
                val textColor = when {
                    !enabled -> c.textDisabled
                    isSelected -> c.textPrimary
                    else -> c.textSecondary
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(shape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            enabled = enabled,
                            role = Role.Tab,
                            onClick = { onOptionSelected(index) },
                        )
                        .semantics { selected = isSelected },
                    contentAlignment = Alignment.Center,
                ) {
                    BasicText(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaigTheme.typography.label.copy(color = textColor),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigSegmentedControlLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigSegmentedControl(
                options = listOf("Visão", "Métricas", "Logs"),
                selectedIndex = 0,
                onOptionSelected = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigSegmentedControlDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigSegmentedControl(
                options = listOf("Mensal", "Anual"),
                selectedIndex = 1,
                onOptionSelected = {},
            )
        }
    }
}
