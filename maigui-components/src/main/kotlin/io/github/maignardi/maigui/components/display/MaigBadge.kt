package io.github.maignardi.maigui.components.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.maignardi.maigui.components.buttons.MaigButton
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Semantic color intents for [MaigBadge].
 */
public enum class MaigBadgeColor {
    Primary,
    Secondary,
    Success,
    Warning,
    Danger,
    Info,
}

/**
 * Defaults for [MaigBadge].
 */
public object MaigBadgeDefaults {
    @Composable
    public fun colors(color: MaigBadgeColor = MaigBadgeColor.Danger): Pair<Color, Color> {
        val c = MaigTheme.colors
        return when (color) {
            MaigBadgeColor.Primary -> c.primaryDefault to c.onPrimary
            MaigBadgeColor.Secondary -> c.secondaryDefault to c.onSecondary
            MaigBadgeColor.Success -> c.successDefault to c.onSuccess
            MaigBadgeColor.Warning -> c.warningDefault to c.onWarning
            MaigBadgeColor.Danger -> c.dangerDefault to c.onDanger
            MaigBadgeColor.Info -> c.infoDefault to c.onInfo
        }
    }
}

/**
 * Compact notification dot or pill counter badge.
 */
@Composable
public fun MaigBadge(
    modifier: Modifier = Modifier,
    text: String? = null,
    color: MaigBadgeColor = MaigBadgeColor.Danger,
) {
    val (bg, fg) = MaigBadgeDefaults.colors(color)
    if (text == null) {
        Box(
            modifier = modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(bg),
        )
    } else {
        Box(
            modifier = modifier
                .defaultMinSize(minWidth = 20.dp, minHeight = 20.dp)
                .clip(CircleShape)
                .background(bg)
                .padding(horizontal = 6.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center,
        ) {
            BasicText(
                text = text,
                style = MaigTheme.typography.caption.copy(
                    color = fg,
                    fontSize = 11.sp,
                ),
            )
        }
    }
}

/**
 * Anchors a [badge] at the top-end corner of [content].
 */
@Composable
public fun MaigBadgedBox(
    badge: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    badgeOffsetX: Dp = 8.dp,
    badgeOffsetY: Dp = (-6).dp,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier) {
        content()
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = badgeOffsetX, y = badgeOffsetY),
            content = badge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigBadgeLightPreview() {
    MaigTheme(darkTheme = false) {
        Row(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MaigBadge(text = "99+", color = MaigBadgeColor.Danger)
            MaigBadge(text = "Novo", color = MaigBadgeColor.Primary)
            MaigBadge(color = MaigBadgeColor.Success)
            MaigBadgedBox(badge = { MaigBadge(text = "3") }) {
                MaigButton(text = "Inbox", onClick = {})
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigBadgeDarkPreview() {
    MaigTheme(darkTheme = true) {
        Row(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MaigBadge(text = "12", color = MaigBadgeColor.Primary)
            MaigBadge(text = "Beta", color = MaigBadgeColor.Info)
        }
    }
}
