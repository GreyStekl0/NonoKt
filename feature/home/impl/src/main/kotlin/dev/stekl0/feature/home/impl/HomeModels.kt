package dev.stekl0.feature.home.impl

import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIState
import pro.respawn.flowmvi.dsl.LambdaIntent

internal sealed interface HomeState : MVIState {
    data object Loading : HomeState

    data class Error(
        val e: Exception?,
    ) : HomeState
    //
}

internal typealias HomeIntent = LambdaIntent<HomeState, HomeAction>

internal sealed interface HomeAction : MVIAction
