package dev.stekl0.nonokt.feature.game

import kotlinx.collections.immutable.persistentListOf

internal fun GameState.withMode(mode: GameMode): GameState = if (this.mode == mode) this else copy(mode = mode)

internal fun GameState.onCellPressed(
    row: Int,
    column: Int,
): GameState =
    if (!isInteractionEnabled || row !in board.indices || column !in board[row].indices) {
        this
    } else {
        val position = CellPosition(row = row, column = column)
        val previousCellState = cellStateAt(position)
        val nextCellState = resolveNextCellState(position, previousCellState)

        if (nextCellState == previousCellState) {
            this
        } else {
            applyCellPress(
                position = position,
                previousCellState = previousCellState,
                nextCellState = nextCellState,
            )
        }
    }

internal fun GameState.undo(): GameState {
    if (!canUndo) return this

    val move = pastMoves.last()
    return applyChanges(
        changes =
            move.changes
                .asReversed()
                .map { change ->
                    change.copy(
                        previousCellState = change.nextCellState,
                        nextCellState = change.previousCellState,
                    )
                },
    ).copy(
        pastMoves = pastMoves.removeAt(pastMoves.lastIndex),
        futureMoves = futureMoves.add(move),
    )
}

internal fun GameState.redo(): GameState {
    if (!canRedo) return this

    val move = futureMoves.last()
    return applyChanges(
        changes = move.changes,
    ).copy(
        pastMoves = pastMoves.add(move),
        futureMoves = futureMoves.removeAt(futureMoves.lastIndex),
    )
}

private fun GameState.applyCellPress(
    position: CellPosition,
    previousCellState: PlayerCellState,
    nextCellState: PlayerCellState,
): GameState {
    val primaryChange =
        CellStateChange(
            position = position,
            previousCellState = previousCellState,
            nextCellState = nextCellState,
        )
    val stateAfterBoardChange =
        applyChanges(
            changes = listOf(primaryChange),
        )

    return if (nextCellState.isUndoLocked()) {
        stateAfterBoardChange.copy(
            errorCount = errorCount + 1,
            futureMoves = persistentListOf(),
        )
    } else {
        stateAfterBoardChange.applyValidMove(primaryChange, position, nextCellState)
    }
}

private fun GameState.applyValidMove(
    primaryChange: CellStateChange,
    position: CellPosition,
    nextCellState: PlayerCellState,
): GameState {
    val autoMarkChanges =
        if (nextCellState == PlayerCellState.FILLED) {
            autoMarkChangesForCompletedLines(position)
        } else {
            persistentListOf()
        }
    val stateAfterAutoMark = applyChanges(changes = autoMarkChanges)
    val move =
        GameMove(
            changes =
                persistentListOf(primaryChange)
                    .addAll(autoMarkChanges),
        )

    return stateAfterAutoMark.copy(
        pastMoves = pastMoves.add(move),
        futureMoves = persistentListOf(),
    )
}

private fun GameState.cellStateAt(position: CellPosition): PlayerCellState = board[position.row][position.column]

private fun GameState.resolveNextCellState(
    position: CellPosition,
    previousCellState: PlayerCellState,
): PlayerCellState =
    when (previousCellState) {
        PlayerCellState.FILLED,
        PlayerCellState.ERROR,
        -> {
            previousCellState
        }

        PlayerCellState.MARKED -> {
            PlayerCellState.EMPTY
        }

        PlayerCellState.EMPTY -> {
            when {
                mode == GameMode.MARK -> PlayerCellState.MARKED
                level.solution[position.row][position.column] == '1' -> PlayerCellState.FILLED
                else -> PlayerCellState.ERROR
            }
        }
    }

private fun PlayerCellState.isUndoLocked(): Boolean = this == PlayerCellState.ERROR
