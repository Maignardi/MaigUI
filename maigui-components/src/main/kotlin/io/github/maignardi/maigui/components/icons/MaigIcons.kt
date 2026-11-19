package io.github.maignardi.maigui.components.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.ChevronDirection
import io.github.maignardi.maigui.components.internal.MaigArrowBackIcon
import io.github.maignardi.maigui.components.internal.MaigCalendarIcon
import io.github.maignardi.maigui.components.internal.MaigCheckIcon
import io.github.maignardi.maigui.components.internal.MaigChevronIcon
import io.github.maignardi.maigui.components.internal.MaigClockIcon
import io.github.maignardi.maigui.components.internal.MaigCloseIcon
import io.github.maignardi.maigui.components.internal.MaigMenuIcon
import io.github.maignardi.maigui.components.internal.MaigPlusIcon
import io.github.maignardi.maigui.components.internal.MaigSearchIcon
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor

/**
 * Built-in vector-crisp Canvas icons provided by MaigUI with zero Material Icons dependency.
 */
public object MaigIcons {
    /** Plus (`+`) icon. */
    @Composable
    public fun Plus(
        modifier: Modifier = Modifier,
        size: Dp = 18.dp,
        tint: Color = LocalMaigContentColor.current,
    ): Unit = MaigPlusIcon(modifier = modifier, size = size, tint = tint)

    /** Magnifying glass search icon. */
    @Composable
    public fun Search(
        modifier: Modifier = Modifier,
        size: Dp = 18.dp,
        tint: Color = LocalMaigContentColor.current,
    ): Unit = MaigSearchIcon(modifier = modifier, size = size, tint = tint)

    /** Hamburger menu icon. */
    @Composable
    public fun Menu(
        modifier: Modifier = Modifier,
        size: Dp = 20.dp,
        tint: Color = LocalMaigContentColor.current,
    ): Unit = MaigMenuIcon(modifier = modifier, size = size, tint = tint)

    /** Leftward back arrow icon. */
    @Composable
    public fun ArrowBack(
        modifier: Modifier = Modifier,
        size: Dp = 20.dp,
        tint: Color = LocalMaigContentColor.current,
    ): Unit = MaigArrowBackIcon(modifier = modifier, size = size, tint = tint)

    /** Checkmark icon. */
    @Composable
    public fun Check(
        modifier: Modifier = Modifier,
        size: Dp = 16.dp,
        tint: Color = LocalMaigContentColor.current,
    ): Unit = MaigCheckIcon(modifier = modifier, size = size, tint = tint)

    /** Close (`X`) icon. */
    @Composable
    public fun Close(
        modifier: Modifier = Modifier,
        size: Dp = 16.dp,
        tint: Color = LocalMaigContentColor.current,
    ): Unit = MaigCloseIcon(modifier = modifier, size = size, tint = tint)

    /** Right-pointing chevron icon. */
    @Composable
    public fun ChevronRight(
        modifier: Modifier = Modifier,
        size: Dp = 16.dp,
        tint: Color = LocalMaigContentColor.current,
    ): Unit = MaigChevronIcon(direction = ChevronDirection.Right, modifier = modifier, size = size, tint = tint)

    /** Left-pointing chevron icon. */
    @Composable
    public fun ChevronLeft(
        modifier: Modifier = Modifier,
        size: Dp = 16.dp,
        tint: Color = LocalMaigContentColor.current,
    ): Unit = MaigChevronIcon(direction = ChevronDirection.Left, modifier = modifier, size = size, tint = tint)

    /** Calendar icon. */
    @Composable
    public fun Calendar(
        modifier: Modifier = Modifier,
        size: Dp = 18.dp,
        tint: Color = LocalMaigContentColor.current,
    ): Unit = MaigCalendarIcon(modifier = modifier, size = size, tint = tint)

    /** Clock icon. */
    @Composable
    public fun Clock(
        modifier: Modifier = Modifier,
        size: Dp = 18.dp,
        tint: Color = LocalMaigContentColor.current,
    ): Unit = MaigClockIcon(modifier = modifier, size = size, tint = tint)
}
