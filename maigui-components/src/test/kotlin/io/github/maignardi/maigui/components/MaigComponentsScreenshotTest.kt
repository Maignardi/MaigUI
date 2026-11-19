package io.github.maignardi.maigui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import com.github.takahirom.roborazzi.captureRoboImage
import io.github.maignardi.maigui.components.buttons.MaigButton
import io.github.maignardi.maigui.components.buttons.MaigButtonColor
import io.github.maignardi.maigui.components.buttons.MaigButtonGroup
import io.github.maignardi.maigui.components.buttons.MaigButtonSize
import io.github.maignardi.maigui.components.buttons.MaigButtonVariant
import io.github.maignardi.maigui.components.buttons.MaigFab
import io.github.maignardi.maigui.components.buttons.MaigIconButton
import io.github.maignardi.maigui.components.display.MaigAccordion
import io.github.maignardi.maigui.components.display.MaigAvatar
import io.github.maignardi.maigui.components.display.MaigAvatarGroup
import io.github.maignardi.maigui.components.display.MaigAvatarStatus
import io.github.maignardi.maigui.components.display.MaigBadge
import io.github.maignardi.maigui.components.display.MaigBadgeColor
import io.github.maignardi.maigui.components.display.MaigBadgedBox
import io.github.maignardi.maigui.components.display.MaigCard
import io.github.maignardi.maigui.components.display.MaigCardVariant
import io.github.maignardi.maigui.components.display.MaigDivider
import io.github.maignardi.maigui.components.display.MaigEmptyState
import io.github.maignardi.maigui.components.display.MaigListItem
import io.github.maignardi.maigui.components.display.MaigTag
import io.github.maignardi.maigui.components.display.MaigTagColor
import io.github.maignardi.maigui.components.display.MaigTagVariant
import io.github.maignardi.maigui.components.feedback.MaigAlert
import io.github.maignardi.maigui.components.feedback.MaigAlertType
import io.github.maignardi.maigui.components.feedback.MaigCircularProgressBar
import io.github.maignardi.maigui.components.feedback.MaigLinearProgressBar
import io.github.maignardi.maigui.components.feedback.MaigSkeletonCard
import io.github.maignardi.maigui.components.feedback.MaigToastBanner
import io.github.maignardi.maigui.components.feedback.MaigToastData
import io.github.maignardi.maigui.components.feedback.MaigToastType
import io.github.maignardi.maigui.components.inputs.MaigMoneyField
import io.github.maignardi.maigui.components.inputs.MaigOtpField
import io.github.maignardi.maigui.components.inputs.MaigPasswordField
import io.github.maignardi.maigui.components.inputs.MaigPhoneField
import io.github.maignardi.maigui.components.inputs.MaigSearchField
import io.github.maignardi.maigui.components.inputs.MaigTextArea
import io.github.maignardi.maigui.components.inputs.MaigTextField
import io.github.maignardi.maigui.components.inputs.MaigTextFieldVariant
import io.github.maignardi.maigui.components.internal.MaigArrowBackIcon
import io.github.maignardi.maigui.components.internal.MaigPlusIcon
import io.github.maignardi.maigui.components.internal.MaigSearchIcon
import io.github.maignardi.maigui.components.navigation.MaigBottomNavItem
import io.github.maignardi.maigui.components.navigation.MaigBottomNavigation
import io.github.maignardi.maigui.components.navigation.MaigPagination
import io.github.maignardi.maigui.components.navigation.MaigStepper
import io.github.maignardi.maigui.components.navigation.MaigTabVariant
import io.github.maignardi.maigui.components.navigation.MaigTabs
import io.github.maignardi.maigui.components.navigation.MaigTopBar
import io.github.maignardi.maigui.components.overlays.MaigBottomSheetSurface
import io.github.maignardi.maigui.components.overlays.MaigDialogSurface
import io.github.maignardi.maigui.components.overlays.MaigMenuDivider
import io.github.maignardi.maigui.components.overlays.MaigMenuItem
import io.github.maignardi.maigui.components.overlays.MaigMenuSurface
import io.github.maignardi.maigui.components.overlays.MaigTooltipBubble
import io.github.maignardi.maigui.components.selection.MaigCheckbox
import io.github.maignardi.maigui.components.selection.MaigChip
import io.github.maignardi.maigui.components.selection.MaigChipVariant
import io.github.maignardi.maigui.components.selection.MaigDate
import io.github.maignardi.maigui.components.selection.MaigDatePicker
import io.github.maignardi.maigui.components.selection.MaigRadioGroup
import io.github.maignardi.maigui.components.selection.MaigRangeSlider
import io.github.maignardi.maigui.components.selection.MaigSelect
import io.github.maignardi.maigui.components.selection.MaigSlider
import io.github.maignardi.maigui.components.selection.MaigSwitch
import io.github.maignardi.maigui.components.selection.MaigTime
import io.github.maignardi.maigui.components.selection.MaigTimePicker
import io.github.maignardi.maigui.components.selection.MaigTriStateCheckbox
import io.github.maignardi.maigui.core.theme.MaigTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "w420dp-h1500dp")
class MaigComponentsScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun buttons_light() = captureSnapshot("buttons_light", darkTheme = false) { ButtonsShowcase() }

    @Test
    fun buttons_dark() = captureSnapshot("buttons_dark", darkTheme = true) { ButtonsShowcase() }

    @Test
    fun inputs_light() = captureSnapshot("inputs_light", darkTheme = false) { InputsShowcase() }

    @Test
    fun inputs_dark() = captureSnapshot("inputs_dark", darkTheme = true) { InputsShowcase() }

    @Test
    fun selection_light() = captureSnapshot("selection_light", darkTheme = false) { SelectionShowcase() }

    @Test
    fun selection_dark() = captureSnapshot("selection_dark", darkTheme = true) { SelectionShowcase() }

    @Test
    fun overlays_feedback_light() = captureSnapshot("overlays_feedback_light", darkTheme = false) { OverlaysFeedbackShowcase() }

    @Test
    fun overlays_feedback_dark() = captureSnapshot("overlays_feedback_dark", darkTheme = true) { OverlaysFeedbackShowcase() }

    @Test
    fun display_navigation_light() = captureSnapshot("display_navigation_light", darkTheme = false) { DisplayNavigationShowcase() }

    @Test
    fun display_navigation_dark() = captureSnapshot("display_navigation_dark", darkTheme = true) { DisplayNavigationShowcase() }

    private fun captureSnapshot(
        name: String,
        darkTheme: Boolean,
        content: @Composable () -> Unit,
    ) {
        composeRule.setContent {
            MaigTheme(darkTheme = darkTheme) {
                Box(
                    modifier = Modifier
                        .width(410.dp)
                        .background(MaigTheme.colors.background)
                        .padding(MaigTheme.spacing.lg),
                ) {
                    content()
                }
            }
        }
        composeRule.onRoot().captureRoboImage(
            filePath = "src/test/snapshots/$name.png",
        )
    }

    @Composable
    private fun ButtonsShowcase() {
        Column(verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md)) {
            Row(horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm)) {
                MaigButton(text = "Solid", onClick = {}, variant = MaigButtonVariant.Solid)
                MaigButton(text = "Outline", onClick = {}, variant = MaigButtonVariant.Outline)
                MaigButton(text = "Soft", onClick = {}, variant = MaigButtonVariant.Soft)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm)) {
                MaigButton(text = "Ghost", onClick = {}, variant = MaigButtonVariant.Ghost)
                MaigButton(text = "Link", onClick = {}, variant = MaigButtonVariant.Link)
                MaigButton(text = "Success", onClick = {}, color = MaigButtonColor.Success)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MaigButton(text = "Danger", onClick = {}, color = MaigButtonColor.Danger, size = MaigButtonSize.Small)
                MaigButton(text = "Disabled", onClick = {}, enabled = false, size = MaigButtonSize.Small)
                MaigIconButton(
                    onClick = {},
                    contentDescription = "Add",
                    variant = MaigButtonVariant.Solid,
                    color = MaigButtonColor.Primary,
                ) { MaigPlusIcon() }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MaigFab(onClick = {}, contentDescription = "Add") { MaigPlusIcon() }
                MaigFab(onClick = {}, label = "Novo item") { MaigPlusIcon() }
            }
            MaigButtonGroup(
                options = listOf("Dia", "Semana", "Mês"),
                selectedIndex = 1,
                onOptionSelected = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    @Composable
    private fun InputsShowcase() {
        Column(verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md)) {
            MaigSearchField(value = "MaigUI", onValueChange = {})
            MaigTextField(
                value = "dev@maigui.io",
                onValueChange = {},
                label = "E-mail",
                helperText = "Campo obrigatório",
            )
            MaigTextField(
                value = "abc",
                onValueChange = {},
                label = "Filled com erro",
                errorText = "Valor inválido",
                variant = MaigTextFieldVariant.Filled,
                maxLength = 20,
                showCounter = true,
            )
            MaigPasswordField(value = "12345678", onValueChange = {})
            MaigMoneyField(value = "125990", onValueChange = {})
            MaigPhoneField(value = "11987654321", onValueChange = {})
            MaigOtpField(value = "4821", onValueChange = {}, length = 4, label = "Código OTP")
            MaigTextArea(
                value = "Design system em Jetpack Compose.",
                onValueChange = {},
                label = "Notas",
                minLines = 2,
            )
        }
    }

    @Composable
    private fun SelectionShowcase() {
        Column(verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md)) {
            MaigCheckbox(checked = true, onCheckedChange = {}, label = "Aceito os termos")
            MaigTriStateCheckbox(state = ToggleableState.Indeterminate, onClick = {}, label = "Indeterminado")
            MaigSwitch(checked = true, onCheckedChange = {}, label = "Notificações")
            MaigRadioGroup(
                options = listOf("Pix", "Cartão", "Boleto"),
                selectedOption = "Pix",
                onOptionSelected = {},
                optionLabel = { it },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm)) {
                MaigChip(label = "Kotlin", selected = true, onClick = {}, variant = MaigChipVariant.Filter)
                MaigChip(label = "Compose", selected = false, onClick = {}, variant = MaigChipVariant.Choice)
                MaigChip(label = "Tag", selected = false, onClick = {}, variant = MaigChipVariant.Input, onRemove = {})
            }
            MaigSelect(
                items = listOf("São Paulo", "Rio de Janeiro"),
                selectedItem = "São Paulo",
                onItemSelected = {},
                itemLabel = { it },
                label = "Cidade",
            )
            MaigSlider(value = 0.65f, onValueChange = {}, label = "Volume", valueFormatter = { "65%" })
            MaigRangeSlider(value = 20f..80f, onValueChange = {}, valueRange = 0f..100f, label = "Faixa")
            MaigDatePicker(selectedDate = MaigDate(2026, 11, 19), onDateSelected = {})
            MaigTimePicker(selectedTime = MaigTime(14, 30), onTimeSelected = {})
        }
    }

    @Composable
    private fun OverlaysFeedbackShowcase() {
        Column(verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md)) {
            MaigAlert(
                title = "Publicado com sucesso",
                description = "Seus tokens já estão ativos.",
                type = MaigAlertType.Success,
            )
            MaigAlert(
                title = "Atenção",
                description = "Verifique os campos antes de salvar.",
                type = MaigAlertType.Warning,
            )
            MaigToastBanner(
                data = MaigToastData("Item salvo!", MaigToastType.Success, "Desfazer", {}),
                onDismiss = {},
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.lg),
            ) {
                MaigCircularProgressBar(progress = 0.75f, showPercentage = true)
                Box(modifier = Modifier.weight(1f)) {
                    MaigLinearProgressBar(progress = 0.72f, showPercentage = true)
                }
            }
            MaigSkeletonCard(animated = false)
            MaigDialogSurface(
                title = "Confirmar ação",
                message = "Deseja aplicar as alterações?",
                confirmButton = { MaigButton(text = "Confirmar", onClick = {}, size = MaigButtonSize.Small) },
            )
            MaigMenuSurface {
                MaigMenuItem(text = "Editar", onClick = {})
                MaigMenuDivider()
                MaigMenuItem(text = "Excluir", onClick = {}, destructive = true)
            }
            MaigTooltipBubble(text = "Tooltip informativo")
            MaigBottomSheetSurface(title = "Bottom Sheet") {
                BasicText("Ação rápida", style = MaigTheme.typography.body.copy(color = MaigTheme.colors.textPrimary))
            }
        }
    }

    @Composable
    private fun DisplayNavigationShowcase() {
        Column(verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md)) {
            MaigTopBar(
                title = "MaigUI Catalog",
                navigationIcon = { MaigIconButton(onClick = {}, contentDescription = "Voltar") { MaigArrowBackIcon() } },
                actions = { MaigIconButton(onClick = {}, contentDescription = "Buscar") { MaigSearchIcon() } },
            )
            MaigTabs(tabs = listOf("Visão", "Componentes", "Forms"), selectedIndex = 1, onTabSelected = {})
            MaigTabs(tabs = listOf("Light", "Dark"), selectedIndex = 0, onTabSelected = {}, variant = MaigTabVariant.Pill)
            MaigStepper(steps = listOf("Tema", "UI", "Forms"), currentStep = 1)
            MaigCard(modifier = Modifier.fillMaxWidth(), variant = MaigCardVariant.Elevated) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
                ) {
                    MaigAvatar(name = "Matheus Maignardi", status = MaigAvatarStatus.Online)
                    Column(modifier = Modifier.weight(1f)) {
                        BasicText("MaigCard Elevated", style = MaigTheme.typography.title.copy(color = MaigTheme.colors.textPrimary))
                        BasicText("Com avatar e tags", style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary))
                    }
                    MaigTag(text = "v0.1.0", color = MaigTagColor.Success, variant = MaigTagVariant.Soft, showDot = true)
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
            ) {
                MaigAvatarGroup(names = listOf("Ana", "Bruno", "Carlos", "Diana", "Eduardo"))
                MaigBadgedBox(badge = { MaigBadge(text = "5", color = MaigBadgeColor.Danger) }) {
                    MaigButton(text = "Inbox", onClick = {}, size = MaigButtonSize.Small)
                }
            }
            MaigDivider(label = "LISTA")
            MaigListItem(
                headline = "Configurações de Tema",
                supportingText = "WCAG AA garantido",
                onClick = {},
            )
            MaigAccordion(title = "Accordion expandido", expanded = true, onExpandedChange = {}) {
                BasicText("Conteúdo interno", style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary))
            }
            MaigPagination(currentPage = 3, totalPages = 8, onPageChange = {})
            MaigEmptyState(title = "Sem notificações", description = "Tudo em dia por aqui.")
            MaigBottomNavigation(
                items = listOf(
                    MaigBottomNavItem("Home") { MaigPlusIcon() },
                    MaigBottomNavItem("Buscar", badgeCount = 3) { MaigSearchIcon() },
                ),
                selectedIndex = 0,
                onItemSelected = {},
            )
        }
    }
}
