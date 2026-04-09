package dev.stekl0.nonokt.feature.levels.impl

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.core.annotation.KoinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.dsl.intent
import pro.respawn.flowmvi.dsl.lazyStore
import pro.respawn.flowmvi.dsl.reduceLambdas
import pro.respawn.flowmvi.dsl.updateState
import pro.respawn.flowmvi.plugins.asyncInit
import pro.respawn.flowmvi.plugins.recover

@KoinViewModel
internal class LevelsViewModel :
    ViewModel(),
    KoinComponent,
    Container<LevelsState, LevelsIntent, LevelsAction> {
    private val appContext: Context by inject()
    private val json = Json { ignoreUnknownKeys = true }

    override val store by lazyStore(
        initial = LevelsState.Loading,
        scope = viewModelScope,
    ) {
        reduceLambdas()
        recover { exception ->
            updateState { LevelsState.Error(exception) }
            null
        }
        asyncInit(Dispatchers.IO) {
            val levelPacks =
                Tab.entries.associateWith { tab ->
                    loadLevelPack(tab.assetPath)
                }

            updateState {
                LevelsState.Content(levelPacks = levelPacks)
            }
        }
    }

    fun onTabSelected(tab: Int) =
        store.intent {
            updateState<LevelsState.Content, _> {
                copy(selectedTab = Tab.entries[tab])
            }
        }

    private fun loadLevelPack(assetPath: String): LevelPack =
        appContext.assets
            .open(assetPath)
            .bufferedReader()
            .use { reader ->
                json.decodeFromString<LevelPack>(reader.readText())
            }
}
