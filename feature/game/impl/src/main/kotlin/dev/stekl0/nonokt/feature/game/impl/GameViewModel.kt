package dev.stekl0.nonokt.feature.game.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.stekl0.nonokt.feature.game.api.GameLevel
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.dsl.intent
import pro.respawn.flowmvi.dsl.reduceLambdas
import pro.respawn.flowmvi.dsl.store

@KoinViewModel
internal class GameViewModel(
    @InjectedParam level: GameLevel,
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
}
