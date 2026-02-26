package dev.stekl0.nonokt.feature.levels.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.PipelineContext
import pro.respawn.flowmvi.dsl.lazyStore
import pro.respawn.flowmvi.dsl.reduceLambdas

internal class LevelsViewModel :
    ViewModel(),
    Container<LevelsState, LevelsIntent, LevelsAction> {
    override val store by lazyStore(
        initial = LevelsState.Loading,
        scope = viewModelScope,
    ) {
        reduceLambdas()
    }
}
