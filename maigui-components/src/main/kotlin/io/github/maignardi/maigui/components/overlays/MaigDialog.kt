package io.github.maignardi.maigui.components.overlays

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.maignardi.maigui.components.buttons.MaigButton
import io.github.maignardi.maigui.components.buttons.MaigButtonColor
import io.github.maignardi.maigui.components.buttons.MaigButtonSize
import io.github.maignardi.maigui.components.buttons.MaigButtonVariant
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Color configuration for [MaigDialog].
 */
@Immutable
public class MaigDialogColors(
    public val containerColor: Color,
    public val borderColor: Color,
    public val titleColor: Color,
    public val messageColor: Color,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigDialogColors) return false
        return containerColor == other.containerColor &&
            borderColor == other.borderColor &&
            titleColor == other.titleColor &&
            messageColor == other.messageColor
    }

    override fun hashCode(): Int {
        var result = containerColor.hashCode()
        result = 31 * result + borderColor.hashCode()
        result = 31 * result + titleColor.hashCode()
        result = 31 * result + messageColor.hashCode()
        return result
    }
}

/**
 * Default values for [MaigDialog].
 */
public object MaigDialogDefaults {
    @Composable
    public fun shape(): Shape = MaigTheme.shapes.xl

    @Composable
    public fun colors(): MaigDialogColors {
        val c = MaigTheme.colors
        return MaigDialogColors(
            containerColor = c.surfaceElevated,
            borderColor = c.border,
            titleColor = c.textPrimary,
            messageColor = c.textSecondary,
        )
    }
}

/**
 * Modal dialog supporting custom content, icon, title, message, and action slots.
 */
@Composable
public fun MaigDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String? = null,
    icon: @Composable (() -> Unit)? = null,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    confirmButton: @Composable (() -> Unit)? = null,
    dismissButton: @Composable (() -> Unit)? = null,
    shape: Shape = MaigDialogDefaults.shape(),
    colors: MaigDialogColors = MaigDialogDefaults.colors(),
    content: @Composable (ColumnScope.() -> Unit)? = null,
) {
    if (!visible) return

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
        ),
    ) {
        MaigDialogSurface(
            modifier = modifier,
            title = title,
            message = message,
            icon = icon,
            confirmButton = confirmButton,
            dismissButton = dismissButton,
            shape = shape,
            colors = colors,
            content = content,
        )
    }
}

/**
 * Inline-renderable dialog surface useful for both modal dialogs and visual previews.
 */
@Composable
public fun MaigDialogSurface(
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String? = null,
    icon: @Composable (() -> Unit)? = null,
    confirmButton: @Composable (() -> Unit)? = null,
    dismissButton: @Composable (() -> Unit)? = null,
    shape: Shape = MaigDialogDefaults.shape(),
    colors: MaigDialogColors = MaigDialogDefaults.colors(),
    content: @Composable (ColumnScope.() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (MaigTheme.colors.isDark) 0.dp else MaigTheme.elevation.lg,
                shape = shape,
                ambientColor = MaigTheme.colors.shadow,
                spotColor = MaigTheme.colors.shadow,
            )
            .clip(shape)
            .background(colors.containerColor)
            .border(1.dp, colors.borderColor, shape)
            .padding(MaigTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
    ) {
        if (icon != null) {
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                icon()
            }
        }
        if (title != null) {
            BasicText(
                text = title,
                style = MaigTheme.typography.title.copy(color = colors.titleColor),
            )
        }
        if (message != null) {
            BasicText(
                text = message,
                style = MaigTheme.typography.body.copy(color = colors.messageColor),
            )
        }
        if (content != null) {
            content()
        }
        if (confirmButton != null || dismissButton != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaigTheme.spacing.xs),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (dismissButton != null) {
                    dismissButton()
                }
                if (dismissButton != null && confirmButton != null) {
                    Spacer(modifier = Modifier.width(MaigTheme.spacing.sm))
                }
                if (confirmButton != null) {
                    confirmButton()
                }
            }
        }
    }
}

/**
 * Convenience alert dialog with a single acknowledgement action.
 */
@Composable
public fun MaigAlertDialog(
    visible: Boolean,
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = "OK",
    icon: @Composable (() -> Unit)? = null,
) {
    MaigDialog(
        visible = visible,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        title = title,
        message = message,
        icon = icon,
        confirmButton = {
            MaigButton(
                text = confirmText,
                onClick = onDismissRequest,
                size = MaigButtonSize.Small,
            )
        },
    )
}

/**
 * Convenience confirmation dialog with confirm and cancel buttons.
 */
@Composable
public fun MaigConfirmDialog(
    visible: Boolean,
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = "Confirmar",
    dismissText: String = "Cancelar",
    destructive: Boolean = false,
    icon: @Composable (() -> Unit)? = null,
) {
    MaigDialog(
        visible = visible,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        title = title,
        message = message,
        icon = icon,
        dismissButton = {
            MaigButton(
                text = dismissText,
                onClick = onDismissRequest,
                variant = MaigButtonVariant.Ghost,
                size = MaigButtonSize.Small,
            )
        },
        confirmButton = {
            MaigButton(
                text = confirmText,
                onClick = onConfirm,
                color = if (destructive) MaigButtonColor.Danger else MaigButtonColor.Primary,
                size = MaigButtonSize.Small,
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun MaigDialogLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.backgroundSubtle)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigDialogSurface(
                title = "Excluir item?",
                message = "Esta ação não poderá ser desfeita depois de confirmada.",
                dismissButton = {
                    MaigButton(text = "Cancelar", onClick = {}, variant = MaigButtonVariant.Ghost, size = MaigButtonSize.Small)
                },
                confirmButton = {
                    MaigButton(text = "Excluir", onClick = {}, color = MaigButtonColor.Danger, size = MaigButtonSize.Small)
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigDialogDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigDialogSurface(
                title = "Atualização disponível",
                message = "Uma nova versão do MaigUI está pronta para instalar.",
                confirmButton = {
                    MaigButton(text = "Entendi", onClick = {}, size = MaigButtonSize.Small)
                },
            )
        }
    }
}
