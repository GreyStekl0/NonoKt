package dev.stekl0.nonokt.feature.game

import androidx.compose.runtime.Immutable
import dev.stekl0.nonokt.core.model.GameLevel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

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
    val completionPersistenceState: CompletionPersistenceState = CompletionPersistenceState.NOT_STARTED,
) {
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

    val isCompletionPersistenceBlocking: Boolean
        get() = completionPersistenceState == CompletionPersistenceState.SAVING

    internal companion object {
        fun create(level: GameLevel): GameState = createGameState(level)
    }
}

@Immutable
internal data class LineHint(
    val values: ImmutableList<Int>,
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

internal enum class CompletionPersistenceState {
    NOT_STARTED,
    SAVING,
    SAVED,
    FAILED,
}

@Immutable
internal data class GameMove(
    val changes: ImmutableList<CellStateChange>,
)

@Immutable
internal data class CellStateChange(
    val position: CellPosition,
    val previousCellState: PlayerCellState,
    val nextCellState: PlayerCellState,
)

@Immutable
internal data class CellPosition(
    val row: Int,
    val column: Int,
)
