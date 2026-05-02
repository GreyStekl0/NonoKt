package dev.stekl0.nonokt.feature.game

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
internal fun GameMode.label(): String =
    when (this) {
        GameMode.FILL -> stringResource(R.string.feature_game_mode_fill)
        GameMode.MARK -> stringResource(R.string.feature_game_mode_mark)
    }

@Composable
internal fun GameState.progressLabel(): String =
    when {
        isSolved -> stringResource(R.string.feature_game_progress_solved)
        isFailed -> stringResource(R.string.feature_game_progress_failed)
        else -> stringResource(R.string.feature_game_progress_in_progress)
    }

@Composable
internal fun gameErrorsText(state: GameState): String =
    stringResource(
        R.string.feature_game_errors_value,
        state.errorCount,
        GameLimits.MAX_ERROR_COUNT,
    )

@Composable
internal fun gameSizeText(state: GameState): String {
    val boardSize = state.level.size

    return stringResource(
        R.string.feature_game_size_value,
        boardSize,
        boardSize,
    )
}
