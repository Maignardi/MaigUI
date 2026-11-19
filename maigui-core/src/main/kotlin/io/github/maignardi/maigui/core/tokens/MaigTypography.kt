package io.github.maignardi.maigui.core.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.maignardi.maigui.core.R

/**
 * Default font resources and typography construction helpers for MaigUI.
 */
public object MaigTypographyDefaults {
    /**
     * Bundled Inter font family (Regular, Medium, SemiBold, Bold) licensed under SIL OFL 1.1.
     */
    public val DefaultFontFamily: FontFamily = FontFamily(
        Font(resId = R.font.inter_regular, weight = FontWeight.Normal),
        Font(resId = R.font.inter_medium, weight = FontWeight.Medium),
        Font(resId = R.font.inter_semibold, weight = FontWeight.SemiBold),
        Font(resId = R.font.inter_bold, weight = FontWeight.Bold),
    )
}

/**
 * Typography scale tokens for MaigUI.
 *
 * Consumers can provide a custom [FontFamily] via the constructor or override individual [TextStyle]s.
 */
@Immutable
public class MaigTypography(
    public val displayLarge: TextStyle,
    public val displayMedium: TextStyle,
    public val headline: TextStyle,
    public val title: TextStyle,
    public val subtitle: TextStyle,
    public val body: TextStyle,
    public val bodySmall: TextStyle,
    public val label: TextStyle,
    public val caption: TextStyle,
    public val button: TextStyle,
) {
    /**
     * Builds a complete [MaigTypography] scale using [fontFamily] (defaults to Inter).
     */
    public constructor(
        fontFamily: FontFamily = MaigTypographyDefaults.DefaultFontFamily,
    ) : this(
        displayLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp,
            lineHeight = 40.sp,
            letterSpacing = (-0.5).sp,
        ),
        displayMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            lineHeight = 34.sp,
            letterSpacing = (-0.3).sp,
        ),
        headline = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = (-0.2).sp,
        ),
        title = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp,
        ),
        subtitle = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            letterSpacing = 0.sp,
        ),
        body = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            letterSpacing = 0.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.1.sp,
        ),
        label = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.1.sp,
        ),
        caption = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.2.sp,
        ),
        button = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
        ),
    )

    /**
     * Returns a new [MaigTypography] with all text styles updated to use [fontFamily].
     */
    public fun withFontFamily(fontFamily: FontFamily): MaigTypography = MaigTypography(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        headline = headline.copy(fontFamily = fontFamily),
        title = title.copy(fontFamily = fontFamily),
        subtitle = subtitle.copy(fontFamily = fontFamily),
        body = body.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        label = label.copy(fontFamily = fontFamily),
        caption = caption.copy(fontFamily = fontFamily),
        button = button.copy(fontFamily = fontFamily),
    )

    /**
     * Creates a copy of this [MaigTypography] with optionally overridden styles.
     */
    public fun copy(
        displayLarge: TextStyle = this.displayLarge,
        displayMedium: TextStyle = this.displayMedium,
        headline: TextStyle = this.headline,
        title: TextStyle = this.title,
        subtitle: TextStyle = this.subtitle,
        body: TextStyle = this.body,
        bodySmall: TextStyle = this.bodySmall,
        label: TextStyle = this.label,
        caption: TextStyle = this.caption,
        button: TextStyle = this.button,
    ): MaigTypography = MaigTypography(
        displayLarge = displayLarge,
        displayMedium = displayMedium,
        headline = headline,
        title = title,
        subtitle = subtitle,
        body = body,
        bodySmall = bodySmall,
        label = label,
        caption = caption,
        button = button,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigTypography) return false
        return displayLarge == other.displayLarge &&
            displayMedium == other.displayMedium &&
            headline == other.headline &&
            title == other.title &&
            subtitle == other.subtitle &&
            body == other.body &&
            bodySmall == other.bodySmall &&
            label == other.label &&
            caption == other.caption &&
            button == other.button
    }

    override fun hashCode(): Int {
        var result = displayLarge.hashCode()
        result = 31 * result + displayMedium.hashCode()
        result = 31 * result + headline.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + subtitle.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + bodySmall.hashCode()
        result = 31 * result + label.hashCode()
        result = 31 * result + caption.hashCode()
        result = 31 * result + button.hashCode()
        return result
    }

    override fun toString(): String = "MaigTypography(body=$body, title=$title)"
}
