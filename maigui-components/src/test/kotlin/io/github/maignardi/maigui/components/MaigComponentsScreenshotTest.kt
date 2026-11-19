package io.github.maignardi.maigui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import io.github.maignardi.maigui.components.display.MaigAvatarSize
import io.github.maignardi.maigui.components.display.MaigAvatarStatus
import io.github.maignardi.maigui.components.display.MaigBadge
import io.github.maignardi.maigui.components.display.MaigBadgeColor
import io.github.maignardi.maigui.components.display.MaigBadgedBox
import io.github.maignardi.maigui.components.display.MaigCard
import io.github.maignardi.maigui.components.display.MaigCardVariant
import io.github.maignardi.maigui.components.display.MaigDivider
import io.github.maignardi.maigui.components.display.MaigEmptyState
import io.github.maignardi.maigui.components.display.MaigKeyValueRow
import io.github.maignardi.maigui.components.display.MaigListItem
import io.github.maignardi.maigui.components.display.MaigStatCard
import io.github.maignardi.maigui.components.display.MaigStatTrend
import io.github.maignardi.maigui.components.display.MaigTag
import io.github.maignardi.maigui.components.display.MaigTagColor
import io.github.maignardi.maigui.components.display.MaigTagVariant
import io.github.maignardi.maigui.components.display.MaigTimeline
import io.github.maignardi.maigui.components.display.MaigTimelineItem
import io.github.maignardi.maigui.components.display.MaigTimelineStatus
import io.github.maignardi.maigui.components.feedback.MaigAlert
import io.github.maignardi.maigui.components.feedback.MaigAlertType
import io.github.maignardi.maigui.components.feedback.MaigCircularProgressBar
import io.github.maignardi.maigui.components.feedback.MaigLinearProgressBar
import io.github.maignardi.maigui.components.feedback.MaigSkeletonCard
import io.github.maignardi.maigui.components.feedback.MaigToastBanner
import io.github.maignardi.maigui.components.feedback.MaigToastData
import io.github.maignardi.maigui.components.feedback.MaigToastType
import io.github.maignardi.maigui.components.icons.MaigIcons
import io.github.maignardi.maigui.components.inputs.MaigMoneyField
import io.github.maignardi.maigui.components.inputs.MaigNumberStepper
import io.github.maignardi.maigui.components.inputs.MaigOtpField
import io.github.maignardi.maigui.components.inputs.MaigPasswordField
import io.github.maignardi.maigui.components.inputs.MaigPhoneField
import io.github.maignardi.maigui.components.inputs.MaigSearchField
import io.github.maignardi.maigui.components.inputs.MaigTextArea
import io.github.maignardi.maigui.components.inputs.MaigTextField
import io.github.maignardi.maigui.components.inputs.MaigTextFieldVariant
import io.github.maignardi.maigui.components.navigation.MaigBottomNavItem
import io.github.maignardi.maigui.components.navigation.MaigBottomNavigation
import io.github.maignardi.maigui.components.navigation.MaigBreadcrumb
import io.github.maignardi.maigui.components.navigation.MaigPagination
import io.github.maignardi.maigui.components.navigation.MaigSegmentedControl
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
import io.github.maignardi.maigui.components.selection.MaigRating
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
@Config(qualifiers = "w440dp-h1850dp")
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

    @Test
    fun modern_extras_light() = captureSnapshot("modern_extras_light", darkTheme = false) { ModernExtrasShowcase() }

    @Test
    fun modern_extras_dark() = captureSnapshot("modern_extras_dark", darkTheme = true) { ModernExtrasShowcase() }

    private fun captureSnapshot(
        name: String,
        darkTheme: Boolean,
        content: @Composable () -> Unit,
    ) {
        composeRule.setContent {
            MaigTheme(darkTheme = darkTheme) {
                Box(
                    modifier = Modifier
                        .width(420.dp)
                        .background(MaigTheme.colors.background)
                        .padding(20.dp),
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
    private fun ShowcaseHeader(
        title: String,
        subtitle: String,
    ) {
        val isDark = MaigTheme.colors.isDark
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                BasicText(
                    text = "MAIGUI DESIGN SYSTEM",
                    style = MaigTheme.typography.caption.copy(
                        color = MaigTheme.colors.primaryDefault,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp,
                        fontSize = 10.sp,
                    ),
                )
                BasicText(
                    text = title,
                    style = MaigTheme.typography.headline.copy(
                        color = MaigTheme.colors.textPrimary,
                        fontSize = 20.sp,
                    ),
                )
                BasicText(
                    text = subtitle,
                    style = MaigTheme.typography.caption.copy(
                        color = MaigTheme.colors.textSecondary,
                    ),
                )
            }
            MaigTag(
                text = if (isDark) "Dark" else "Light",
                color = if (isDark) MaigTagColor.Primary else MaigTagColor.Neutral,
                variant = MaigTagVariant.Soft,
                showDot = true,
            )
        }
    }

    @Composable
    private fun SectionLabel(text: String) {
        BasicText(
            text = text,
            style = MaigTheme.typography.caption.copy(
                color = MaigTheme.colors.textTertiary,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.8.sp,
                fontSize = 10.sp,
            ),
        )
    }

    @Composable
    private fun TintedIconBadge(
        background: Color,
        tint: Color,
        icon: @Composable () -> Unit,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(MaigTheme.shapes.sm)
                .background(background),
            contentAlignment = Alignment.Center,
        ) {
            androidx.compose.runtime.CompositionLocalProvider(
                io.github.maignardi.maigui.core.theme.LocalMaigContentColor provides tint,
            ) {
                icon()
            }
        }
    }

    @Composable
    private fun ButtonsShowcase() {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ShowcaseHeader(
                title = "Buttons & Actions",
                subtitle = "5 variants • 6 intents • spring micro-interactions",
            )

            MaigCard(
                modifier = Modifier.fillMaxWidth(),
                variant = MaigCardVariant.Elevated,
                contentPadding = PaddingValues(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SectionLabel("VARIANTS")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        MaigButton(
                            text = "Solid",
                            onClick = {},
                            variant = MaigButtonVariant.Solid,
                            modifier = Modifier.weight(1f),
                            fullWidth = true,
                        )
                        MaigButton(
                            text = "Outline",
                            onClick = {},
                            variant = MaigButtonVariant.Outline,
                            modifier = Modifier.weight(1f),
                            fullWidth = true,
                        )
                        MaigButton(
                            text = "Soft",
                            onClick = {},
                            variant = MaigButtonVariant.Soft,
                            modifier = Modifier.weight(1f),
                            fullWidth = true,
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        MaigButton(
                            text = "Ghost",
                            onClick = {},
                            variant = MaigButtonVariant.Ghost,
                            modifier = Modifier.weight(1f),
                            fullWidth = true,
                        )
                        MaigButton(
                            text = "Link",
                            onClick = {},
                            variant = MaigButtonVariant.Link,
                            modifier = Modifier.weight(1f),
                            fullWidth = true,
                        )
                        MaigButton(
                            text = "Disabled",
                            onClick = {},
                            enabled = false,
                            modifier = Modifier.weight(1f),
                            fullWidth = true,
                        )
                    }
                }
            }

            MaigCard(
                modifier = Modifier.fillMaxWidth(),
                variant = MaigCardVariant.Elevated,
                contentPadding = PaddingValues(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SectionLabel("SEMANTIC INTENTS & CTA")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        MaigButton(
                            text = "Confirmar",
                            onClick = {},
                            color = MaigButtonColor.Success,
                            size = MaigButtonSize.Small,
                            leadingIcon = { MaigIcons.Check(size = 14.dp) },
                            modifier = Modifier.weight(1f),
                            fullWidth = true,
                        )
                        MaigButton(
                            text = "Excluir",
                            onClick = {},
                            color = MaigButtonColor.Danger,
                            size = MaigButtonSize.Small,
                            leadingIcon = { MaigIcons.Close(size = 14.dp) },
                            modifier = Modifier.weight(1f),
                            fullWidth = true,
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        MaigButton(
                            text = "Atenção",
                            onClick = {},
                            color = MaigButtonColor.Warning,
                            variant = MaigButtonVariant.Soft,
                            size = MaigButtonSize.Small,
                            modifier = Modifier.weight(1f),
                            fullWidth = true,
                        )
                        MaigButton(
                            text = "Secundário",
                            onClick = {},
                            color = MaigButtonColor.Neutral,
                            variant = MaigButtonVariant.Solid,
                            size = MaigButtonSize.Small,
                            modifier = Modifier.weight(1f),
                            fullWidth = true,
                        )
                    }
                    MaigButton(
                        text = "Continuar pagamento",
                        onClick = {},
                        size = MaigButtonSize.Large,
                        fullWidth = true,
                        trailingIcon = { MaigIcons.ChevronRight(size = 16.dp) },
                    )
                }
            }

            MaigCard(
                modifier = Modifier.fillMaxWidth(),
                variant = MaigCardVariant.Elevated,
                contentPadding = PaddingValues(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    SectionLabel("ICON BUTTONS, FAB & GROUP")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            MaigIconButton(
                                onClick = {},
                                contentDescription = "Buscar",
                                variant = MaigButtonVariant.Soft,
                                color = MaigButtonColor.Primary,
                            ) { MaigIcons.Search() }
                            MaigIconButton(
                                onClick = {},
                                contentDescription = "Notificações",
                                variant = MaigButtonVariant.Outline,
                                color = MaigButtonColor.Neutral,
                            ) { MaigIcons.Bell() }
                            MaigFab(onClick = {}, contentDescription = "Criar", size = MaigFabSizeSmall()) {
                                MaigIcons.Plus()
                            }
                        }
                        MaigFab(onClick = {}, label = "Novo projeto") {
                            MaigIcons.Plus()
                        }
                    }
                    MaigButtonGroup(
                        options = listOf("Dia", "Semana", "Mês"),
                        selectedIndex = 1,
                        onOptionSelected = {},
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }

    private fun MaigFabSizeSmall() = io.github.maignardi.maigui.components.buttons.MaigFabSize.Small

    @Composable
    private fun InputsShowcase() {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ShowcaseHeader(
                title = "Inputs & Forms",
                subtitle = "Outlined, Filled, masks, OTP & inline validation",
            )

            MaigSearchField(value = "MaigUI Design System", onValueChange = {})

            MaigCard(
                modifier = Modifier.fillMaxWidth(),
                variant = MaigCardVariant.Elevated,
                contentPadding = PaddingValues(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    SectionLabel("ACCOUNT CREDENTIALS")
                    MaigTextField(
                        value = "dev@maigui.io",
                        onValueChange = {},
                        label = "E-mail corporativo",
                        helperText = "Usado para faturamento e alertas",
                        leadingIcon = { MaigIcons.Mail(size = 18.dp) },
                    )
                    MaigPasswordField(
                        value = "12345678",
                        onValueChange = {},
                        label = "Senha de acesso",
                    )
                    MaigTextField(
                        value = "abc",
                        onValueChange = {},
                        label = "Código de convite (Filled)",
                        errorText = "Código inválido ou expirado",
                        variant = MaigTextFieldVariant.Filled,
                        maxLength = 20,
                        showCounter = true,
                    )
                }
            }

            MaigCard(
                modifier = Modifier.fillMaxWidth(),
                variant = MaigCardVariant.Elevated,
                contentPadding = PaddingValues(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    SectionLabel("LOCALIZED & VERIFICATION")
                    MaigMoneyField(value = "125990", onValueChange = {}, label = "Valor da transferência")
                    MaigPhoneField(value = "11987654321", onValueChange = {}, label = "Celular (WhatsApp)")
                    MaigOtpField(
                        value = "4821",
                        onValueChange = {},
                        length = 4,
                        label = "Código de verificação (2FA)",
                    )
                    MaigTextArea(
                        value = "Design system minimalista 100% Jetpack Compose Foundation.",
                        onValueChange = {},
                        label = "Observações",
                        minLines = 2,
                    )
                }
            }
        }
    }

    @Composable
    private fun SelectionShowcase() {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ShowcaseHeader(
                title = "Selection & Pickers",
                subtitle = "Toggles, chips, sliders & native date/time pickers",
            )

            MaigCard(
                modifier = Modifier.fillMaxWidth(),
                variant = MaigCardVariant.Elevated,
                contentPadding = PaddingValues(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SectionLabel("CONTROLS & CHIPS")
                    MaigSwitch(checked = true, onCheckedChange = {}, label = "Notificações em tempo real")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        MaigCheckbox(checked = true, onCheckedChange = {}, label = "Aceito termos")
                        MaigTriStateCheckbox(state = ToggleableState.Indeterminate, onClick = {}, label = "Parcial")
                    }
                    MaigDivider()
                    SectionLabel("MÉTODO DE PAGAMENTO")
                    MaigRadioGroup(
                        options = listOf("Pix instantâneo", "Cartão de crédito", "Boleto bancário"),
                        selectedOption = "Pix instantâneo",
                        onOptionSelected = {},
                        optionLabel = { it },
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        MaigChip(label = "Kotlin", selected = true, onClick = {}, variant = MaigChipVariant.Filter)
                        MaigChip(label = "Compose", selected = false, onClick = {}, variant = MaigChipVariant.Choice)
                        MaigChip(label = "v0.1.0", selected = false, onClick = {}, variant = MaigChipVariant.Input, onRemove = {})
                    }
                }
            }

            MaigCard(
                modifier = Modifier.fillMaxWidth(),
                variant = MaigCardVariant.Elevated,
                contentPadding = PaddingValues(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    SectionLabel("SELECT & SLIDERS")
                    MaigSelect(
                        items = listOf("São Paulo — SP", "Rio de Janeiro — RJ"),
                        selectedItem = "São Paulo — SP",
                        onItemSelected = {},
                        itemLabel = { it },
                        label = "Cidade",
                    )
                    MaigSlider(value = 0.65f, onValueChange = {}, label = "Volume de alertas", valueFormatter = { "65%" })
                    MaigRangeSlider(value = 20f..80f, onValueChange = {}, valueRange = 0f..100f, label = "Faixa de preço (R$)")
                }
            }

            MaigDatePicker(selectedDate = MaigDate(2026, 11, 19), onDateSelected = {})
            MaigTimePicker(selectedTime = MaigTime(14, 30), onTimeSelected = {})
        }
    }

    @Composable
    private fun OverlaysFeedbackShowcase() {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ShowcaseHeader(
                title = "Overlays & Feedback",
                subtitle = "Alerts, toasts, progress, skeletons, dialogs & sheets",
            )

            MaigAlert(
                title = "Publicado no Maven Central",
                description = "Artefatos assinados e sincronizados com sucesso.",
                type = MaigAlertType.Success,
            )
            MaigAlert(
                title = "Verificação pendente",
                description = "Confirme seu domínio antes de ativar webhooks.",
                type = MaigAlertType.Warning,
            )
            MaigToastBanner(
                data = MaigToastData("Configurações salvas!", MaigToastType.Success, "Desfazer", {}),
                onDismiss = {},
            )

            MaigCard(
                modifier = Modifier.fillMaxWidth(),
                variant = MaigCardVariant.Elevated,
                contentPadding = PaddingValues(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    SectionLabel("PROGRESS & LOADING")
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        MaigCircularProgressBar(progress = 0.78f, size = 54.dp, strokeWidth = 5.dp, showPercentage = true)
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            MaigLinearProgressBar(progress = 0.72f, showPercentage = true)
                            MaigLinearProgressBar(
                                progress = 0.94f,
                                color = MaigTheme.colors.successDefault,
                                showPercentage = true,
                            )
                        }
                    }
                    MaigSkeletonCard(animated = false)
                }
            }

            MaigDialogSurface(
                title = "Confirmar publicação?",
                message = "A versão 0.1.0 ficará imediatamente disponível para todos os projetos da organização.",
                icon = {
                    TintedIconBadge(
                        background = MaigTheme.colors.primarySubtle,
                        tint = MaigTheme.colors.primaryDefault,
                    ) { MaigIcons.Layers(size = 18.dp) }
                },
                dismissButton = {
                    MaigButton(
                        text = "Cancelar",
                        onClick = {},
                        variant = MaigButtonVariant.Ghost,
                        color = MaigButtonColor.Neutral,
                        size = MaigButtonSize.Small,
                    )
                },
                confirmButton = {
                    MaigButton(
                        text = "Publicar",
                        onClick = {},
                        size = MaigButtonSize.Small,
                    )
                },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                MaigMenuSurface(modifier = Modifier.weight(1f)) {
                    MaigMenuItem(
                        text = "Editar tema",
                        onClick = {},
                        leadingIcon = { MaigIcons.Palette(size = 16.dp) },
                    )
                    MaigMenuItem(
                        text = "Segurança",
                        onClick = {},
                        leadingIcon = { MaigIcons.Lock(size = 16.dp) },
                    )
                    MaigMenuDivider()
                    MaigMenuItem(
                        text = "Remover projeto",
                        onClick = {},
                        destructive = true,
                        leadingIcon = { MaigIcons.Close(size = 16.dp) },
                    )
                }
                Column(
                    modifier = Modifier.weight(0.75f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    SectionLabel("TOOLTIP")
                    MaigTooltipBubble(text = "WCAG AA >= 4.5:1")
                }
            }

            MaigBottomSheetSurface(title = "Ações rápidas") {
                MaigListItem(
                    headline = "Exportar tokens JSON",
                    supportingText = "Compatível com Figma Variables",
                    leadingContent = {
                        TintedIconBadge(
                            background = MaigTheme.colors.primarySubtle,
                            tint = MaigTheme.colors.primaryDefault,
                        ) { MaigIcons.Palette(size = 18.dp) }
                    },
                    trailingContent = { MaigIcons.ChevronRight() },
                    containerColor = Color.Transparent,
                    onClick = {},
                )
            }
        }
    }

    @Composable
    private fun DisplayNavigationShowcase() {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ShowcaseHeader(
                title = "Display & Navigation",
                subtitle = "Bars, tabs, steppers, cards, avatars, lists & nav",
            )

            MaigCard(
                modifier = Modifier.fillMaxWidth(),
                variant = MaigCardVariant.Outlined,
                contentPadding = PaddingValues(0.dp),
            ) {
                MaigTopBar(
                    title = "MaigUI Studio",
                    navigationIcon = { MaigIconButton(onClick = {}, contentDescription = "Voltar") { MaigIcons.ArrowBack() } },
                    actions = { MaigIconButton(onClick = {}, contentDescription = "Buscar") { MaigIcons.Search() } },
                )
                MaigTabs(tabs = listOf("Visão", "Componentes", "Forms"), selectedIndex = 1, onTabSelected = {})
            }

            MaigTabs(tabs = listOf("Light", "Dark"), selectedIndex = 0, onTabSelected = {}, variant = MaigTabVariant.Pill)

            MaigCard(
                modifier = Modifier.fillMaxWidth(),
                variant = MaigCardVariant.Elevated,
                contentPadding = PaddingValues(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    MaigStepper(steps = listOf("Tema", "UI", "Deploy"), currentStep = 1)
                    MaigDivider()
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        MaigAvatar(name = "Matheus Maignardi", size = MaigAvatarSize.Lg, status = MaigAvatarStatus.Online)
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            BasicText(
                                "Matheus Maignardi",
                                style = MaigTheme.typography.title.copy(color = MaigTheme.colors.textPrimary),
                            )
                            BasicText(
                                "Android Staff Engineer",
                                style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary),
                            )
                        }
                        MaigTag(text = "Pro", color = MaigTagColor.Success, variant = MaigTagVariant.Soft, showDot = true)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        MaigAvatarGroup(names = listOf("Ana", "Bruno", "Carlos", "Diana", "Eduardo"))
                        MaigBadgedBox(badge = { MaigBadge(text = "5", color = MaigBadgeColor.Danger) }) {
                            MaigButton(
                                text = "Inbox",
                                onClick = {},
                                size = MaigButtonSize.Small,
                                variant = MaigButtonVariant.Soft,
                                leadingIcon = { MaigIcons.Mail(size = 14.dp) },
                            )
                        }
                    }
                }
            }

            MaigCard(
                modifier = Modifier.fillMaxWidth(),
                variant = MaigCardVariant.Outlined,
                contentPadding = PaddingValues(0.dp),
            ) {
                MaigListItem(
                    headline = "Paleta & Contraste",
                    supportingText = "WCAG AA automático em Light & Dark",
                    leadingContent = {
                        TintedIconBadge(
                            background = MaigTheme.colors.primarySubtle,
                            tint = MaigTheme.colors.primaryDefault,
                        ) { MaigIcons.Palette(size = 18.dp) }
                    },
                    trailingContent = { MaigIcons.ChevronRight() },
                    showDivider = true,
                    onClick = {},
                )
                MaigListItem(
                    headline = "Autenticação 2FA",
                    supportingText = "Proteção por aplicativo autenticador",
                    leadingContent = {
                        TintedIconBadge(
                            background = MaigTheme.colors.successSubtle,
                            tint = MaigTheme.colors.successDefault,
                        ) { MaigIcons.Lock(size = 18.dp) }
                    },
                    trailingContent = { MaigTag(text = "Ativo", color = MaigTagColor.Success, variant = MaigTagVariant.Soft) },
                    onClick = {},
                )
            }

            MaigAccordion(title = "Como funciona o OKLCH?", expanded = true, onExpandedChange = {}) {
                BasicText(
                    "Gera 10 tons perceptualmente uniformes a partir de uma única cor da marca.",
                    style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary),
                )
            }

            MaigPagination(currentPage = 3, totalPages = 8, onPageChange = {})

            MaigCard(
                modifier = Modifier.fillMaxWidth(),
                variant = MaigCardVariant.Outlined,
                contentPadding = PaddingValues(0.dp),
            ) {
                MaigEmptyState(
                    title = "Nenhuma pendência",
                    description = "Todos os seus deployments estão saudáveis.",
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaigTheme.shapes.lg)
                    .border(1.dp, MaigTheme.colors.border, MaigTheme.shapes.lg),
            ) {
                MaigBottomNavigation(
                    items = listOf(
                        MaigBottomNavItem("Início") { MaigIcons.Home() },
                        MaigBottomNavItem("Buscar", badgeCount = 3) { MaigIcons.Search() },
                        MaigBottomNavItem("Alertas") { MaigIcons.Bell() },
                        MaigBottomNavItem("Perfil") { MaigIcons.User() },
                    ),
                    selectedIndex = 0,
                    onItemSelected = {},
                )
            }
        }
    }

    @Composable
    private fun ModernExtrasShowcase() {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ShowcaseHeader(
                title = "Dashboard & Commerce",
                subtitle = "KPI metrics, ratings, checkout summary & timeline",
            )

            MaigBreadcrumb(items = listOf("Início", "Analytics", "Receita"), onItemClick = {})
            MaigSegmentedControl(
                options = listOf("7 dias", "30 dias", "12 meses"),
                selectedIndex = 1,
                onOptionSelected = {},
            )

            MaigStatCard(
                label = "Receita Mensal Recorrente",
                value = "R$ 48.920",
                delta = "+18,4%",
                trend = MaigStatTrend.Positive,
                subtitle = "Comparado aos últimos 30 dias",
                icon = { MaigIcons.Layers(size = 18.dp) },
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                MaigStatCard(
                    label = "Conversão",
                    value = "4,8%",
                    delta = "+0,6%",
                    trend = MaigStatTrend.Positive,
                    modifier = Modifier.weight(1f),
                )
                MaigStatCard(
                    label = "Churn",
                    value = "1,2%",
                    delta = "-0,3%",
                    trend = MaigStatTrend.Neutral,
                    modifier = Modifier.weight(1f),
                )
            }

            MaigCard(
                modifier = Modifier.fillMaxWidth(),
                variant = MaigCardVariant.Elevated,
                contentPadding = PaddingValues(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SectionLabel("CHECKOUT SUMMARY")
                    MaigNumberStepper(value = 3, onValueChange = {}, label = "Licenças Enterprise")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        MaigRating(rating = 4, onRatingChange = {})
                        BasicText(
                            text = "4.0 • 128 avaliações",
                            style = MaigTheme.typography.caption.copy(color = MaigTheme.colors.textSecondary),
                        )
                    }
                    MaigDivider()
                    MaigKeyValueRow(label = "Subtotal", value = "R$ 149,90", showDivider = true)
                    MaigKeyValueRow(
                        label = "Cupom MAIG20",
                        value = "- R$ 20,00",
                        valueColor = MaigTheme.colors.successDefault,
                        showDivider = true,
                    )
                    MaigKeyValueRow(label = "Total", value = "R$ 129,90", emphasizeValue = true)
                    Spacer(modifier = Modifier.height(2.dp))
                    MaigButton(
                        text = "Finalizar pedido",
                        onClick = {},
                        fullWidth = true,
                    )
                }
            }

            MaigCard(
                modifier = Modifier.fillMaxWidth(),
                variant = MaigCardVariant.Elevated,
                contentPadding = PaddingValues(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionLabel("RASTREAMENTO EM TEMPO REAL")
                    MaigTimeline(
                        items = listOf(
                            MaigTimelineItem("Pedido confirmado", "Pagamento via Pix aprovado", "10:15", MaigTimelineStatus.Completed),
                            MaigTimelineItem("Em preparação", "Separando itens no centro de distribuição", "11:40", MaigTimelineStatus.Active),
                            MaigTimelineItem("Saiu para entrega", "Previsão: amanhã até 18h", null, MaigTimelineStatus.Pending),
                        ),
                    )
                }
            }
        }
    }
}
