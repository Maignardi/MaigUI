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
import io.github.maignardi.maigui.components.buttons.MaigButtonSize
import io.github.maignardi.maigui.components.buttons.MaigButtonVariant
import io.github.maignardi.maigui.components.display.MaigAccordion
import io.github.maignardi.maigui.components.display.MaigAvatar
import io.github.maignardi.maigui.components.display.MaigAvatarGroup
import io.github.maignardi.maigui.components.display.MaigAvatarSize
import io.github.maignardi.maigui.components.display.MaigAvatarStatus
import io.github.maignardi.maigui.components.display.MaigBadge
import io.github.maignardi.maigui.components.display.MaigBadgeColor
import io.github.maignardi.maigui.components.display.MaigBadgedBox
import io.github.maignardi.maigui.components.display.MaigCard
import io.github.maignardi.maigui.components.display.MaigCardVariant
import io.github.maignardi.maigui.components.display.MaigDivider
import io.github.maignardi.maigui.components.display.MaigEmptyState
import io.github.maignardi.maigui.components.display.MaigListItem
import io.github.maignardi.maigui.components.display.MaigStatCard
import io.github.maignardi.maigui.components.display.MaigStatTrend
import io.github.maignardi.maigui.components.display.MaigTag
import io.github.maignardi.maigui.components.display.MaigTagColor
import io.github.maignardi.maigui.components.display.MaigTagVariant
import io.github.maignardi.maigui.components.display.MaigTimeline
import io.github.maignardi.maigui.components.display.MaigTimelineItem
import io.github.maignardi.maigui.components.display.MaigTimelineStatus
import io.github.maignardi.maigui.components.feedback.LocalMaigToastState
import io.github.maignardi.maigui.components.icons.MaigIcons
import io.github.maignardi.maigui.components.layout.MaigSection
import io.github.maignardi.maigui.components.navigation.MaigBreadcrumb
import io.github.maignardi.maigui.components.navigation.MaigPagination
import io.github.maignardi.maigui.components.navigation.MaigSegmentedControl
import io.github.maignardi.maigui.components.navigation.MaigStepper
import io.github.maignardi.maigui.components.navigation.MaigTabVariant
import io.github.maignardi.maigui.components.navigation.MaigTabs
import io.github.maignardi.maigui.core.theme.MaigTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun DisplayNavigationShowcaseScreen() {
    val toast = LocalMaigToastState.current
    var accordion1 by remember { mutableStateOf(true) }
    var accordion2 by remember { mutableStateOf(false) }
    var subTabIndex by remember { mutableIntStateOf(0) }
    var pillTabIndex by remember { mutableIntStateOf(1) }
    var segmentedIndex by remember { mutableIntStateOf(0) }
    var currentStep by remember { mutableIntStateOf(1) }
    var currentPage by remember { mutableIntStateOf(3) }

    MaigSection(title = "StatCard, Timeline & Breadcrumb") {
        MaigBreadcrumb(
            items = listOf("Início", "Catálogo", "Exibição"),
            onItemClick = { toast.showInfo("Breadcrumb #$it") },
        )
        MaigStatCard(
            label = "Usuários Ativos",
            value = "24.890",
            delta = "+21,6%",
            trend = MaigStatTrend.Positive,
            subtitle = "Últimos 30 dias",
            icon = { MaigIcons.Plus() },
            onClick = { toast.showInfo("StatCard clicado") },
            modifier = Modifier.fillMaxWidth(),
        )
        MaigCard(variant = MaigCardVariant.Outlined, modifier = Modifier.fillMaxWidth()) {
            MaigTimeline(
                items = listOf(
                    MaigTimelineItem("Design System", "Tokens OKLCH + Inter", "00:15", MaigTimelineStatus.Completed),
                    MaigTimelineItem("Componentes Modernos", "45+ componentes prontos", "Agora", MaigTimelineStatus.Active),
                    MaigTimelineItem("Publicação", "Maven Central & JitPack", null, MaigTimelineStatus.Pending),
                ),
            )
        }
    }

    MaigSection(title = "Cards (Elevated, Outlined, Filled, Clicável)") {
        MaigCard(variant = MaigCardVariant.Elevated, modifier = Modifier.fillMaxWidth()) {
            BasicText("MaigCard Elevated", style = MaigTheme.typography.title.copy(color = MaigTheme.colors.textPrimary))
            BasicText("Elevação e borda sutil", style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary))
        }
        MaigCard(variant = MaigCardVariant.Outlined, modifier = Modifier.fillMaxWidth()) {
            BasicText("MaigCard Outlined", style = MaigTheme.typography.title.copy(color = MaigTheme.colors.textPrimary))
            BasicText("Borda limpa 1dp", style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary))
        }
        MaigCard(
            onClick = { toast.showInfo("Card clicado!") },
            variant = MaigCardVariant.Filled,
            modifier = Modifier.fillMaxWidth(),
        ) {
            BasicText("MaigCard Filled Clicável", style = MaigTheme.typography.title.copy(color = MaigTheme.colors.textPrimary))
            BasicText("Toque para sentir o ripple", style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary))
        }
    }

    MaigSection(title = "Avatars, Badges & Tags") {
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MaigAvatar(name = "Matheus Maignardi", size = MaigAvatarSize.Lg, status = MaigAvatarStatus.Online)
            MaigAvatar(name = "Ana Silva", size = MaigAvatarSize.Md, status = MaigAvatarStatus.Away)
            MaigAvatarGroup(names = listOf("Ana", "Bruno", "Carlos", "Diana", "Eduardo", "Fernanda"))
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigBadgedBox(badge = { MaigBadge(text = "8") }) {
                MaigButton(text = "Mensagens", onClick = {}, size = MaigButtonSize.Small, variant = MaigButtonVariant.Outline)
            }
            MaigTag(text = "Online", color = MaigTagColor.Success, showDot = true)
            MaigTag(text = "Novo", color = MaigTagColor.Primary, variant = MaigTagVariant.Solid)
            MaigTag(text = "Beta", color = MaigTagColor.Warning, variant = MaigTagVariant.Outline)
            MaigTag(text = "Removível", color = MaigTagColor.Info, onRemove = { toast.showInfo("Tag removida") })
        }
    }

    MaigSection(title = "ListItem, Divider, Accordion & EmptyState") {
        MaigCard(variant = MaigCardVariant.Outlined, modifier = Modifier.fillMaxWidth()) {
            MaigListItem(
                headline = "Matheus Maignardi",
                supportingText = "Android Engineer • Jetpack Compose",
                leadingContent = { MaigAvatar(name = "Matheus Maignardi") },
                trailingContent = { MaigIcons.ChevronRight() },
                onClick = { toast.showInfo("Item 1 clicado") },
                showDivider = true,
            )
            MaigListItem(
                overlineText = "SISTEMA",
                headline = "Tema & Tokens",
                supportingText = "WCAG AA automático",
                trailingContent = { MaigTag(text = "Ativo", color = MaigTagColor.Success) },
                onClick = { toast.showInfo("Item 2 clicado") },
            )
        }

        MaigDivider(label = "PERGUNTAS FREQUENTES")

        MaigAccordion(
            title = "O que é o MaigUI?",
            subtitle = "Design system Bootstrap para Jetpack Compose",
            expanded = accordion1,
            onExpandedChange = { accordion1 = it },
        ) {
            BasicText(
                text = "Uma biblioteca completa com tema dinâmico, componentes Foundation e formulários brasileiros.",
                style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary),
            )
        }
        MaigAccordion(
            title = "Precisa de Material 3 para aparência?",
            expanded = accordion2,
            onExpandedChange = { accordion2 = it },
        ) {
            BasicText(
                text = "Não! Todos os visuais são desenhados diretamente sobre androidx.compose.foundation.",
                style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary),
            )
        }

        MaigEmptyState(
            title = "Nenhum item salvo ainda",
            description = "Explore o catálogo ou crie seu primeiro componente.",
            action = {
                MaigButton(text = "Recarregar", onClick = { toast.showSuccess("Atualizado!") }, size = MaigButtonSize.Small)
            },
        )
    }

    MaigSection(title = "SegmentedControl, Tabs, Stepper & Pagination") {
        MaigSegmentedControl(
            options = listOf("Visão", "Métricas", "Logs"),
            selectedIndex = segmentedIndex,
            onOptionSelected = { segmentedIndex = it },
        )
        MaigTabs(
            tabs = listOf("Visão Geral", "Exemplos", "Propriedades"),
            selectedIndex = subTabIndex,
            onTabSelected = { subTabIndex = it },
        )
        MaigTabs(
            tabs = listOf("Compacto", "Confortável"),
            selectedIndex = pillTabIndex,
            onTabSelected = { pillTabIndex = it },
            variant = MaigTabVariant.Pill,
        )
        MaigStepper(
            steps = listOf("Conta", "Dados", "Endereço", "Fim"),
            currentStep = currentStep,
            onStepClick = { currentStep = it },
        )
        Row(horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm)) {
            MaigButton(
                text = "Anterior",
                onClick = { currentStep = (currentStep - 1).coerceAtLeast(0) },
                variant = MaigButtonVariant.Outline,
                size = MaigButtonSize.Small,
            )
            MaigButton(
                text = "Próximo passo",
                onClick = { currentStep = (currentStep + 1).coerceAtMost(3) },
                size = MaigButtonSize.Small,
            )
        }
        MaigPagination(
            currentPage = currentPage,
            totalPages = 12,
            onPageChange = { currentPage = it },
        )
    }
}
