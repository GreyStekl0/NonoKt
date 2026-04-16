package dev.stekl0.nonokt.feature.game.impl.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import dev.stekl0.nonokt.feature.game.impl.LineHint

private object BoardDecorationMetrics {
    const val HINT_GROUP_SIZE: Int = 5
    const val MARKER_INSET_FACTOR: Float = 0.22f
    const val MARKER_STROKE_FACTOR: Float = 0.08f
}

internal fun hintTextColor(
    hint: LineHint,
    colorScheme: ColorScheme,
): Color = if (hint.isFullyFilled) colorScheme.primary else colorScheme.onSurface

internal fun leadingHorizontalStroke(
    rowIndex: Int,
    thinGridStroke: Dp,
    thickGridStroke: Dp,
): Dp =
    if (rowIndex == 0 || rowIndex % BoardDecorationMetrics.HINT_GROUP_SIZE == 0) {
        thickGridStroke
    } else {
        thinGridStroke
    }

internal fun trailingHorizontalStroke(
    rowIndex: Int,
    rowCount: Int,
    thinGridStroke: Dp,
    thickGridStroke: Dp,
): Dp =
    if (rowIndex == rowCount - 1 || (rowIndex + 1) % BoardDecorationMetrics.HINT_GROUP_SIZE == 0) {
        thickGridStroke
    } else {
        thinGridStroke
    }

internal fun leadingVerticalStroke(
    columnIndex: Int,
    thinGridStroke: Dp,
    thickGridStroke: Dp,
): Dp =
    if (columnIndex == 0 || columnIndex % BoardDecorationMetrics.HINT_GROUP_SIZE == 0) {
        thickGridStroke
    } else {
        thinGridStroke
    }

internal fun trailingVerticalStroke(
    columnIndex: Int,
    columnCount: Int,
    thinGridStroke: Dp,
    thickGridStroke: Dp,
): Dp =
    if (columnIndex == columnCount - 1 || (columnIndex + 1) % BoardDecorationMetrics.HINT_GROUP_SIZE == 0) {
        thickGridStroke
    } else {
        thinGridStroke
    }

internal data class CellStrokeWidths(
    val top: Dp,
    val right: Dp,
    val bottom: Dp,
    val left: Dp,
)

internal fun Modifier.drawHintDecoration(
    backgroundColor: Color,
    strokes: CellStrokeWidths,
    lineColor: Color,
): Modifier =
    drawBehind {
        drawRect(color = backgroundColor)
        drawCellFrame(strokes = strokes, lineColor = lineColor)
    }

internal fun Modifier.drawBoardDecoration(
    backgroundColor: Color,
    strokes: CellStrokeWidths,
    lineColor: Color,
    markerColor: Color,
): Modifier =
    drawBehind {
        drawRect(color = backgroundColor)
        if (markerColor != Color.Unspecified) {
            val inset = size.minDimension * BoardDecorationMetrics.MARKER_INSET_FACTOR
            val strokeWidth = size.minDimension * BoardDecorationMetrics.MARKER_STROKE_FACTOR
            drawLine(
                color = markerColor,
                start = Offset(x = inset, y = inset),
                end = Offset(x = size.width - inset, y = size.height - inset),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round,
            )
            drawLine(
                color = markerColor,
                start = Offset(x = size.width - inset, y = inset),
                end = Offset(x = inset, y = size.height - inset),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round,
            )
        }
        drawCellFrame(strokes = strokes, lineColor = lineColor)
    }

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCellFrame(
    strokes: CellStrokeWidths,
    lineColor: Color,
) {
    drawLine(
        color = lineColor,
        start = Offset.Zero,
        end = Offset(x = size.width, y = 0f),
        strokeWidth = strokes.top.toPx(),
    )
    drawLine(
        color = lineColor,
        start = Offset.Zero,
        end = Offset(x = 0f, y = size.height),
        strokeWidth = strokes.left.toPx(),
    )
    drawLine(
        color = lineColor,
        start = Offset(x = size.width, y = 0f),
        end = Offset(x = size.width, y = size.height),
        strokeWidth = strokes.right.toPx(),
    )
    drawLine(
        color = lineColor,
        start = Offset(x = 0f, y = size.height),
        end = Offset(x = size.width, y = size.height),
        strokeWidth = strokes.bottom.toPx(),
    )
}
