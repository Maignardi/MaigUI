package io.github.maignardi.maigui.components.internal

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
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor

@Composable
internal fun MaigCheckIcon(
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    tint: Color = LocalMaigContentColor.current,
    strokeWidth: Dp = 2.dp,
) {
    Canvas(modifier = modifier.size(size)) {
        val strokePx = strokeWidth.toPx()
        val path = Path().apply {
            moveTo(this@Canvas.size.width * 0.20f, this@Canvas.size.height * 0.52f)
            lineTo(this@Canvas.size.width * 0.42f, this@Canvas.size.height * 0.74f)
            lineTo(this@Canvas.size.width * 0.82f, this@Canvas.size.height * 0.28f)
        }
        drawPath(
            path = path,
            color = tint,
            style = Stroke(width = strokePx, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }
}

@Composable
internal fun MaigCloseIcon(
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    tint: Color = LocalMaigContentColor.current,
    strokeWidth: Dp = 1.8.dp,
) {
    Canvas(modifier = modifier.size(size)) {
        val strokePx = strokeWidth.toPx()
        val p = this.size.width * 0.24f
        val q = this.size.width * 0.76f
        drawLine(tint, Offset(p, p), Offset(q, q), strokeWidth = strokePx, cap = StrokeCap.Round)
        drawLine(tint, Offset(q, p), Offset(p, q), strokeWidth = strokePx, cap = StrokeCap.Round)
    }
}

@Composable
internal fun MaigSearchIcon(
    modifier: Modifier = Modifier,
    size: Dp = 18.dp,
    tint: Color = LocalMaigContentColor.current,
    strokeWidth: Dp = 1.8.dp,
) {
    Canvas(modifier = modifier.size(size)) {
        val strokePx = strokeWidth.toPx()
        val radius = this.size.minDimension * 0.29f
        val center = Offset(this.size.width * 0.44f, this.size.height * 0.44f)
        drawCircle(
            color = tint,
            radius = radius,
            center = center,
            style = Stroke(width = strokePx),
        )
        val start = Offset(
            center.x + radius * 0.72f,
            center.y + radius * 0.72f,
        )
        val end = Offset(this.size.width * 0.84f, this.size.height * 0.84f)
        drawLine(tint, start, end, strokeWidth = strokePx, cap = StrokeCap.Round)
    }
}

@Composable
internal fun MaigEyeIcon(
    visible: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 18.dp,
    tint: Color = LocalMaigContentColor.current,
    strokeWidth: Dp = 1.8.dp,
) {
    Canvas(modifier = modifier.size(size)) {
        val strokePx = strokeWidth.toPx()
        val w = this.size.width
        val h = this.size.height
        val path = Path().apply {
            moveTo(w * 0.10f, h * 0.50f)
            cubicTo(w * 0.28f, h * 0.22f, w * 0.72f, h * 0.22f, w * 0.90f, h * 0.50f)
            cubicTo(w * 0.72f, h * 0.78f, w * 0.28f, h * 0.78f, w * 0.10f, h * 0.50f)
            close()
        }
        drawPath(path, color = tint, style = Stroke(width = strokePx, join = StrokeJoin.Round))
        drawCircle(
            color = tint,
            radius = w * 0.12f,
            center = Offset(w * 0.50f, h * 0.50f),
            style = Stroke(width = strokePx),
        )
        if (!visible) {
            drawLine(
                color = tint,
                start = Offset(w * 0.18f, h * 0.82f),
                end = Offset(w * 0.82f, h * 0.18f),
                strokeWidth = strokePx,
                cap = StrokeCap.Round,
            )
        }
    }
}

@Composable
internal fun MaigChevronIcon(
    direction: ChevronDirection,
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    tint: Color = LocalMaigContentColor.current,
    strokeWidth: Dp = 1.8.dp,
) {
    Canvas(modifier = modifier.size(size)) {
        val strokePx = strokeWidth.toPx()
        val w = this.size.width
        val h = this.size.height
        val path = Path().apply {
            when (direction) {
                ChevronDirection.Down -> {
                    moveTo(w * 0.24f, h * 0.36f)
                    lineTo(w * 0.50f, h * 0.64f)
                    lineTo(w * 0.76f, h * 0.36f)
                }
                ChevronDirection.Up -> {
                    moveTo(w * 0.24f, h * 0.64f)
                    lineTo(w * 0.50f, h * 0.36f)
                    lineTo(w * 0.76f, h * 0.64f)
                }
                ChevronDirection.Left -> {
                    moveTo(w * 0.64f, h * 0.24f)
                    lineTo(w * 0.36f, h * 0.50f)
                    lineTo(w * 0.64f, h * 0.76f)
                }
                ChevronDirection.Right -> {
                    moveTo(w * 0.36f, h * 0.24f)
                    lineTo(w * 0.64f, h * 0.50f)
                    lineTo(w * 0.36f, h * 0.76f)
                }
            }
        }
        drawPath(
            path = path,
            color = tint,
            style = Stroke(width = strokePx, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }
}

internal enum class ChevronDirection {
    Up,
    Down,
    Left,
    Right,
}

@Composable
internal fun MaigArrowBackIcon(
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    tint: Color = LocalMaigContentColor.current,
    strokeWidth: Dp = 1.8.dp,
) {
    Canvas(modifier = modifier.size(size)) {
        val strokePx = strokeWidth.toPx()
        val w = this.size.width
        val h = this.size.height
        drawLine(tint, Offset(w * 0.80f, h * 0.50f), Offset(w * 0.22f, h * 0.50f), strokePx, StrokeCap.Round)
        val head = Path().apply {
            moveTo(w * 0.48f, h * 0.24f)
            lineTo(w * 0.20f, h * 0.50f)
            lineTo(w * 0.48f, h * 0.76f)
        }
        drawPath(head, tint, style = Stroke(strokePx, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
internal fun MaigMenuIcon(
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    tint: Color = LocalMaigContentColor.current,
    strokeWidth: Dp = 1.8.dp,
) {
    Canvas(modifier = modifier.size(size)) {
        val strokePx = strokeWidth.toPx()
        val w = this.size.width
        val h = this.size.height
        drawLine(tint, Offset(w * 0.16f, h * 0.28f), Offset(w * 0.84f, h * 0.28f), strokePx, StrokeCap.Round)
        drawLine(tint, Offset(w * 0.16f, h * 0.50f), Offset(w * 0.84f, h * 0.50f), strokePx, StrokeCap.Round)
        drawLine(tint, Offset(w * 0.16f, h * 0.72f), Offset(w * 0.84f, h * 0.72f), strokePx, StrokeCap.Round)
    }
}

@Composable
internal fun MaigPlusIcon(
    modifier: Modifier = Modifier,
    size: Dp = 18.dp,
    tint: Color = LocalMaigContentColor.current,
    strokeWidth: Dp = 2.dp,
) {
    Canvas(modifier = modifier.size(size)) {
        val strokePx = strokeWidth.toPx()
        val w = this.size.width
        val h = this.size.height
        drawLine(tint, Offset(w * 0.50f, h * 0.20f), Offset(w * 0.50f, h * 0.80f), strokePx, StrokeCap.Round)
        drawLine(tint, Offset(w * 0.20f, h * 0.50f), Offset(w * 0.80f, h * 0.50f), strokePx, StrokeCap.Round)
    }
}

@Composable
internal fun MaigCalendarIcon(
    modifier: Modifier = Modifier,
    size: Dp = 18.dp,
    tint: Color = LocalMaigContentColor.current,
    strokeWidth: Dp = 1.6.dp,
) {
    Canvas(modifier = modifier.size(size)) {
        val strokePx = strokeWidth.toPx()
        val w = this.size.width
        val h = this.size.height
        drawRoundRect(
            color = tint,
            topLeft = Offset(w * 0.14f, h * 0.22f),
            size = Size(w * 0.72f, h * 0.64f),
            cornerRadius = CornerRadius(w * 0.10f, w * 0.10f),
            style = Stroke(width = strokePx),
        )
        drawLine(tint, Offset(w * 0.14f, h * 0.42f), Offset(w * 0.86f, h * 0.42f), strokePx)
        drawLine(tint, Offset(w * 0.34f, h * 0.12f), Offset(w * 0.34f, h * 0.28f), strokePx, StrokeCap.Round)
        drawLine(tint, Offset(w * 0.66f, h * 0.12f), Offset(w * 0.66f, h * 0.28f), strokePx, StrokeCap.Round)
    }
}

@Composable
internal fun MaigClockIcon(
    modifier: Modifier = Modifier,
    size: Dp = 18.dp,
    tint: Color = LocalMaigContentColor.current,
    strokeWidth: Dp = 1.6.dp,
) {
    Canvas(modifier = modifier.size(size)) {
        val strokePx = strokeWidth.toPx()
        val c = Offset(this.size.width * 0.5f, this.size.height * 0.5f)
        val r = this.size.minDimension * 0.36f
        drawCircle(tint, radius = r, center = c, style = Stroke(width = strokePx))
        drawLine(tint, c, Offset(c.x, c.y - r * 0.55f), strokePx, StrokeCap.Round)
        drawLine(tint, c, Offset(c.x + r * 0.45f, c.y + r * 0.20f), strokePx, StrokeCap.Round)
    }
}

@Composable
internal fun MaigStatusGlyphIcon(
    type: MaigGlyphType,
    modifier: Modifier = Modifier,
    size: Dp = 18.dp,
    tint: Color = LocalMaigContentColor.current,
    strokeWidth: Dp = 1.8.dp,
) {
    Canvas(modifier = modifier.size(size)) {
        val strokePx = strokeWidth.toPx()
        val w = this.size.width
        val h = this.size.height
        val c = Offset(w * 0.5f, h * 0.5f)
        val r = this.size.minDimension * 0.40f
        when (type) {
            MaigGlyphType.Info -> {
                drawCircle(tint, radius = r, center = c, style = Stroke(width = strokePx))
                drawCircle(tint, radius = strokePx * 0.65f, center = Offset(c.x, h * 0.34f))
                drawLine(tint, Offset(c.x, h * 0.48f), Offset(c.x, h * 0.68f), strokePx, StrokeCap.Round)
            }
            MaigGlyphType.Success -> {
                drawCircle(tint, radius = r, center = c, style = Stroke(width = strokePx))
                val check = Path().apply {
                    moveTo(w * 0.32f, h * 0.52f)
                    lineTo(w * 0.45f, h * 0.64f)
                    lineTo(w * 0.68f, h * 0.38f)
                }
                drawPath(check, tint, style = Stroke(strokePx, cap = StrokeCap.Round, join = StrokeJoin.Round))
            }
            MaigGlyphType.Warning -> {
                val tri = Path().apply {
                    moveTo(w * 0.50f, h * 0.14f)
                    lineTo(w * 0.88f, h * 0.82f)
                    lineTo(w * 0.12f, h * 0.82f)
                    close()
                }
                drawPath(tri, tint, style = Stroke(strokePx, join = StrokeJoin.Round))
                drawLine(tint, Offset(w * 0.50f, h * 0.38f), Offset(w * 0.50f, h * 0.58f), strokePx, StrokeCap.Round)
                drawCircle(tint, radius = strokePx * 0.6f, center = Offset(w * 0.50f, h * 0.70f))
            }
            MaigGlyphType.Error -> {
                drawCircle(tint, radius = r, center = c, style = Stroke(width = strokePx))
                val d = r * 0.42f
                drawLine(tint, Offset(c.x - d, c.y - d), Offset(c.x + d, c.y + d), strokePx, StrokeCap.Round)
                drawLine(tint, Offset(c.x + d, c.y - d), Offset(c.x - d, c.y + d), strokePx, StrokeCap.Round)
            }
        }
    }
}

internal enum class MaigGlyphType {
    Info,
    Success,
    Warning,
    Error,
}
