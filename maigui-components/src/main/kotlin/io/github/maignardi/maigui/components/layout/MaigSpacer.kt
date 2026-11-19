package io.github.maignardi.maigui.components.layout

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Token-based spacing composables mapped directly to [MaigTheme.spacing].
 */
public object MaigSpacer {
    /** Renders a square spacer of size [MaigTheme.spacing.xxs] (`2.dp`). */
    @Composable
    public fun xxs(): Unit = Spacer(modifier = Modifier.size(MaigTheme.spacing.xxs))

    /** Renders a square spacer of size [MaigTheme.spacing.xs] (`4.dp`). */
    @Composable
    public fun xs(): Unit = Spacer(modifier = Modifier.size(MaigTheme.spacing.xs))

    /** Renders a square spacer of size [MaigTheme.spacing.sm] (`8.dp`). */
    @Composable
    public fun sm(): Unit = Spacer(modifier = Modifier.size(MaigTheme.spacing.sm))

    /** Renders a square spacer of size [MaigTheme.spacing.md] (`12.dp`). */
    @Composable
    public fun md(): Unit = Spacer(modifier = Modifier.size(MaigTheme.spacing.md))

    /** Renders a square spacer of size [MaigTheme.spacing.lg] (`16.dp`). */
    @Composable
    public fun lg(): Unit = Spacer(modifier = Modifier.size(MaigTheme.spacing.lg))

    /** Renders a square spacer of size [MaigTheme.spacing.xl] (`24.dp`). */
    @Composable
    public fun xl(): Unit = Spacer(modifier = Modifier.size(MaigTheme.spacing.xl))

    /** Renders a square spacer of size [MaigTheme.spacing.xxl] (`32.dp`). */
    @Composable
    public fun xxl(): Unit = Spacer(modifier = Modifier.size(MaigTheme.spacing.xxl))

    /** Renders a square spacer of size [MaigTheme.spacing.xxxl] (`48.dp`). */
    @Composable
    public fun xxxl(): Unit = Spacer(modifier = Modifier.size(MaigTheme.spacing.xxxl))

    /** Renders a vertical-only spacer of [height]. */
    @Composable
    public fun Vertical(height: Dp): Unit = Spacer(modifier = Modifier.height(height))

    /** Renders a horizontal-only spacer of [width]. */
    @Composable
    public fun Horizontal(width: Dp): Unit = Spacer(modifier = Modifier.width(width))
}
