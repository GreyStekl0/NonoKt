package dev.stekl0.nonokt.feature.game.impl

import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIState
import pro.respawn.flowmvi.dsl.LambdaIntent

internal sealed interface GameState : MVIState {
    data object Loading : GameState

    data class Error(
        val e: Exception?,
    ) : GameState
    //
}

internal typealias GameIntent = LambdaIntent<GameState, GameAction>

internal sealed interface GameAction : MVIAction
