package dev.stekl0.nonokt.feature.game

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

internal fun GameState.autoMarkChangesForCompletedLines(position: CellPosition): PersistentList<CellStateChange> {
    val rowChanges =
        if (isRowCompleted(position.row)) {
            rowAutoMarkChanges(position.row)
        } else {
            emptyList()
        }

    val columnChanges =
        if (isColumnCompleted(position.column)) {
            columnAutoMarkChanges(position.column)
        } else {
            emptyList()
        }

    return (rowChanges + columnChanges)
        .distinctBy(CellStateChange::position)
        .toPersistentList()
}

private fun GameState.isRowCompleted(row: Int): Boolean =
    (0 until level.size).all { column ->
        level.solution[row][column] != '1' || board[row][column] == PlayerCellState.FILLED
    }

private fun GameState.isColumnCompleted(column: Int): Boolean =
    (0 until level.size).all { row ->
        level.solution[row][column] != '1' || board[row][column] == PlayerCellState.FILLED
    }

private fun GameState.rowAutoMarkChanges(row: Int): List<CellStateChange> =
    (0 until level.size)
        .asSequence()
        .filter { column ->
            level.solution[row][column] == '0' && board[row][column] == PlayerCellState.EMPTY
        }.map { column ->
            CellStateChange(
                position = CellPosition(row = row, column = column),
                previousCellState = PlayerCellState.EMPTY,
                nextCellState = PlayerCellState.MARKED,
            )
        }.toList()

private fun GameState.columnAutoMarkChanges(column: Int): List<CellStateChange> =
    (0 until level.size)
        .asSequence()
        .filter { row ->
            level.solution[row][column] == '0' && board[row][column] == PlayerCellState.EMPTY
        }.map { row ->
            CellStateChange(
                position = CellPosition(row = row, column = column),
                previousCellState = PlayerCellState.EMPTY,
                nextCellState = PlayerCellState.MARKED,
            )
        }.toList()
