package io.github.maignardi.maigui.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.maignardi.maigui.components.buttons.MaigButton
import io.github.maignardi.maigui.components.buttons.MaigButtonColor
import io.github.maignardi.maigui.components.buttons.MaigButtonGroup
import io.github.maignardi.maigui.components.buttons.MaigButtonSize
import io.github.maignardi.maigui.components.buttons.MaigButtonVariant
import io.github.maignardi.maigui.components.buttons.MaigFab
import io.github.maignardi.maigui.components.buttons.MaigFabSize
import io.github.maignardi.maigui.components.buttons.MaigIconButton
import io.github.maignardi.maigui.components.display.MaigCard
import io.github.maignardi.maigui.components.display.MaigCardVariant
import io.github.maignardi.maigui.components.feedback.LocalMaigToastState
import io.github.maignardi.maigui.components.icons.MaigIcons
import io.github.maignardi.maigui.components.layout.MaigSection
import io.github.maignardi.maigui.components.selection.MaigSwitch
import io.github.maignardi.maigui.core.theme.MaigTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ButtonsShowcaseScreen() {
    val toast = LocalMaigToastState.current
    var loadingDemo by remember { mutableStateOf(false) }
    var enabledDemo by remember { mutableStateOf(true) }
    var groupSelected by remember { mutableIntStateOf(1) }

    MaigSection(title = "Controles de Estado") {
        MaigCard(variant = MaigCardVariant.Outlined, modifier = Modifier.fillMaxWidth()) {
            MaigSwitch(checked = enabledDemo, onCheckedChange = { enabledDemo = it }, label = "Enabled")
            MaigSwitch(checked = loadingDemo, onCheckedChange = { loadingDemo = it }, label = "Loading Spinner")
        }
    }

    MaigSection(title = "Variantes (MaigButtonVariant)") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigButtonVariant.entries.forEach { variant ->
                MaigButton(
                    text = variant.name,
                    onClick = { toast.showInfo("Botão ${variant.name} clicado") },
                    variant = variant,
                    enabled = enabledDemo,
                    loading = loadingDemo,
                )
            }
        }
    }

    MaigSection(title = "Cores Semânticas (MaigButtonColor)") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigButtonColor.entries.forEach { color ->
                MaigButton(
                    text = color.name,
                    onClick = { toast.showSuccess("Cor ${color.name}") },
                    color = color,
                    enabled = enabledDemo,
                    loading = loadingDemo,
                )
            }
        }
    }

    MaigSection(title = "Tamanhos, Ícones e Full Width") {
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MaigButtonSize.entries.forEach { size ->
                MaigButton(
                    text = size.name,
                    onClick = {},
                    size = size,
                    enabled = enabledDemo,
                )
            }
        }
        MaigButton(
            text = "Botão Full Width com Ícone",
            onClick = { toast.showInfo("Ação Full Width") },
            fullWidth = true,
            enabled = enabledDemo,
            loading = loadingDemo,
            leadingIcon = { MaigIcons.Plus() },
        )
    }

    MaigSection(title = "IconButtons, FABs e ButtonGroup") {
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MaigIconButton(
                onClick = {},
                contentDescription = "Adicionar",
                variant = MaigButtonVariant.Solid,
                color = MaigButtonColor.Primary,
                enabled = enabledDemo,
            ) { MaigIcons.Plus() }

            MaigIconButton(
                onClick = {},
                contentDescription = "Buscar",
                variant = MaigButtonVariant.Outline,
                color = MaigButtonColor.Primary,
                enabled = enabledDemo,
            ) { MaigIcons.Search() }

            MaigFab(
                onClick = {},
                size = MaigFabSize.Small,
                contentDescription = "Novo",
                enabled = enabledDemo,
            ) { MaigIcons.Plus() }

            MaigFab(
                onClick = {},
                label = "Criar",
                enabled = enabledDemo,
            ) { MaigIcons.Plus() }
        }

        MaigButtonGroup(
            options = listOf("Dia", "Semana", "Mês", "Ano"),
            selectedIndex = groupSelected,
            onOptionSelected = { groupSelected = it },
            enabled = enabledDemo,
            modifier = Modifier.fillMaxWidth(),
        )
        BasicText(
            text = "Selecionado no grupo: índice $groupSelected",
            style = MaigTheme.typography.caption.copy(color = MaigTheme.colors.textSecondary),
        )
    }
}
