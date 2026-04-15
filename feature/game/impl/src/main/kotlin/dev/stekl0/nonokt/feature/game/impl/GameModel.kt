package dev.stekl0.nonokt.feature.game.impl

import androidx.compose.runtime.Immutable
import dev.stekl0.nonokt.feature.game.api.GameLevel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import pro.respawn.flowmvi.api.MVIState
import pro.respawn.flowmvi.dsl.LambdaIntent

internal object GameLimits {
    const val MAX_ERROR_COUNT: Int = 3
}

@Immutable
internal data class GameState(
    val level: GameLevel,
    val board: PersistentList<PersistentList<PlayerCellState>>,
    val rowHints: PersistentList<LineHint>,
    val columnHints: PersistentList<LineHint>,
    val maxRowHintCount: Int,
    val maxColumnHintCount: Int,
    val requiredFilledCellCount: Int,
    val filledCellCount: Int = 0,
    val mode: GameMode = GameMode.FILL,
    val errorCount: Int = 0,
    val pastMoves: PersistentList<GameMove> = persistentListOf(),
    val futureMoves: PersistentList<GameMove> = persistentListOf(),
) : MVIState {
    val canUndo: Boolean
        get() = pastMoves.isNotEmpty()

    val canRedo: Boolean
        get() = futureMoves.isNotEmpty()

    val isSolved: Boolean
        get() = !isFailed && filledCellCount == requiredFilledCellCount

    val isFailed: Boolean
        get() = errorCount >= GameLimits.MAX_ERROR_COUNT

    val isInteractionEnabled: Boolean
        get() = !isFailed && !isSolved

    fun withMode(mode: GameMode): GameState = if (this.mode == mode) this else copy(mode = mode)

    fun onCellPressed(
        row: Int,
        column: Int,
    ): GameState {
        if (!isInteractionEnabled) return this

        val position = CellPosition(row = row, column = column)
        val previousCellState = cellStateAt(position)
        val nextCellState = resolveNextCellState(position, previousCellState)

        return if (nextCellState == previousCellState) {
            this
        } else {
            val stateAfterBoardChange =
                applyBoardChange(
                    position = position,
                    previousCellState = previousCellState,
                    nextCellState = nextCellState,
                )

            if (nextCellState.isUndoLocked()) {
                stateAfterBoardChange.copy(
                    errorCount = errorCount + 1,
                    futureMoves = persistentListOf(),
                )
            } else {
                val move = GameMove(position, previousCellState, nextCellState)
                stateAfterBoardChange.copy(
                    pastMoves = pastMoves.add(move),
                    futureMoves = persistentListOf(),
                )
            }
        }
    }

    fun undo(): GameState {
        if (!canUndo) return this

        val move = pastMoves.last()
        return applyBoardChange(
            position = move.position,
            previousCellState = move.nextCellState,
            nextCellState = move.previousCellState,
        ).copy(
            pastMoves = pastMoves.removeAt(pastMoves.lastIndex),
            futureMoves = futureMoves.add(move),
        )
    }

    fun redo(): GameState {
        if (!canRedo) return this

        val move = futureMoves.last()
        return applyBoardChange(
            position = move.position,
            previousCellState = move.previousCellState,
            nextCellState = move.nextCellState,
        ).copy(
            pastMoves = pastMoves.add(move),
            futureMoves = futureMoves.removeAt(futureMoves.lastIndex),
        )
    }

    private fun cellStateAt(position: CellPosition): PlayerCellState = board[position.row][position.column]

    private fun resolveNextCellState(
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
                if (mode == GameMode.MARK) {
                    PlayerCellState.MARKED
                } else if (level.solution[position.row][position.column] == '1') {
                    PlayerCellState.FILLED
                } else {
                    PlayerCellState.ERROR
                }
            }
        }

    private fun applyBoardChange(
        position: CellPosition,
        previousCellState: PlayerCellState,
        nextCellState: PlayerCellState,
    ): GameState =
        copy(
            board = board.updated(position = position, value = nextCellState),
            filledCellCount = filledCellCount + filledCellDelta(previousCellState, nextCellState),
        )

    private fun filledCellDelta(
        previous: PlayerCellState,
        next: PlayerCellState,
    ): Int =
        when {
            previous != PlayerCellState.FILLED && next == PlayerCellState.FILLED -> 1
            previous == PlayerCellState.FILLED && next != PlayerCellState.FILLED -> -1
            else -> 0
        }

    internal companion object {
        fun create(level: GameLevel): GameState {
            val size = level.size
            val rowHints = level.solution.map(::lineHint).toPersistentList()
            val columnHints =
                (0 until size)
                    .map { column -> columnHint(level, column) }
                    .toPersistentList()
            val requiredFilledCellCount = rowHints.sumOf { hint -> hint.values.sum() }

            return GameState(
                level = level,
                board =
                    List(size = size) {
                        List(size = size) { PlayerCellState.EMPTY }.toPersistentList()
                    }.toPersistentList(),
                rowHints = rowHints,
                columnHints = columnHints,
                maxRowHintCount = rowHints.maxOf(LineHint::size),
                maxColumnHintCount = columnHints.maxOf(LineHint::size),
                requiredFilledCellCount = requiredFilledCellCount,
            )
        }
    }
}

internal typealias GameIntent = LambdaIntent<GameState, Nothing>

@Immutable
internal data class LineHint(
    val values: ImmutableList<Int>,
    val isFullyFilled: Boolean,
) {
    val size: Int
        get() = values.size
}

internal enum class GameMode {
    FILL,
    MARK,
}

internal enum class PlayerCellState {
    EMPTY,
    FILLED,
    MARKED,
    ERROR,
}

@Immutable
internal data class GameMove(
    val position: CellPosition,
    val previousCellState: PlayerCellState,
    val nextCellState: PlayerCellState,
)

@Immutable
internal data class CellPosition(
    val row: Int,
    val column: Int,
)

private fun lineHint(line: String): LineHint =
    buildLineHint(
        length = line.length,
        cellAt = line::get,
    )

private fun columnHint(
    level: GameLevel,
    column: Int,
): LineHint =
    buildLineHint(
        length = level.size,
        cellAt = { row -> level.solution[row][column] },
    )

private inline fun buildLineHint(
    length: Int,
    cellAt: (Int) -> Char,
): LineHint {
    var filledCellCount = 0

    val values =
        buildList {
            var runLength = 0

            repeat(length) { index ->
                val cell = cellAt(index)
                if (cell == '1') {
                    runLength += 1
                    filledCellCount += 1
                } else if (runLength > 0) {
                    add(runLength)
                    runLength = 0
                }
            }

            if (runLength > 0) add(runLength)
        }

    return LineHint(
        values = values.ifEmpty { listOf(0) }.toPersistentList(),
        isFullyFilled = filledCellCount == length,
    )
}

private fun PlayerCellState.isUndoLocked(): Boolean = this == PlayerCellState.ERROR

private fun PersistentList<PersistentList<PlayerCellState>>.updated(
    position: CellPosition,
    value: PlayerCellState,
): PersistentList<PersistentList<PlayerCellState>> =
    set(
        index = position.row,
        element = this[position.row].set(index = position.column, element = value),
    )
