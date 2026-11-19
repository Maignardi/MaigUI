package io.github.maignardi.maigui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.buttons.MaigIconButton
import io.github.maignardi.maigui.components.display.MaigDivider
import io.github.maignardi.maigui.components.internal.MaigArrowBackIcon
import io.github.maignardi.maigui.components.internal.MaigSearchIcon
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Default values for [MaigTopBar].
 */
public object MaigTopBarDefaults {
    public val Height: Dp = 56.dp

    @Composable
    public fun containerColor(): Color = MaigTheme.colors.surface

    @Composable
    public fun contentColor(): Color = MaigTheme.colors.textPrimary
}

/**
 * Top application bar with optional navigation icon, title/subtitle, centered layout, and action slots.
 */
@Composable
public fun MaigTopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    centered: Boolean = false,
    showDivider: Boolean = true,
    applyStatusBarsPadding: Boolean = false,
    containerColor: Color = MaigTopBarDefaults.containerColor(),
    contentColor: Color = MaigTopBarDefaults.contentColor(),
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor)
            .then(if (applyStatusBarsPadding) Modifier.statusBarsPadding() else Modifier),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaigTopBarDefaults.Height)
                .padding(horizontal = MaigTheme.spacing.sm),
        ) {
            CompositionLocalProvider(LocalMaigContentColor provides contentColor) {
                if (centered) {
                    if (navigationIcon != null) {
                        Box(modifier = Modifier.align(Alignment.CenterStart)) {
                            navigationIcon()
                        }
                    }
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 56.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        BasicText(
                            text = title,
                            style = MaigTheme.typography.title.copy(color = contentColor),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        if (subtitle != null) {
                            BasicText(
                                text = subtitle,
                                style = MaigTheme.typography.caption.copy(color = MaigTheme.colors.textSecondary),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        verticalAlignment = Alignment.CenterVertically,
                        content = actions,
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (navigationIcon != null) {
                            navigationIcon()
                            Spacer(modifier = Modifier.width(MaigTheme.spacing.xs))
                        } else {
                            Spacer(modifier = Modifier.width(MaigTheme.spacing.sm))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            BasicText(
                                text = title,
                                style = MaigTheme.typography.title.copy(color = contentColor),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            if (subtitle != null) {
                                BasicText(
                                    text = subtitle,
                                    style = MaigTheme.typography.caption.copy(color = MaigTheme.colors.textSecondary),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            content = actions,
                        )
                    }
                }
            }
        }
        if (showDivider) {
            MaigDivider()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigTopBarLightPreview() {
    MaigTheme(darkTheme = false) {
        MaigTopBar(
            title = "Configurações",
            subtitle = "Conta e aparência",
            navigationIcon = {
                MaigIconButton(onClick = {}, contentDescription = "Voltar") { MaigArrowBackIcon() }
            },
            actions = {
                MaigIconButton(onClick = {}, contentDescription = "Buscar") { MaigSearchIcon() }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigTopBarDarkPreview() {
    MaigTheme(darkTheme = true) {
        MaigTopBar(
            title = "MaigUI",
            centered = true,
            navigationIcon = {
                MaigIconButton(onClick = {}, contentDescription = "Voltar") { MaigArrowBackIcon() }
            },
        )
    }
}
