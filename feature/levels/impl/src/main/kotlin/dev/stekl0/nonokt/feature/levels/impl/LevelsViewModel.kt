package dev.stekl0.nonokt.feature.levels.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.koin.core.annotation.KoinViewModel
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.dsl.intent
import pro.respawn.flowmvi.dsl.lazyStore
import pro.respawn.flowmvi.dsl.reduceLambdas
import pro.respawn.flowmvi.dsl.updateState

@KoinViewModel
internal class LevelsViewModel :
    ViewModel(),
    Container<LevelsState, LevelsIntent, LevelsAction> {
    override val store by lazyStore(
        initial = LevelsState.Content(),
        scope = viewModelScope,
    ) {
        reduceLambdas()
    }

    fun onTabSelected(tab: Int) =
        store.intent {
            updateState<LevelsState.Content, _> {
                copy(selectedTab = Tab.entries[tab])
            }
        }
}
