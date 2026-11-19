package io.github.maignardi.maigui.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.github.maignardi.maigui.components.buttons.MaigFab
import io.github.maignardi.maigui.components.feedback.LocalMaigToastState
import io.github.maignardi.maigui.components.feedback.MaigToastHost
import io.github.maignardi.maigui.components.feedback.MaigToastState
import io.github.maignardi.maigui.components.feedback.rememberMaigToastState
import io.github.maignardi.maigui.components.internal.MaigPlusIcon
import io.github.maignardi.maigui.components.navigation.MaigTopBar
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Defaults for [MaigScaffold].
 */
public object MaigScaffoldDefaults {
    @Composable
    public fun containerColor(): Color = MaigTheme.colors.background

    @Composable
    public fun contentColor(): Color = MaigTheme.colors.textPrimary
}

/**
 * Screen layout scaffold with integrated [MaigToastHost], [topBar], [bottomBar],
 * [floatingActionButton], and safe inset handling.
 */
@Composable
public fun MaigScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    toastState: MaigToastState = rememberMaigToastState(),
    containerColor: Color = MaigScaffoldDefaults.containerColor(),
    contentColor: Color = MaigScaffoldDefaults.contentColor(),
    applySystemBarsPadding: Boolean = true,
    content: @Composable (PaddingValues) -> Unit,
) {
    CompositionLocalProvider(
        LocalMaigToastState provides toastState,
        LocalMaigContentColor provides contentColor,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(containerColor)
                .then(
                    if (applySystemBarsPadding) {
                        Modifier.statusBarsPadding().navigationBarsPadding().imePadding()
                    } else {
                        Modifier
                    },
                ),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    topBar()
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    content(PaddingValues())

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(MaigTheme.spacing.lg),
                    ) {
                        floatingActionButton()
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                    ) {
                        MaigToastHost(hostState = toastState)
                    }
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    bottomBar()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigScaffoldLightPreview() {
    MaigTheme(darkTheme = false) {
        MaigScaffold(
            topBar = { MaigTopBar(title = "MaigUI App") },
            floatingActionButton = { MaigFab(onClick = {}) { MaigPlusIcon() } },
            applySystemBarsPadding = false,
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(MaigTheme.spacing.lg),
            ) {
                BasicText("Conteúdo principal", style = MaigTheme.typography.body)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigScaffoldDarkPreview() {
    MaigTheme(darkTheme = true) {
        MaigScaffold(
            topBar = { MaigTopBar(title = "MaigUI App") },
            applySystemBarsPadding = false,
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(MaigTheme.spacing.lg),
            ) {
                BasicText("Conteúdo principal", style = MaigTheme.typography.body)
            }
        }
    }
}
