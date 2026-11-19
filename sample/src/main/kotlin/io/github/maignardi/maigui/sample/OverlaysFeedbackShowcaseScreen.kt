package io.github.maignardi.maigui.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.maignardi.maigui.components.buttons.MaigButton
import io.github.maignardi.maigui.components.buttons.MaigButtonColor
import io.github.maignardi.maigui.components.buttons.MaigButtonSize
import io.github.maignardi.maigui.components.buttons.MaigButtonVariant
import io.github.maignardi.maigui.components.feedback.LocalMaigToastState
import io.github.maignardi.maigui.components.feedback.MaigAlert
import io.github.maignardi.maigui.components.feedback.MaigAlertType
import io.github.maignardi.maigui.components.feedback.MaigAlertVariant
import io.github.maignardi.maigui.components.feedback.MaigCircularProgressBar
import io.github.maignardi.maigui.components.feedback.MaigLinearProgressBar
import io.github.maignardi.maigui.components.feedback.MaigLoadingOverlay
import io.github.maignardi.maigui.components.feedback.MaigSkeletonCard
import io.github.maignardi.maigui.components.feedback.MaigSpinner
import io.github.maignardi.maigui.components.layout.MaigSection
import io.github.maignardi.maigui.components.overlays.MaigAlertDialog
import io.github.maignardi.maigui.components.overlays.MaigBottomSheet
import io.github.maignardi.maigui.components.overlays.MaigConfirmDialog
import io.github.maignardi.maigui.components.overlays.MaigDropdownMenu
import io.github.maignardi.maigui.components.overlays.MaigMenuDivider
import io.github.maignardi.maigui.components.overlays.MaigMenuItem
import io.github.maignardi.maigui.components.overlays.MaigPopover
import io.github.maignardi.maigui.components.overlays.MaigTooltip
import io.github.maignardi.maigui.components.selection.MaigSlider
import io.github.maignardi.maigui.core.theme.MaigTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun OverlaysFeedbackShowcaseScreen() {
    val toast = LocalMaigToastState.current
    var showAlertDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showPopover by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showLoadingOverlay by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0.68f) }

    if (showLoadingOverlay) {
        LaunchedEffect(Unit) {
            delay(1800)
            showLoadingOverlay = false
            toast.showSuccess("Sincronização concluída!")
        }
    }

    MaigSection(title = "Toasts Globais (MaigToastHost)") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigButton(
                text = "Toast Success",
                onClick = { toast.showSuccess("Operação concluída com sucesso!") },
                color = MaigButtonColor.Success,
                size = MaigButtonSize.Small,
            )
            MaigButton(
                text = "Toast Error",
                onClick = { toast.show("Falha na conexão", type = io.github.maignardi.maigui.components.feedback.MaigToastType.Error, actionLabel = "Tentar", onAction = {}) },
                color = MaigButtonColor.Danger,
                size = MaigButtonSize.Small,
            )
            MaigButton(
                text = "Toast Warning",
                onClick = { toast.showWarning("Armazenamento quase cheio") },
                color = MaigButtonColor.Warning,
                size = MaigButtonSize.Small,
            )
            MaigButton(
                text = "Toast Info",
                onClick = { toast.showInfo("Dica: segure o botão Tooltip") },
                variant = MaigButtonVariant.Soft,
                size = MaigButtonSize.Small,
            )
        }
    }

    MaigSection(title = "Dialogs, BottomSheet, Popover, Menu & Tooltip") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigButton(text = "Alert Dialog", onClick = { showAlertDialog = true }, variant = MaigButtonVariant.Outline, size = MaigButtonSize.Small)
            MaigButton(text = "Confirm Dialog", onClick = { showConfirmDialog = true }, variant = MaigButtonVariant.Outline, size = MaigButtonSize.Small)
            MaigButton(text = "Bottom Sheet", onClick = { showBottomSheet = true }, variant = MaigButtonVariant.Outline, size = MaigButtonSize.Small)

            MaigPopover(
                expanded = showPopover,
                onDismissRequest = { showPopover = false },
                anchor = {
                    MaigButton(text = "Popover", onClick = { showPopover = true }, variant = MaigButtonVariant.Soft, size = MaigButtonSize.Small)
                },
            ) {
                BasicText("Popover ancorado!", style = MaigTheme.typography.title.copy(color = MaigTheme.colors.textPrimary))
                BasicText("Toque fora para fechar.", style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary))
            }

            Box {
                MaigButton(text = "Dropdown Menu", onClick = { showMenu = true }, variant = MaigButtonVariant.Soft, size = MaigButtonSize.Small)
                MaigDropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    MaigMenuItem(text = "Editar", onClick = { showMenu = false; toast.showInfo("Editar") })
                    MaigMenuItem(text = "Duplicar", onClick = { showMenu = false; toast.showInfo("Duplicar") })
                    MaigMenuDivider()
                    MaigMenuItem(text = "Excluir", onClick = { showMenu = false; toast.showError("Item excluído") }, destructive = true)
                }
            }

            MaigTooltip(text = "Segure qualquer botão para tooltip!") {
                MaigButton(text = "Segure (Tooltip)", onClick = { toast.showInfo("Pressione e segure para ver o tooltip") }, variant = MaigButtonVariant.Ghost, size = MaigButtonSize.Small)
            }
        }
    }

    MaigSection(title = "Alerts Inline (Banners)") {
        MaigAlert(title = "Informação", description = "Você está usando MaigUI 0.1.0.", type = MaigAlertType.Info)
        MaigAlert(title = "Sucesso", description = "Build e testes aprovados.", type = MaigAlertType.Success, variant = MaigAlertVariant.Outline)
        MaigAlert(title = "Aviso", description = "Verifique suas credenciais.", type = MaigAlertType.Warning)
        MaigAlert(title = "Erro", description = "Não foi possível sincronizar.", type = MaigAlertType.Danger, variant = MaigAlertVariant.Solid)
    }

    MaigSection(title = "Progress, Loading & Skeleton Shimmer") {
        MaigSlider(value = progress, onValueChange = { progress = it }, label = "Progresso manual")
        MaigLinearProgressBar(progress = progress, showPercentage = true)
        MaigLinearProgressBar(progress = null)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.lg),
        ) {
            MaigCircularProgressBar(progress = progress, showPercentage = true)
            MaigCircularProgressBar(progress = null)
            MaigSpinner()
            MaigButton(
                text = "Overlay Tela Cheia",
                onClick = { showLoadingOverlay = true },
                size = MaigButtonSize.Small,
                variant = MaigButtonVariant.Soft,
            )
        }
        MaigSkeletonCard()
    }

    MaigAlertDialog(
        visible = showAlertDialog,
        title = "MaigUI Alert",
        message = "Dialog construído sobre Compose Foundation.",
        onDismissRequest = { showAlertDialog = false },
    )
    MaigConfirmDialog(
        visible = showConfirmDialog,
        title = "Excluir registro?",
        message = "Essa operação é permanente.",
        destructive = true,
        onConfirm = {
            showConfirmDialog = false
            toast.showError("Registro excluído")
        },
        onDismissRequest = { showConfirmDialog = false },
    )
    MaigBottomSheet(
        visible = showBottomSheet,
        onDismissRequest = { showBottomSheet = false },
        title = "Ações do Bottom Sheet",
    ) {
        BasicText(
            text = "Arraste para baixo ou toque fora para fechar.",
            style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary),
        )
        MaigButton(
            text = "Confirmar ação",
            onClick = {
                showBottomSheet = false
                toast.showSuccess("Confirmado!")
            },
            fullWidth = true,
        )
    }
    MaigLoadingOverlay(
        visible = showLoadingOverlay,
        message = "Sincronizando dados...",
    )
}
