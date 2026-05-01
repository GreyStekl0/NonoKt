package dev.stekl0.nonokt.core.ui.draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.roundToInt

public fun DrawScope.drawLevelSilhouette(
    solution: List<String>,
    boardOrigin: Offset,
    cellSize: Float,
    color: Color,
) {
    if (solution.isEmpty()) return

    val rowCount = solution.size
    val columnCount = solution.first().length
    if (columnCount == 0) return

    // Snap the whole board rect once and derive all edges from it.
    // This avoids cumulative per-edge rounding drift and guarantees contiguous cells.
    val boardLeft = boardOrigin.x.roundToInt()
    val boardTop = boardOrigin.y.roundToInt()
    val boardRight = (boardOrigin.x + (columnCount * cellSize)).roundToInt()
    val boardBottom = (boardOrigin.y + (rowCount * cellSize)).roundToInt()
    val boardWidth = boardRight - boardLeft
    val boardHeight = boardBottom - boardTop

    val xEdges =
        IntArray(size = columnCount + 1) { index ->
            boardLeft + (boardWidth * index) / columnCount
        }
    val yEdges =
        IntArray(size = rowCount + 1) { index ->
            boardTop + (boardHeight * index) / rowCount
        }

    solution.forEachIndexed { rowIndex, row ->
        var columnIndex = 0

        while (columnIndex < row.length) {
            if (row[columnIndex] != '1') {
                columnIndex += 1
                continue
            }

            val runStart = columnIndex
            while (columnIndex < row.length && row[columnIndex] == '1') {
                columnIndex += 1
            }

            drawRect(
                color = color,
                topLeft =
                    Offset(
                        x = xEdges[runStart].toFloat(),
                        y = yEdges[rowIndex].toFloat(),
                    ),
                size =
                    Size(
                        width = (xEdges[columnIndex] - xEdges[runStart]).toFloat(),
                        height = (yEdges[rowIndex + 1] - yEdges[rowIndex]).toFloat(),
                    ),
            )
        }
    }
}
