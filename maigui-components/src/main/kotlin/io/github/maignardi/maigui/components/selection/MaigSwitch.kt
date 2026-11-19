package io.github.maignardi.maigui.components.selection

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.maigTouchTarget
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Color tokens for [MaigSwitch].
 */
@Immutable
public class MaigSwitchColors(
    public val checkedTrackColor: Color,
    public val uncheckedTrackColor: Color,
    public val checkedThumbColor: Color,
    public val uncheckedThumbColor: Color,
    public val disabledTrackColor: Color,
    public val disabledThumbColor: Color,
    public val labelColor: Color,
    public val disabledLabelColor: Color,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigSwitchColors) return false
        return checkedTrackColor == other.checkedTrackColor &&
            uncheckedTrackColor == other.uncheckedTrackColor &&
            checkedThumbColor == other.checkedThumbColor &&
            uncheckedThumbColor == other.uncheckedThumbColor &&
            disabledTrackColor == other.disabledTrackColor &&
            disabledThumbColor == other.disabledThumbColor &&
            labelColor == other.labelColor &&
            disabledLabelColor == other.disabledLabelColor
    }

    override fun hashCode(): Int {
        var result = checkedTrackColor.hashCode()
        result = 31 * result + uncheckedTrackColor.hashCode()
        result = 31 * result + checkedThumbColor.hashCode()
        result = 31 * result + uncheckedThumbColor.hashCode()
        result = 31 * result + disabledTrackColor.hashCode()
        result = 31 * result + disabledThumbColor.hashCode()
        result = 31 * result + labelColor.hashCode()
        result = 31 * result + disabledLabelColor.hashCode()
        return result
    }
}

/**
 * Default dimensions and colors for [MaigSwitch].
 */
public object MaigSwitchDefaults {
    public val TrackWidth: Dp = 46.dp
    public val TrackHeight: Dp = 26.dp
    public val ThumbSize: Dp = 20.dp

    @Composable
    public fun colors(): MaigSwitchColors {
        val c = MaigTheme.colors
        return MaigSwitchColors(
            checkedTrackColor = c.primaryDefault,
            uncheckedTrackColor = if (c.isDark) c.neutral.shade700 else c.neutral.shade300,
            checkedThumbColor = Color.White,
            uncheckedThumbColor = Color.White,
            disabledTrackColor = c.disabled,
            disabledThumbColor = c.onDisabled,
            labelColor = c.textPrimary,
            disabledLabelColor = c.textDisabled,
        )
    }
}

/**
 * Animated toggle switch with optional [label] and 48dp accessible touch target.
 */
@Composable
public fun MaigSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    colors: MaigSwitchColors = MaigSwitchDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    val resolvedInteraction = interactionSource ?: remember { MutableInteractionSource() }
    val trackColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledTrackColor
            checked -> colors.checkedTrackColor
            else -> colors.uncheckedTrackColor
        },
        animationSpec = tween(160),
        label = "MaigSwitchTrack",
    )
    val thumbColor = when {
        !enabled -> colors.disabledThumbColor
        checked -> colors.checkedThumbColor
        else -> colors.uncheckedThumbColor
    }
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) {
            MaigSwitchDefaults.TrackWidth - MaigSwitchDefaults.ThumbSize - 3.dp
        } else {
            3.dp
        },
        animationSpec = tween(160),
        label = "MaigSwitchThumbOffset",
    )

    val toggleModifier = if (onClickEnabled(onCheckedChange)) {
        Modifier.toggleable(
            value = checked,
            onValueChange = { onCheckedChange?.invoke(it) },
            enabled = enabled,
            role = Role.Switch,
            interactionSource = resolvedInteraction,
            indication = null,
        )
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .maigTouchTarget(minWidth = 48.dp, minHeight = 48.dp)
            .then(toggleModifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(MaigSwitchDefaults.TrackWidth, MaigSwitchDefaults.TrackHeight)
                .clip(CircleShape)
                .background(trackColor)
                .border(
                    width = 1.dp,
                    color = if (checked && enabled) colors.checkedTrackColor else MaigTheme.colors.borderSubtle,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.CenterStart,
        ) {
            Box(
                modifier = Modifier
                    .offset(x = thumbOffset)
                    .size(MaigSwitchDefaults.ThumbSize)
                    .shadow(2.dp, CircleShape)
                    .clip(CircleShape)
                    .background(thumbColor),
            )
        }

        if (label != null) {
            Spacer(modifier = Modifier.width(MaigTheme.spacing.md))
            BasicText(
                text = label,
                style = MaigTheme.typography.body.copy(
                    color = if (enabled) colors.labelColor else colors.disabledLabelColor,
                ),
            )
        }
    }
}

private fun onClickEnabled(callback: ((Boolean) -> Unit)?): Boolean = callback != null

@Preview(showBackground = true)
@Composable
private fun MaigSwitchLightPreview() {
    MaigTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigSwitch(checked = true, onCheckedChange = {}, label = "Notificações push")
            MaigSwitch(checked = false, onCheckedChange = {}, label = "Modo economia")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigSwitchDarkPreview() {
    MaigTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigSwitch(checked = true, onCheckedChange = {}, label = "Tema escuro")
            MaigSwitch(checked = false, onCheckedChange = {}, enabled = false, label = "Desabilitado")
        }
    }
}
