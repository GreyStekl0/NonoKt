package dev.stekl0.nonokt.feature.game.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

internal val ThinGridStroke: Dp = 0.75.dp
internal val ThickGridStroke: Dp = 1.75.dp

internal fun paddedHintValues(
    values: List<Int>,
    targetSize: Int,
): ImmutableList<Int?> = (List((targetSize - values.size).coerceAtLeast(0)) { null } + values).toPersistentList()

internal fun columnHintCellStrokes(
    hintRowIndex: Int,
    hintRowCount: Int,
    columnIndex: Int,
    columnCount: Int,
): CellStrokeWidths =
    CellStrokeWidths(
        top = if (hintRowIndex == 0) ThickGridStroke else ThinGridStroke,
        right =
            trailingVerticalStroke(
                columnIndex = columnIndex,
                columnCount = columnCount,
                thinGridStroke = ThinGridStroke,
                thickGridStroke = ThickGridStroke,
            ),
        bottom = if (hintRowIndex == hintRowCount - 1) ThickGridStroke else ThinGridStroke,
        left =
            leadingVerticalStroke(
                columnIndex = columnIndex,
                thinGridStroke = ThinGridStroke,
                thickGridStroke = ThickGridStroke,
            ),
    )

internal fun rowHintCellStrokes(
    rowIndex: Int,
    rowCount: Int,
    hintColumnIndex: Int,
    hintColumnCount: Int,
): CellStrokeWidths =
    CellStrokeWidths(
        top =
            leadingHorizontalStroke(
                rowIndex = rowIndex,
                thinGridStroke = ThinGridStroke,
                thickGridStroke = ThickGridStroke,
            ),
        right = if (hintColumnIndex == hintColumnCount - 1) ThickGridStroke else ThinGridStroke,
        bottom =
            trailingHorizontalStroke(
                rowIndex = rowIndex,
                rowCount = rowCount,
                thinGridStroke = ThinGridStroke,
                thickGridStroke = ThickGridStroke,
            ),
        left = if (hintColumnIndex == 0) ThickGridStroke else ThinGridStroke,
    )

internal fun boardCellStrokes(
    rowIndex: Int,
    rowCount: Int,
    columnIndex: Int,
    columnCount: Int,
): CellStrokeWidths =
    CellStrokeWidths(
        top =
            leadingHorizontalStroke(
                rowIndex = rowIndex,
                thinGridStroke = ThinGridStroke,
                thickGridStroke = ThickGridStroke,
            ),
        right =
            trailingVerticalStroke(
                columnIndex = columnIndex,
                columnCount = columnCount,
                thinGridStroke = ThinGridStroke,
                thickGridStroke = ThickGridStroke,
            ),
        bottom =
            trailingHorizontalStroke(
                rowIndex = rowIndex,
                rowCount = rowCount,
                thinGridStroke = ThinGridStroke,
                thickGridStroke = ThickGridStroke,
            ),
        left =
            leadingVerticalStroke(
                columnIndex = columnIndex,
                thinGridStroke = ThinGridStroke,
                thickGridStroke = ThickGridStroke,
            ),
    )
