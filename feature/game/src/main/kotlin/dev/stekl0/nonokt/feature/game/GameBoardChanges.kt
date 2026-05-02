package dev.stekl0.nonokt.feature.game

import kotlinx.collections.immutable.PersistentList

internal fun GameState.applyChanges(changes: List<CellStateChange>): GameState =
    changes.fold(this) { state, change ->
        state.applyBoardChange(
            position = change.position,
            nextCellState = change.nextCellState,
        )
    }

private fun GameState.applyBoardChange(
    position: CellPosition,
    nextCellState: PlayerCellState,
): GameState {
    val previousCellState = board[position.row][position.column]
    return copy(
        board = board.updated(position = position, value = nextCellState),
        filledCellCount = filledCellCount + filledCellDelta(previousCellState, nextCellState),
    )
}

private fun filledCellDelta(
    previous: PlayerCellState,
    next: PlayerCellState,
): Int =
    when {
        previous != PlayerCellState.FILLED && next == PlayerCellState.FILLED -> 1
        previous == PlayerCellState.FILLED && next != PlayerCellState.FILLED -> -1
        else -> 0
    }

private fun PersistentList<PersistentList<PlayerCellState>>.updated(
    position: CellPosition,
    value: PlayerCellState,
): PersistentList<PersistentList<PlayerCellState>> =
    set(
        index = position.row,
        element = this[position.row].set(index = position.column, element = value),
    )
