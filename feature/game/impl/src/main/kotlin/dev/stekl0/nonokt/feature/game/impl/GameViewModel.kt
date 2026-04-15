package dev.stekl0.nonokt.feature.game.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.PipelineContext
import pro.respawn.flowmvi.dsl.lazyStore
import pro.respawn.flowmvi.dsl.reduceLambdas
import pro.respawn.flowmvi.plugins.enableLogging

private typealias Ctx = PipelineContext<GameState, GameIntent, GameAction>

internal class GameViewModel :
    ViewModel(),
    Container<GameState, GameIntent, GameAction> {
    override val store by lazyStore(
        initial = GameState.Loading,
        scope = viewModelScope,
    ) {
        enableLogging()
        reduceLambdas()
    }
}
