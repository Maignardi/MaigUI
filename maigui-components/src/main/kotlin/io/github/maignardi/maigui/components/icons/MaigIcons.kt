package io.github.maignardi.maigui.components.icons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
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

    /** House / Home icon. */
    @Composable
    public fun Home(
        modifier: Modifier = Modifier,
        size: Dp = 18.dp,
        tint: Color = LocalMaigContentColor.current,
    ) {
        Canvas(modifier = modifier.size(size)) {
            val s = 1.7.dp.toPx()
            val w = this.size.width
            val h = this.size.height
            val path = Path().apply {
                moveTo(w * 0.14f, h * 0.46f)
                lineTo(w * 0.50f, h * 0.16f)
                lineTo(w * 0.86f, h * 0.46f)
                lineTo(w * 0.86f, h * 0.84f)
                lineTo(w * 0.62f, h * 0.84f)
                lineTo(w * 0.62f, h * 0.58f)
                lineTo(w * 0.38f, h * 0.58f)
                lineTo(w * 0.38f, h * 0.84f)
                lineTo(w * 0.14f, h * 0.84f)
                close()
            }
            drawPath(path, color = tint, style = Stroke(s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
    }

    /** User profile icon. */
    @Composable
    public fun User(
        modifier: Modifier = Modifier,
        size: Dp = 18.dp,
        tint: Color = LocalMaigContentColor.current,
    ) {
        Canvas(modifier = modifier.size(size)) {
            val s = 1.7.dp.toPx()
            val w = this.size.width
            val h = this.size.height
            drawCircle(tint, radius = w * 0.18f, center = Offset(w * 0.5f, h * 0.32f), style = Stroke(s))
            val arc = Path().apply {
                moveTo(w * 0.18f, h * 0.84f)
                cubicTo(w * 0.22f, h * 0.60f, w * 0.78f, h * 0.60f, w * 0.82f, h * 0.84f)
            }
            drawPath(arc, tint, style = Stroke(s, cap = StrokeCap.Round))
        }
    }

    /** Notification bell icon. */
    @Composable
    public fun Bell(
        modifier: Modifier = Modifier,
        size: Dp = 18.dp,
        tint: Color = LocalMaigContentColor.current,
    ) {
        Canvas(modifier = modifier.size(size)) {
            val s = 1.7.dp.toPx()
            val w = this.size.width
            val h = this.size.height
            val bell = Path().apply {
                moveTo(w * 0.24f, h * 0.68f)
                lineTo(w * 0.24f, h * 0.44f)
                cubicTo(w * 0.24f, h * 0.20f, w * 0.76f, h * 0.20f, w * 0.76f, h * 0.44f)
                lineTo(w * 0.76f, h * 0.68f)
                lineTo(w * 0.86f, h * 0.74f)
                lineTo(w * 0.14f, h * 0.74f)
                close()
            }
            drawPath(bell, tint, style = Stroke(s, cap = StrokeCap.Round, join = StrokeJoin.Round))
            drawLine(tint, Offset(w * 0.42f, h * 0.86f), Offset(w * 0.58f, h * 0.86f), s, StrokeCap.Round)
        }
    }

    /** Stacked layers / UI components icon. */
    @Composable
    public fun Layers(
        modifier: Modifier = Modifier,
        size: Dp = 18.dp,
        tint: Color = LocalMaigContentColor.current,
    ) {
        Canvas(modifier = modifier.size(size)) {
            val s = 1.6.dp.toPx()
            val w = this.size.width
            val h = this.size.height
            val top = Path().apply {
                moveTo(w * 0.50f, h * 0.16f)
                lineTo(w * 0.86f, h * 0.34f)
                lineTo(w * 0.50f, h * 0.52f)
                lineTo(w * 0.14f, h * 0.34f)
                close()
            }
            drawPath(top, tint, style = Stroke(s, join = StrokeJoin.Round))
            val mid = Path().apply {
                moveTo(w * 0.14f, h * 0.52f)
                lineTo(w * 0.50f, h * 0.70f)
                lineTo(w * 0.86f, h * 0.52f)
            }
            drawPath(mid, tint, style = Stroke(s, cap = StrokeCap.Round, join = StrokeJoin.Round))
            val bot = Path().apply {
                moveTo(w * 0.14f, h * 0.68f)
                lineTo(w * 0.50f, h * 0.86f)
                lineTo(w * 0.86f, h * 0.68f)
            }
            drawPath(bot, tint, style = Stroke(s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
    }

    /** Palette / Theme swatch icon. */
    @Composable
    public fun Palette(
        modifier: Modifier = Modifier,
        size: Dp = 18.dp,
        tint: Color = LocalMaigContentColor.current,
    ) {
        Canvas(modifier = modifier.size(size)) {
            val s = 1.6.dp.toPx()
            val w = this.size.width
            val h = this.size.height
            drawCircle(tint, radius = w * 0.38f, center = Offset(w * 0.5f, h * 0.5f), style = Stroke(s))
            drawCircle(tint, radius = 1.6.dp.toPx(), center = Offset(w * 0.36f, h * 0.42f))
            drawCircle(tint, radius = 1.6.dp.toPx(), center = Offset(w * 0.54f, h * 0.34f))
            drawCircle(tint, radius = 1.6.dp.toPx(), center = Offset(w * 0.66f, h * 0.48f))
        }
    }

    /** Envelope / Mail icon. */
    @Composable
    public fun Mail(
        modifier: Modifier = Modifier,
        size: Dp = 18.dp,
        tint: Color = LocalMaigContentColor.current,
    ) {
        Canvas(modifier = modifier.size(size)) {
            val s = 1.6.dp.toPx()
            val w = this.size.width
            val h = this.size.height
            drawRoundRect(
                color = tint,
                topLeft = Offset(w * 0.12f, h * 0.22f),
                size = Size(w * 0.76f, h * 0.56f),
                cornerRadius = CornerRadius(w * 0.10f, w * 0.10f),
                style = Stroke(s),
            )
            val flap = Path().apply {
                moveTo(w * 0.14f, h * 0.28f)
                lineTo(w * 0.50f, h * 0.54f)
                lineTo(w * 0.86f, h * 0.28f)
            }
            drawPath(flap, tint, style = Stroke(s, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
    }

    /** Padlock / Security icon. */
    @Composable
    public fun Lock(
        modifier: Modifier = Modifier,
        size: Dp = 18.dp,
        tint: Color = LocalMaigContentColor.current,
    ) {
        Canvas(modifier = modifier.size(size)) {
            val s = 1.6.dp.toPx()
            val w = this.size.width
            val h = this.size.height
            drawRoundRect(
                color = tint,
                topLeft = Offset(w * 0.20f, h * 0.44f),
                size = Size(w * 0.60f, h * 0.42f),
                cornerRadius = CornerRadius(w * 0.10f, w * 0.10f),
                style = Stroke(s),
            )
            val shackle = Path().apply {
                moveTo(w * 0.34f, h * 0.44f)
                lineTo(w * 0.34f, h * 0.30f)
                cubicTo(w * 0.34f, h * 0.14f, w * 0.66f, h * 0.14f, w * 0.66f, h * 0.30f)
                lineTo(w * 0.66f, h * 0.44f)
            }
            drawPath(shackle, tint, style = Stroke(s, cap = StrokeCap.Round))
        }
    }

    /** Clipboard / Form document icon. */
    @Composable
    public fun Form(
        modifier: Modifier = Modifier,
        size: Dp = 18.dp,
        tint: Color = LocalMaigContentColor.current,
    ) {
        Canvas(modifier = modifier.size(size)) {
            val s = 1.6.dp.toPx()
            val w = this.size.width
            val h = this.size.height
            drawRoundRect(
                color = tint,
                topLeft = Offset(w * 0.18f, h * 0.14f),
                size = Size(w * 0.64f, h * 0.72f),
                cornerRadius = CornerRadius(w * 0.10f, w * 0.10f),
                style = Stroke(s),
            )
            drawLine(tint, Offset(w * 0.32f, h * 0.36f), Offset(w * 0.68f, h * 0.36f), s, StrokeCap.Round)
            drawLine(tint, Offset(w * 0.32f, h * 0.52f), Offset(w * 0.68f, h * 0.52f), s, StrokeCap.Round)
            drawLine(tint, Offset(w * 0.32f, h * 0.68f), Offset(w * 0.54f, h * 0.68f), s, StrokeCap.Round)
        }
    }
}
