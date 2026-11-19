package io.github.maignardi.maigui.core.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Complete color token palette for MaigUI, containing both full tonal scales (50-900)
 * and semantic UI roles for light and dark themes.
 */
@Immutable
public class MaigColors(
    public val isDark: Boolean,
    public val primary: MaigColorScale,
    public val secondary: MaigColorScale,
    public val success: MaigColorScale,
    public val warning: MaigColorScale,
    public val danger: MaigColorScale,
    public val info: MaigColorScale,
    public val neutral: MaigColorScale,
    public val primaryDefault: Color,
    public val primaryHover: Color,
    public val primaryActive: Color,
    public val primarySubtle: Color,
    public val onPrimary: Color,
    public val onPrimarySubtle: Color,
    public val secondaryDefault: Color,
    public val secondaryHover: Color,
    public val secondaryActive: Color,
    public val secondarySubtle: Color,
    public val onSecondary: Color,
    public val onSecondarySubtle: Color,
    public val successDefault: Color,
    public val successSubtle: Color,
    public val onSuccess: Color,
    public val onSuccessSubtle: Color,
    public val warningDefault: Color,
    public val warningSubtle: Color,
    public val onWarning: Color,
    public val onWarningSubtle: Color,
    public val dangerDefault: Color,
    public val dangerSubtle: Color,
    public val onDanger: Color,
    public val onDangerSubtle: Color,
    public val infoDefault: Color,
    public val infoSubtle: Color,
    public val onInfo: Color,
    public val onInfoSubtle: Color,
    public val background: Color,
    public val backgroundSubtle: Color,
    public val surface: Color,
    public val surfaceVariant: Color,
    public val surfaceElevated: Color,
    public val onBackground: Color,
    public val onSurface: Color,
    public val onSurfaceVariant: Color,
    public val textPrimary: Color,
    public val textSecondary: Color,
    public val textTertiary: Color,
    public val textDisabled: Color,
    public val textInverse: Color,
    public val border: Color,
    public val borderSubtle: Color,
    public val borderStrong: Color,
    public val borderFocus: Color,
    public val divider: Color,
    public val disabled: Color,
    public val onDisabled: Color,
    public val scrim: Color,
    public val shadow: Color,
    public val focusRing: Color,
    public val ripple: Color,
) {
    /**
     * Returns a copy of this [MaigColors] instance with selectively overridden tokens.
     */
    public fun copy(
        isDark: Boolean = this.isDark,
        primary: MaigColorScale = this.primary,
        secondary: MaigColorScale = this.secondary,
        success: MaigColorScale = this.success,
        warning: MaigColorScale = this.warning,
        danger: MaigColorScale = this.danger,
        info: MaigColorScale = this.info,
        neutral: MaigColorScale = this.neutral,
        primaryDefault: Color = this.primaryDefault,
        primaryHover: Color = this.primaryHover,
        primaryActive: Color = this.primaryActive,
        primarySubtle: Color = this.primarySubtle,
        onPrimary: Color = this.onPrimary,
        onPrimarySubtle: Color = this.onPrimarySubtle,
        secondaryDefault: Color = this.secondaryDefault,
        secondaryHover: Color = this.secondaryHover,
        secondaryActive: Color = this.secondaryActive,
        secondarySubtle: Color = this.secondarySubtle,
        onSecondary: Color = this.onSecondary,
        onSecondarySubtle: Color = this.onSecondarySubtle,
        successDefault: Color = this.successDefault,
        successSubtle: Color = this.successSubtle,
        onSuccess: Color = this.onSuccess,
        onSuccessSubtle: Color = this.onSuccessSubtle,
        warningDefault: Color = this.warningDefault,
        warningSubtle: Color = this.warningSubtle,
        onWarning: Color = this.onWarning,
        onWarningSubtle: Color = this.onWarningSubtle,
        dangerDefault: Color = this.dangerDefault,
        dangerSubtle: Color = this.dangerSubtle,
        onDanger: Color = this.onDanger,
        onDangerSubtle: Color = this.onDangerSubtle,
        infoDefault: Color = this.infoDefault,
        infoSubtle: Color = this.infoSubtle,
        onInfo: Color = this.onInfo,
        onInfoSubtle: Color = this.onInfoSubtle,
        background: Color = this.background,
        backgroundSubtle: Color = this.backgroundSubtle,
        surface: Color = this.surface,
        surfaceVariant: Color = this.surfaceVariant,
        surfaceElevated: Color = this.surfaceElevated,
        onBackground: Color = this.onBackground,
        onSurface: Color = this.onSurface,
        onSurfaceVariant: Color = this.onSurfaceVariant,
        textPrimary: Color = this.textPrimary,
        textSecondary: Color = this.textSecondary,
        textTertiary: Color = this.textTertiary,
        textDisabled: Color = this.textDisabled,
        textInverse: Color = this.textInverse,
        border: Color = this.border,
        borderSubtle: Color = this.borderSubtle,
        borderStrong: Color = this.borderStrong,
        borderFocus: Color = this.borderFocus,
        divider: Color = this.divider,
        disabled: Color = this.disabled,
        onDisabled: Color = this.onDisabled,
        scrim: Color = this.scrim,
        shadow: Color = this.shadow,
        focusRing: Color = this.focusRing,
        ripple: Color = this.ripple,
    ): MaigColors = MaigColors(
        isDark = isDark,
        primary = primary,
        secondary = secondary,
        success = success,
        warning = warning,
        danger = danger,
        info = info,
        neutral = neutral,
        primaryDefault = primaryDefault,
        primaryHover = primaryHover,
        primaryActive = primaryActive,
        primarySubtle = primarySubtle,
        onPrimary = onPrimary,
        onPrimarySubtle = onPrimarySubtle,
        secondaryDefault = secondaryDefault,
        secondaryHover = secondaryHover,
        secondaryActive = secondaryActive,
        secondarySubtle = secondarySubtle,
        onSecondary = onSecondary,
        onSecondarySubtle = onSecondarySubtle,
        successDefault = successDefault,
        successSubtle = successSubtle,
        onSuccess = onSuccess,
        onSuccessSubtle = onSuccessSubtle,
        warningDefault = warningDefault,
        warningSubtle = warningSubtle,
        onWarning = onWarning,
        onWarningSubtle = onWarningSubtle,
        dangerDefault = dangerDefault,
        dangerSubtle = dangerSubtle,
        onDanger = onDanger,
        onDangerSubtle = onDangerSubtle,
        infoDefault = infoDefault,
        infoSubtle = infoSubtle,
        onInfo = onInfo,
        onInfoSubtle = onInfoSubtle,
        background = background,
        backgroundSubtle = backgroundSubtle,
        surface = surface,
        surfaceVariant = surfaceVariant,
        surfaceElevated = surfaceElevated,
        onBackground = onBackground,
        onSurface = onSurface,
        onSurfaceVariant = onSurfaceVariant,
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        textTertiary = textTertiary,
        textDisabled = textDisabled,
        textInverse = textInverse,
        border = border,
        borderSubtle = borderSubtle,
        borderStrong = borderStrong,
        borderFocus = borderFocus,
        divider = divider,
        disabled = disabled,
        onDisabled = onDisabled,
        scrim = scrim,
        shadow = shadow,
        focusRing = focusRing,
        ripple = ripple,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigColors) return false
        return isDark == other.isDark &&
            primary == other.primary &&
            secondary == other.secondary &&
            success == other.success &&
            warning == other.warning &&
            danger == other.danger &&
            info == other.info &&
            neutral == other.neutral &&
            primaryDefault == other.primaryDefault &&
            primaryHover == other.primaryHover &&
            primaryActive == other.primaryActive &&
            primarySubtle == other.primarySubtle &&
            onPrimary == other.onPrimary &&
            onPrimarySubtle == other.onPrimarySubtle &&
            secondaryDefault == other.secondaryDefault &&
            secondaryHover == other.secondaryHover &&
            secondaryActive == other.secondaryActive &&
            secondarySubtle == other.secondarySubtle &&
            onSecondary == other.onSecondary &&
            onSecondarySubtle == other.onSecondarySubtle &&
            successDefault == other.successDefault &&
            successSubtle == other.successSubtle &&
            onSuccess == other.onSuccess &&
            onSuccessSubtle == other.onSuccessSubtle &&
            warningDefault == other.warningDefault &&
            warningSubtle == other.warningSubtle &&
            onWarning == other.onWarning &&
            onWarningSubtle == other.onWarningSubtle &&
            dangerDefault == other.dangerDefault &&
            dangerSubtle == other.dangerSubtle &&
            onDanger == other.onDanger &&
            onDangerSubtle == other.onDangerSubtle &&
            infoDefault == other.infoDefault &&
            infoSubtle == other.infoSubtle &&
            onInfo == other.onInfo &&
            onInfoSubtle == other.onInfoSubtle &&
            background == other.background &&
            backgroundSubtle == other.backgroundSubtle &&
            surface == other.surface &&
            surfaceVariant == other.surfaceVariant &&
            surfaceElevated == other.surfaceElevated &&
            onBackground == other.onBackground &&
            onSurface == other.onSurface &&
            onSurfaceVariant == other.onSurfaceVariant &&
            textPrimary == other.textPrimary &&
            textSecondary == other.textSecondary &&
            textTertiary == other.textTertiary &&
            textDisabled == other.textDisabled &&
            textInverse == other.textInverse &&
            border == other.border &&
            borderSubtle == other.borderSubtle &&
            borderStrong == other.borderStrong &&
            borderFocus == other.borderFocus &&
            divider == other.divider &&
            disabled == other.disabled &&
            onDisabled == other.onDisabled &&
            scrim == other.scrim &&
            shadow == other.shadow &&
            focusRing == other.focusRing &&
            ripple == other.ripple
    }

    override fun hashCode(): Int {
        var result = isDark.hashCode()
        result = 31 * result + primary.hashCode()
        result = 31 * result + secondary.hashCode()
        result = 31 * result + success.hashCode()
        result = 31 * result + warning.hashCode()
        result = 31 * result + danger.hashCode()
        result = 31 * result + info.hashCode()
        result = 31 * result + neutral.hashCode()
        result = 31 * result + primaryDefault.hashCode()
        result = 31 * result + primaryHover.hashCode()
        result = 31 * result + primaryActive.hashCode()
        result = 31 * result + primarySubtle.hashCode()
        result = 31 * result + onPrimary.hashCode()
        result = 31 * result + onPrimarySubtle.hashCode()
        result = 31 * result + secondaryDefault.hashCode()
        result = 31 * result + secondaryHover.hashCode()
        result = 31 * result + secondaryActive.hashCode()
        result = 31 * result + secondarySubtle.hashCode()
        result = 31 * result + onSecondary.hashCode()
        result = 31 * result + onSecondarySubtle.hashCode()
        result = 31 * result + successDefault.hashCode()
        result = 31 * result + successSubtle.hashCode()
        result = 31 * result + onSuccess.hashCode()
        result = 31 * result + onSuccessSubtle.hashCode()
        result = 31 * result + warningDefault.hashCode()
        result = 31 * result + warningSubtle.hashCode()
        result = 31 * result + onWarning.hashCode()
        result = 31 * result + onWarningSubtle.hashCode()
        result = 31 * result + dangerDefault.hashCode()
        result = 31 * result + dangerSubtle.hashCode()
        result = 31 * result + onDanger.hashCode()
        result = 31 * result + onDangerSubtle.hashCode()
        result = 31 * result + infoDefault.hashCode()
        result = 31 * result + infoSubtle.hashCode()
        result = 31 * result + onInfo.hashCode()
        result = 31 * result + onInfoSubtle.hashCode()
        result = 31 * result + background.hashCode()
        result = 31 * result + backgroundSubtle.hashCode()
        result = 31 * result + surface.hashCode()
        result = 31 * result + surfaceVariant.hashCode()
        result = 31 * result + surfaceElevated.hashCode()
        result = 31 * result + onBackground.hashCode()
        result = 31 * result + onSurface.hashCode()
        result = 31 * result + onSurfaceVariant.hashCode()
        result = 31 * result + textPrimary.hashCode()
        result = 31 * result + textSecondary.hashCode()
        result = 31 * result + textTertiary.hashCode()
        result = 31 * result + textDisabled.hashCode()
        result = 31 * result + textInverse.hashCode()
        result = 31 * result + border.hashCode()
        result = 31 * result + borderSubtle.hashCode()
        result = 31 * result + borderStrong.hashCode()
        result = 31 * result + borderFocus.hashCode()
        result = 31 * result + divider.hashCode()
        result = 31 * result + disabled.hashCode()
        result = 31 * result + onDisabled.hashCode()
        result = 31 * result + scrim.hashCode()
        result = 31 * result + shadow.hashCode()
        result = 31 * result + focusRing.hashCode()
        result = 31 * result + ripple.hashCode()
        return result
    }

    override fun toString(): String =
        "MaigColors(isDark=$isDark, primaryDefault=$primaryDefault, background=$background, surface=$surface)"
}
