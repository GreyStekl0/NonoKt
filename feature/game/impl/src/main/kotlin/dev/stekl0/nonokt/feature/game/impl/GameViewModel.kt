package dev.stekl0.nonokt.feature.game.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.stekl0.nonokt.feature.game.api.GameLevel
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.dsl.intent
import pro.respawn.flowmvi.dsl.reduceLambdas
import pro.respawn.flowmvi.dsl.store

internal class GameViewModel(
    level: GameLevel,
) : ViewModel(),
    Container<GameState, GameIntent, Nothing> {
    override val store =
        store(
            initial = GameState.create(level),
            scope = viewModelScope,
        ) {
            reduceLambdas()
        }

    fun onCellPressed(
        row: Int,
        column: Int,
    ) = store.intent {
        updateState {
            onCellPressed(row = row, column = column)
        }
    }

    fun onModeChanged(mode: GameMode) =
        store.intent {
            updateState {
                withMode(mode)
            }
        }

    fun undo() =
        store.intent {
            updateState {
                undo()
            }
        }

    fun redo() =
        store.intent {
            updateState {
                redo()
            }
        }

    internal companion object {
        fun factory(level: GameLevel): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    GameViewModel(level = level)
                }
            }
    }
}
