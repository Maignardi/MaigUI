package io.github.maignardi.maigui.components.display

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.MaigCloseIcon
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Emphasis styles for [MaigTag].
 */
public enum class MaigTagVariant {
    Soft,
    Solid,
    Outline,
}

/**
 * Semantic color palettes for [MaigTag].
 */
public enum class MaigTagColor {
    Primary,
    Secondary,
    Success,
    Warning,
    Danger,
    Info,
    Neutral,
}

/**
 * Defaults for [MaigTag].
 */
public object MaigTagDefaults {
    @Composable
    public fun shape(): Shape = MaigTheme.shapes.sm

    @Composable
    public fun colors(
        color: MaigTagColor,
        variant: MaigTagVariant,
    ): Triple<Color, Color, Color> {
        val c = MaigTheme.colors
        val (solid, onSolid, subtle, onSubtle) = when (color) {
            MaigTagColor.Primary -> listOf(c.primaryDefault, c.onPrimary, c.primarySubtle, c.onPrimarySubtle)
            MaigTagColor.Secondary -> listOf(c.secondaryDefault, c.onSecondary, c.secondarySubtle, c.onSecondarySubtle)
            MaigTagColor.Success -> listOf(c.successDefault, c.onSuccess, c.successSubtle, c.onSuccessSubtle)
            MaigTagColor.Warning -> listOf(c.warningDefault, c.onWarning, c.warningSubtle, c.onWarningSubtle)
            MaigTagColor.Danger -> listOf(c.dangerDefault, c.onDanger, c.dangerSubtle, c.onDangerSubtle)
            MaigTagColor.Info -> listOf(c.infoDefault, c.onInfo, c.infoSubtle, c.onInfoSubtle)
            MaigTagColor.Neutral -> listOf(c.neutral.shade700, Color.White, c.surfaceVariant, c.textPrimary)
        }
        return when (variant) {
            MaigTagVariant.Soft -> Triple(subtle, onSubtle, Color.Transparent)
            MaigTagVariant.Solid -> Triple(solid, onSolid, Color.Transparent)
            MaigTagVariant.Outline -> Triple(Color.Transparent, if (color == MaigTagColor.Neutral) c.textPrimary else solid, solid.copy(alpha = 0.55f))
        }
    }
}

/**
 * Compact status/category tag with optional leading dot/icon and optional dismiss action.
 */
@Composable
public fun MaigTag(
    text: String,
    modifier: Modifier = Modifier,
    variant: MaigTagVariant = MaigTagVariant.Soft,
    color: MaigTagColor = MaigTagColor.Primary,
    showDot: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
    shape: Shape = MaigTagDefaults.shape(),
) {
    val (bg, fg, borderColor) = MaigTagDefaults.colors(color = color, variant = variant)
    Row(
        modifier = modifier
            .clip(shape)
            .background(bg)
            .then(
                if (borderColor != Color.Transparent) {
                    Modifier.border(1.dp, borderColor, shape)
                } else {
                    Modifier
                },
            )
            .padding(horizontal = MaigTheme.spacing.sm, vertical = MaigTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalMaigContentColor provides fg) {
            if (showDot) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(fg),
                )
                Spacer(modifier = Modifier.width(6.dp))
            } else if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(4.dp))
            }

            BasicText(
                text = text,
                style = MaigTheme.typography.caption.copy(color = fg),
            )

            if (onRemove != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = onRemove)
                        .semantics { contentDescription = "Remover $text" },
                ) {
                    MaigCloseIcon(size = 12.dp, tint = fg)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigTagLightPreview() {
    MaigTheme(darkTheme = false) {
        Row(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigTag(text = "Ativo", color = MaigTagColor.Success, showDot = true)
            MaigTag(text = "Pro", color = MaigTagColor.Primary, variant = MaigTagVariant.Solid)
            MaigTag(text = "Pendente", color = MaigTagColor.Warning, variant = MaigTagVariant.Outline)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigTagDarkPreview() {
    MaigTheme(darkTheme = true) {
        Row(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigTag(text = "Erro", color = MaigTagColor.Danger, showDot = true)
            MaigTag(text = "v0.1.0", color = MaigTagColor.Neutral)
        }
    }
}
