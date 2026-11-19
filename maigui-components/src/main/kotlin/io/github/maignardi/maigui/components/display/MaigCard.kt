package io.github.maignardi.maigui.components.display

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Visual surface styles for [MaigCard].
 */
public enum class MaigCardVariant {
    /** Raised card with elevation shadow and subtle border. */
    Elevated,

    /** Flat card with crisp 1dp border. */
    Outlined,

    /** Tinted filled card without shadow. */
    Filled,
}

/**
 * Color tokens for [MaigCard].
 */
@Immutable
public class MaigCardColors(
    public val containerColor: Color,
    public val contentColor: Color,
    public val borderColor: Color,
    public val disabledContainerColor: Color,
    public val disabledContentColor: Color,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigCardColors) return false
        return containerColor == other.containerColor &&
            contentColor == other.contentColor &&
            borderColor == other.borderColor &&
            disabledContainerColor == other.disabledContainerColor &&
            disabledContentColor == other.disabledContentColor
    }

    override fun hashCode(): Int {
        var result = containerColor.hashCode()
        result = 31 * result + contentColor.hashCode()
        result = 31 * result + borderColor.hashCode()
        result = 31 * result + disabledContainerColor.hashCode()
        result = 31 * result + disabledContentColor.hashCode()
        return result
    }
}

/**
 * Default values for [MaigCard].
 */
public object MaigCardDefaults {
    /** Default corner shape for [MaigCard]. */
    @Composable
    public fun shape(): Shape = MaigTheme.shapes.lg

    /** Default internal content padding for [MaigCard]. */
    @Composable
    public fun contentPadding(): PaddingValues = PaddingValues(MaigTheme.spacing.lg)

    /** Default elevation for [variant]. */
    @Composable
    public fun elevation(variant: MaigCardVariant): Dp = when (variant) {
        MaigCardVariant.Elevated -> MaigTheme.elevation.md
        MaigCardVariant.Outlined, MaigCardVariant.Filled -> MaigTheme.elevation.none
    }

    /** Resolves default [MaigCardColors] for [variant]. */
    @Composable
    public fun colors(variant: MaigCardVariant = MaigCardVariant.Elevated): MaigCardColors {
        val c = MaigTheme.colors
        return when (variant) {
            MaigCardVariant.Elevated -> MaigCardColors(
                containerColor = c.surfaceElevated,
                contentColor = c.textPrimary,
                borderColor = c.borderSubtle,
                disabledContainerColor = c.disabled,
                disabledContentColor = c.onDisabled,
            )

            MaigCardVariant.Outlined -> MaigCardColors(
                containerColor = c.surface,
                contentColor = c.textPrimary,
                borderColor = c.border,
                disabledContainerColor = c.disabled,
                disabledContentColor = c.onDisabled,
            )

            MaigCardVariant.Filled -> MaigCardColors(
                containerColor = c.surfaceVariant,
                contentColor = c.textPrimary,
                borderColor = Color.Transparent,
                disabledContainerColor = c.disabled,
                disabledContentColor = c.onDisabled,
            )
        }
    }
}

/**
 * Container card for grouping related content in Elevated, Outlined, or Filled styles.
 */
@Composable
public fun MaigCard(
    modifier: Modifier = Modifier,
    variant: MaigCardVariant = MaigCardVariant.Elevated,
    shape: Shape = MaigCardDefaults.shape(),
    colors: MaigCardColors = MaigCardDefaults.colors(variant),
    elevation: Dp = MaigCardDefaults.elevation(variant),
    contentPadding: PaddingValues = MaigCardDefaults.contentPadding(),
    content: @Composable ColumnScope.() -> Unit,
) {
    val isDark = MaigTheme.colors.isDark
    val borderBrush = if (colors.borderColor != Color.Transparent) {
        if (isDark && variant == MaigCardVariant.Elevated) {
            Brush.verticalGradient(
                listOf(Color.White.copy(alpha = 0.12f), colors.borderColor.copy(alpha = 0.55f)),
            )
        } else {
            Brush.linearGradient(listOf(colors.borderColor, colors.borderColor))
        }
    } else {
        null
    }

    Column(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = MaigTheme.colors.shadow,
                spotColor = MaigTheme.colors.shadow,
            )
            .clip(shape)
            .background(colors.containerColor)
            .then(if (borderBrush != null) Modifier.border(BorderStroke(1.dp, borderBrush), shape) else Modifier)
            .padding(contentPadding),
    ) {
        CompositionLocalProvider(LocalMaigContentColor provides colors.contentColor) {
            content()
        }
    }
}

/**
 * Interactive clickable overload of [MaigCard] with ripple feedback and button semantics.
 */
@Composable
public fun MaigCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: MaigCardVariant = MaigCardVariant.Elevated,
    shape: Shape = MaigCardDefaults.shape(),
    colors: MaigCardColors = MaigCardDefaults.colors(variant),
    elevation: Dp = MaigCardDefaults.elevation(variant),
    contentPadding: PaddingValues = MaigCardDefaults.contentPadding(),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val resolvedInteraction = interactionSource ?: remember { MutableInteractionSource() }
    val isPressed by resolvedInteraction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.98f else 1f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
        label = "MaigCardScale",
    )
    val containerColor = if (enabled) colors.containerColor else colors.disabledContainerColor
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    val borderStroke = if (colors.borderColor != Color.Transparent) {
        BorderStroke(1.dp, colors.borderColor)
    } else {
        null
    }

    Column(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (enabled) elevation else 0.dp,
                shape = shape,
                ambientColor = MaigTheme.colors.shadow,
                spotColor = MaigTheme.colors.shadow,
            )
            .clip(shape)
            .background(containerColor)
            .then(if (borderStroke != null) Modifier.border(borderStroke, shape) else Modifier)
            .clickable(
                interactionSource = resolvedInteraction,
                indication = rememberMaigRipple(bounded = true, color = MaigTheme.colors.ripple),
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(contentPadding),
    ) {
        CompositionLocalProvider(LocalMaigContentColor provides contentColor) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigCardLightPreview() {
    MaigTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
        ) {
            MaigCard(modifier = Modifier.fillMaxWidth(), variant = MaigCardVariant.Elevated) {
                BasicText("Elevated Card", style = MaigTheme.typography.title.copy(color = MaigTheme.colors.textPrimary))
                BasicText("Subtle depth with shadow", style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary))
            }
            MaigCard(modifier = Modifier.fillMaxWidth(), variant = MaigCardVariant.Outlined) {
                BasicText("Outlined Card", style = MaigTheme.typography.title.copy(color = MaigTheme.colors.textPrimary))
                BasicText("Clean 1dp border", style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary))
            }
            MaigCard(onClick = {}, modifier = Modifier.fillMaxWidth(), variant = MaigCardVariant.Filled) {
                BasicText("Filled Clickable Card", style = MaigTheme.typography.title.copy(color = MaigTheme.colors.textPrimary))
                BasicText("Tap for ripple feedback", style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigCardDarkPreview() {
    MaigTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
        ) {
            MaigCard(modifier = Modifier.fillMaxWidth(), variant = MaigCardVariant.Elevated) {
                BasicText("Dark Elevated Card", style = MaigTheme.typography.title.copy(color = MaigTheme.colors.textPrimary))
            }
            MaigCard(modifier = Modifier.fillMaxWidth(), variant = MaigCardVariant.Outlined) {
                BasicText("Dark Outlined Card", style = MaigTheme.typography.title.copy(color = MaigTheme.colors.textPrimary))
            }
        }
    }
}
