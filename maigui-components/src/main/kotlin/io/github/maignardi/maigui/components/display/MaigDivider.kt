package io.github.maignardi.maigui.components.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Orientation options for [MaigDivider].
 */
public enum class MaigDividerOrientation {
    Horizontal,
    Vertical,
}

/**
 * Default values for [MaigDivider].
 */
public object MaigDividerDefaults {
    public val Thickness: Dp = 1.dp

    @Composable
    public fun color(): Color = MaigTheme.colors.divider
}

/**
 * Thin horizontal or vertical separator line with optional centered [label].
 */
@Composable
public fun MaigDivider(
    modifier: Modifier = Modifier,
    orientation: MaigDividerOrientation = MaigDividerOrientation.Horizontal,
    label: String? = null,
    thickness: Dp = MaigDividerDefaults.Thickness,
    color: Color = MaigDividerDefaults.color(),
) {
    when (orientation) {
        MaigDividerOrientation.Horizontal -> {
            if (label == null) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(thickness)
                        .background(color),
                )
            } else {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(thickness)
                            .background(color),
                    )
                    BasicText(
                        text = label,
                        style = MaigTheme.typography.caption.copy(color = MaigTheme.colors.textSecondary),
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(thickness)
                            .background(color),
                    )
                }
            }
        }

        MaigDividerOrientation.Vertical -> {
            Box(
                modifier = modifier
                    .fillMaxHeight()
                    .width(thickness)
                    .background(color),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigDividerLightPreview() {
    MaigTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
        ) {
            MaigDivider()
            MaigDivider(label = "OU CONTINUE COM")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigDividerDarkPreview() {
    MaigTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
        ) {
            MaigDivider()
            MaigDivider(label = "SEÇÃO")
        }
    }
}
