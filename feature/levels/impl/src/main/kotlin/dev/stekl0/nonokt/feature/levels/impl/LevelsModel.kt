package dev.stekl0.nonokt.feature.levels.impl

import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIState
import pro.respawn.flowmvi.dsl.LambdaIntent

internal enum class Tab {
    SMALL,
    MEDIUM,
    LARGE,
}

internal sealed interface LevelsState : MVIState {
    data class Content(
        val selectedTab: Tab = Tab.SMALL,
    ) : LevelsState

    data object Loading : LevelsState

    data class Error(
        val e: Exception?,
    ) : LevelsState
    //
}

internal typealias LevelsIntent = LambdaIntent<LevelsState, LevelsAction>

internal sealed interface LevelsAction : MVIAction
