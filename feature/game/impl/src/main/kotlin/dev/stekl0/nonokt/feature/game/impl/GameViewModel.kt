package dev.stekl0.nonokt.feature.game.impl

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class GameViewModel(
    @InjectedParam level: GameLevel,
) : ViewModel() {
    private val mutableState = MutableStateFlow(GameState.create(level))
    val state: StateFlow<GameState> = mutableState.asStateFlow()

    fun onCellPressed(
        row: Int,
        column: Int,
    ) {
        mutableState.update { state ->
            state.onCellPressed(row = row, column = column)
        }
    }

    fun onModeChanged(mode: GameMode) {
        mutableState.update { state ->
            state.withMode(mode)
        }
    }

    fun undo() {
        mutableState.update { state ->
            state.undo()
        }
    }

    fun redo() {
        mutableState.update { state ->
            state.redo()
        }
    }
}
