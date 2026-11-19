package io.github.maignardi.maigui.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
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
import io.github.maignardi.maigui.components.internal.MaigGlyphType
import io.github.maignardi.maigui.components.internal.MaigStatusGlyphIcon
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Semantic category for [MaigAlert].
 */
public enum class MaigAlertType {
    Info,
    Success,
    Warning,
    Danger,
}

/**
 * Visual emphasis style for [MaigAlert].
 */
public enum class MaigAlertVariant {
    Soft,
    Outline,
    Solid,
}

/**
 * Color tokens for [MaigAlert].
 */
@Immutable
public class MaigAlertColors(
    public val containerColor: Color,
    public val borderColor: Color,
    public val iconColor: Color,
    public val titleColor: Color,
    public val descriptionColor: Color,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigAlertColors) return false
        return containerColor == other.containerColor &&
            borderColor == other.borderColor &&
            iconColor == other.iconColor &&
            titleColor == other.titleColor &&
            descriptionColor == other.descriptionColor
    }

    override fun hashCode(): Int {
        var result = containerColor.hashCode()
        result = 31 * result + borderColor.hashCode()
        result = 31 * result + iconColor.hashCode()
        result = 31 * result + titleColor.hashCode()
        result = 31 * result + descriptionColor.hashCode()
        return result
    }
}

/**
 * Default values for [MaigAlert].
 */
public object MaigAlertDefaults {
    @Composable
    public fun shape(): Shape = MaigTheme.shapes.lg

    @Composable
    public fun colors(
        type: MaigAlertType = MaigAlertType.Info,
        variant: MaigAlertVariant = MaigAlertVariant.Soft,
    ): MaigAlertColors {
        val c = MaigTheme.colors
        val (solid, onSolid, subtle, onSubtle) = when (type) {
            MaigAlertType.Info -> listOf(c.infoDefault, c.onInfo, c.infoSubtle, c.onInfoSubtle)
            MaigAlertType.Success -> listOf(c.successDefault, c.onSuccess, c.successSubtle, c.onSuccessSubtle)
            MaigAlertType.Warning -> listOf(c.warningDefault, c.onWarning, c.warningSubtle, c.onWarningSubtle)
            MaigAlertType.Danger -> listOf(c.dangerDefault, c.onDanger, c.dangerSubtle, c.onDangerSubtle)
        }
        return when (variant) {
            MaigAlertVariant.Soft -> MaigAlertColors(
                containerColor = subtle,
                borderColor = solid.copy(alpha = 0.35f),
                iconColor = solid,
                titleColor = onSubtle,
                descriptionColor = onSubtle.copy(alpha = 0.85f),
            )

            MaigAlertVariant.Outline -> MaigAlertColors(
                containerColor = c.surface,
                borderColor = solid,
                iconColor = solid,
                titleColor = c.textPrimary,
                descriptionColor = c.textSecondary,
            )

            MaigAlertVariant.Solid -> MaigAlertColors(
                containerColor = solid,
                borderColor = solid,
                iconColor = onSolid,
                titleColor = onSolid,
                descriptionColor = onSolid.copy(alpha = 0.90f),
            )
        }
    }
}

/**
 * Inline alert/banner component for communicating status, warnings, errors, or tips.
 */
@Composable
public fun MaigAlert(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    type: MaigAlertType = MaigAlertType.Info,
    variant: MaigAlertVariant = MaigAlertVariant.Soft,
    onDismiss: (() -> Unit)? = null,
    action: @Composable (() -> Unit)? = null,
    shape: Shape = MaigAlertDefaults.shape(),
    colors: MaigAlertColors = MaigAlertDefaults.colors(type = type, variant = variant),
) {
    val glyph = when (type) {
        MaigAlertType.Info -> MaigGlyphType.Info
        MaigAlertType.Success -> MaigGlyphType.Success
        MaigAlertType.Warning -> MaigGlyphType.Warning
        MaigAlertType.Danger -> MaigGlyphType.Error
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.containerColor)
            .border(1.dp, colors.borderColor, shape)
            .padding(MaigTheme.spacing.md),
        verticalAlignment = Alignment.Top,
    ) {
        Box(modifier = Modifier.padding(top = 2.dp)) {
            MaigStatusGlyphIcon(type = glyph, tint = colors.iconColor, size = 20.dp)
        }
        Spacer(modifier = Modifier.width(MaigTheme.spacing.md))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.xs),
        ) {
            BasicText(
                text = title,
                style = MaigTheme.typography.label.copy(color = colors.titleColor),
            )
            if (description != null) {
                BasicText(
                    text = description,
                    style = MaigTheme.typography.bodySmall.copy(color = colors.descriptionColor),
                )
            }
            if (action != null) {
                Box(modifier = Modifier.padding(top = MaigTheme.spacing.xs)) {
                    action()
                }
            }
        }
        if (onDismiss != null) {
            Spacer(modifier = Modifier.width(MaigTheme.spacing.sm))
            Box(
                modifier = Modifier
                    .clip(MaigTheme.shapes.full)
                    .clickable(onClick = onDismiss)
                    .padding(MaigTheme.spacing.xs)
                    .semantics { contentDescription = "Fechar alerta" },
            ) {
                MaigCloseIcon(size = 14.dp, tint = colors.titleColor.copy(alpha = 0.7f))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigAlertLightPreview() {
    MaigTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigAlert(title = "Pagamento confirmado", description = "Seu pedido #1024 foi aprovado.", type = MaigAlertType.Success)
            MaigAlert(title = "Atenção", description = "Verifique seus dados antes de enviar.", type = MaigAlertType.Warning, variant = MaigAlertVariant.Outline)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigAlertDarkPreview() {
    MaigTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigAlert(title = "Erro de validação", description = "O CPF informado é inválido.", type = MaigAlertType.Danger)
            MaigAlert(title = "Dica", description = "Use MaigTheme para padronizar seu app.", type = MaigAlertType.Info, variant = MaigAlertVariant.Solid)
        }
    }
}
