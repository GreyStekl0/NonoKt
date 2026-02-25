package dev.stekl0.feature.home.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.PipelineContext
import pro.respawn.flowmvi.dsl.lazyStore
import pro.respawn.flowmvi.dsl.reduceLambdas

private typealias Ctx = PipelineContext<HomeState, HomeIntent, HomeAction>

internal class HomeViewModel :
    ViewModel(),
    Container<HomeState, HomeIntent, HomeAction> {
    override val store by lazyStore(
        initial = HomeState.Loading,
        scope = viewModelScope,
    ) {
        reduceLambdas()
    }
}
