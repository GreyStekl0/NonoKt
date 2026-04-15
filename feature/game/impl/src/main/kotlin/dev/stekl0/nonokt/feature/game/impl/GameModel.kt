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
    val mode: GameMode = GameMode.FILL,
    val errorCount: Int = 0,
    val pastMoves: PersistentList<GameMove> = persistentListOf(),
    val futureMoves: PersistentList<GameMove> = persistentListOf(),
) : MVIState {
    val maxRowHintCount: Int
        get() = rowHints.maxOf(LineHint::size)

    val maxColumnHintCount: Int
        get() = columnHints.maxOf(LineHint::size)

    val canUndo: Boolean
        get() = pastMoves.isNotEmpty()

    val canRedo: Boolean
        get() = futureMoves.isNotEmpty()

    val isSolved: Boolean
        get() = boardMatchesSolution()

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
            copy(
                board = board.updated(position = position, value = nextCellState),
                errorCount =
                    errorCount +
                        nextErrorDelta(
                            previous = previousCellState,
                            next = nextCellState,
                        ),
                pastMoves = pastMoves.add(GameMove(position, previousCellState, nextCellState)),
                futureMoves = persistentListOf(),
            )
        }
    }

    fun undo(): GameState {
        if (!canUndo) return this

        val move = pastMoves.last()
        return copy(
            board = board.updated(position = move.position, value = move.previousCellState),
            pastMoves = pastMoves.removeAt(pastMoves.lastIndex),
            futureMoves = futureMoves.add(move),
        )
    }

    fun redo(): GameState {
        if (!canRedo) return this

        val move = futureMoves.last()
        return copy(
            board = board.updated(position = move.position, value = move.nextCellState),
            pastMoves = pastMoves.add(move),
            futureMoves = futureMoves.removeAt(futureMoves.lastIndex),
        )
    }

    private fun cellStateAt(position: CellPosition): PlayerCellState = board[position.row][position.column]

    private fun boardMatchesSolution(): Boolean =
        level.solution.indices.all { row ->
            level.solution[row].indices.all { column ->
                val shouldBeFilled = level.solution[row][column] == '1'
                when {
                    shouldBeFilled -> board[row][column] == PlayerCellState.FILLED
                    else -> board[row][column] != PlayerCellState.FILLED && board[row][column] != PlayerCellState.ERROR
                }
            }
        }

    private fun resolveNextCellState(
        position: CellPosition,
        previousCellState: PlayerCellState,
    ): PlayerCellState =
        when (mode) {
            GameMode.FILL -> {
                when (previousCellState) {
                    PlayerCellState.FILLED,
                    PlayerCellState.ERROR,
                    -> {
                        PlayerCellState.EMPTY
                    }

                    PlayerCellState.EMPTY,
                    PlayerCellState.MARKED,
                    -> {
                        if (level.solution[position.row][position.column] == '1') {
                            PlayerCellState.FILLED
                        } else {
                            PlayerCellState.ERROR
                        }
                    }
                }
            }

            GameMode.MARK -> {
                when (previousCellState) {
                    PlayerCellState.MARKED -> PlayerCellState.EMPTY
                    else -> PlayerCellState.MARKED
                }
            }
        }

    private fun nextErrorDelta(
        previous: PlayerCellState,
        next: PlayerCellState,
    ): Int = if (previous != PlayerCellState.ERROR && next == PlayerCellState.ERROR) 1 else 0

    internal companion object {
        fun create(level: GameLevel): GameState =
            GameState(
                level = level,
                board =
                    List(size = level.height) {
                        List(size = level.width) { PlayerCellState.EMPTY }.toPersistentList()
                    }.toPersistentList(),
                rowHints = level.solution.map(::lineHint).toPersistentList(),
                columnHints =
                    (0 until level.width)
                        .map { column ->
                            lineHint(
                                buildString(capacity = level.height) {
                                    repeat(level.height) { row ->
                                        append(level.solution[row][column])
                                    }
                                },
                            )
                        }.toPersistentList(),
            )
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

private fun lineHint(line: String): LineHint {
    val values =
        buildList {
            var runLength = 0

            line.forEach { cell ->
                if (cell == '1') {
                    runLength += 1
                } else if (runLength > 0) {
                    add(runLength)
                    runLength = 0
                }
            }

            if (runLength > 0) add(runLength)
        }

    return LineHint(
        values = values.ifEmpty { listOf(0) }.toPersistentList(),
        isFullyFilled = line.all { it == '1' },
    )
}

private fun PersistentList<PersistentList<PlayerCellState>>.updated(
    position: CellPosition,
    value: PlayerCellState,
): PersistentList<PersistentList<PlayerCellState>> =
    set(
        index = position.row,
        element = this[position.row].set(index = position.column, element = value),
    )
