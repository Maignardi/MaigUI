package io.github.maignardi.maigui.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.buttons.MaigIconButton
import io.github.maignardi.maigui.components.display.MaigDivider
import io.github.maignardi.maigui.components.display.MaigTag
import io.github.maignardi.maigui.components.display.MaigTagColor
import io.github.maignardi.maigui.components.feedback.rememberMaigToastState
import io.github.maignardi.maigui.components.icons.MaigIcons
import io.github.maignardi.maigui.components.layout.MaigScaffold
import io.github.maignardi.maigui.components.navigation.MaigBottomNavItem
import io.github.maignardi.maigui.components.navigation.MaigBottomNavigation
import io.github.maignardi.maigui.components.navigation.MaigModalNavigationDrawer
import io.github.maignardi.maigui.components.navigation.MaigNavigationDrawerItem
import io.github.maignardi.maigui.components.navigation.MaigTabs
import io.github.maignardi.maigui.components.navigation.MaigTopBar
import io.github.maignardi.maigui.core.theme.MaigBrand
import io.github.maignardi.maigui.core.theme.MaigTheme
import io.github.maignardi.maigui.core.theme.MaigThemes

@Composable
public fun MaigSampleApp() {
    var themeMode by remember { mutableStateOf(ThemeMode.System) }
    var selectedBrand by remember { mutableStateOf(MaigThemes.Default.brand) }
    var activePresetName by remember { mutableStateOf(MaigThemes.Default.name) }
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var drawerOpen by remember { mutableStateOf(false) }

    val isDark = when (themeMode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }

    val categories = CatalogCategory.entries
    val activeCategory = categories[selectedCategoryIndex]

    val customSeeds = remember {
        listOf(
            "Rose" to Color(0xFFE11D48),
            "Teal" to Color(0xFF0D9488),
            "Indigo" to Color(0xFF4F46E5),
            "Cyan" to Color(0xFF0891B2),
        )
    }

    MaigTheme(
        brand = selectedBrand,
        darkTheme = isDark,
    ) {
        val toastState = rememberMaigToastState()
        val colors = MaigTheme.colors

        MaigModalNavigationDrawer(
            drawerOpen = drawerOpen,
            onDrawerOpenChange = { drawerOpen = it },
            drawerContent = {
                BasicText(
                    text = "MaigUI Catalog",
                    modifier = Modifier.padding(MaigTheme.spacing.md),
                    style = MaigTheme.typography.headline.copy(color = colors.primaryDefault),
                )
                MaigDivider()
                categories.forEachIndexed { index, category ->
                    MaigNavigationDrawerItem(
                        label = category.title,
                        selected = index == selectedCategoryIndex,
                        onClick = {
                            selectedCategoryIndex = index
                            drawerOpen = false
                        },
                    )
                }
            },
        ) {
            MaigScaffold(
                toastState = toastState,
                topBar = {
                    Column {
                        MaigTopBar(
                            title = "MaigUI Catalog",
                            subtitle = "Theme: $activePresetName • ${if (isDark) "Dark" else "Light"}",
                            navigationIcon = {
                                MaigIconButton(
                                    onClick = { drawerOpen = true },
                                    contentDescription = "Abrir menu",
                                ) { MaigIcons.Menu() }
                            },
                            actions = {
                                MaigTag(
                                    text = if (isDark) "Dark" else "Light",
                                    color = MaigTagColor.Primary,
                                    modifier = Modifier
                                        .clickable {
                                            themeMode = if (isDark) ThemeMode.Light else ThemeMode.Dark
                                        }
                                        .padding(end = MaigTheme.spacing.sm),
                                )
                            },
                        )

                        // Quick live brand color bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colors.backgroundSubtle)
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = MaigTheme.spacing.lg, vertical = MaigTheme.spacing.xs),
                            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            MaigThemes.presets.forEach { preset ->
                                BrandDot(
                                    name = preset.name,
                                    color = preset.brand.primary,
                                    selected = activePresetName == preset.name,
                                    onClick = {
                                        activePresetName = preset.name
                                        selectedBrand = preset.brand
                                    },
                                )
                            }
                            customSeeds.forEach { (name, seed) ->
                                BrandDot(
                                    name = name,
                                    color = seed,
                                    selected = activePresetName == name,
                                    onClick = {
                                        activePresetName = name
                                        selectedBrand = MaigBrand(primary = seed)
                                    },
                                )
                            }
                        }

                        MaigTabs(
                            tabs = categories.map { it.title },
                            selectedIndex = selectedCategoryIndex,
                            onTabSelected = { selectedCategoryIndex = it },
                            scrollable = true,
                        )
                    }
                },
                bottomBar = {
                    val bottomIdx = when (activeCategory) {
                        CatalogCategory.Tokens -> 0
                        CatalogCategory.Buttons, CatalogCategory.Inputs -> 1
                        CatalogCategory.Selection, CatalogCategory.Overlays, CatalogCategory.Display -> 2
                        CatalogCategory.Forms -> 3
                    }
                    MaigBottomNavigation(
                        items = listOf(
                            MaigBottomNavItem("Tokens") { MaigIcons.Palette() },
                            MaigBottomNavItem("Básicos") { MaigIcons.Home() },
                            MaigBottomNavItem("UI", badgeCount = 6) { MaigIcons.Layers() },
                            MaigBottomNavItem("Forms") { MaigIcons.Form() },
                        ),
                        selectedIndex = bottomIdx,
                        onItemSelected = { idx ->
                            selectedCategoryIndex = when (idx) {
                                0 -> 0
                                1 -> 1
                                2 -> 3
                                else -> categories.lastIndex
                            }
                        },
                    )
                },
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(MaigTheme.spacing.lg),
                    verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.xl),
                ) {
                    when (activeCategory) {
                        CatalogCategory.Tokens -> CoreShowcaseContent(
                            themeMode = themeMode,
                            onThemeModeChange = { themeMode = it },
                            activePresetName = activePresetName,
                            onSelectPreset = { name, brand ->
                                activePresetName = name
                                selectedBrand = brand
                            },
                        )

                        CatalogCategory.Buttons -> ButtonsShowcaseScreen()
                        CatalogCategory.Inputs -> InputsShowcaseScreen()
                        CatalogCategory.Selection -> SelectionShowcaseScreen()
                        CatalogCategory.Overlays -> OverlaysFeedbackShowcaseScreen()
                        CatalogCategory.Display -> DisplayNavigationShowcaseScreen()
                        CatalogCategory.Forms -> FormsShowcaseScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun BrandDot(
    name: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (selected) MaigTheme.colors.primarySubtle else MaigTheme.colors.surface)
            .border(
                width = 1.dp,
                color = if (selected) MaigTheme.colors.primaryDefault else MaigTheme.colors.border,
                shape = CircleShape,
            )
            .clickable(onClick = onClick)
            .padding(horizontal = MaigTheme.spacing.sm, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color),
        )
        BasicText(
            text = name,
            style = MaigTheme.typography.caption.copy(
                color = if (selected) MaigTheme.colors.onPrimarySubtle else MaigTheme.colors.textPrimary,
            ),
        )
    }
}
