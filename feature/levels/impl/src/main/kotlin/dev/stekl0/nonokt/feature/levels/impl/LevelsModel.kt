package dev.stekl0.nonokt.feature.levels.impl

import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIState
import pro.respawn.flowmvi.dsl.LambdaIntent

internal sealed interface LevelsState : MVIState {
    data object Loading : LevelsState

    data class Error(
        val e: Exception?,
    ) : LevelsState
    //
}

internal typealias LevelsIntent = LambdaIntent<LevelsState, LevelsAction>

internal sealed interface LevelsAction : MVIAction
