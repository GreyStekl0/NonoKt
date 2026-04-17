package dev.stekl0.nonokt.feature.levels.impl

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.serialization.json.Json
import org.koin.core.annotation.KoinViewModel
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.dsl.intent
import pro.respawn.flowmvi.dsl.reduceLambdas
import pro.respawn.flowmvi.dsl.store
import timber.log.Timber

@KoinViewModel
internal class LevelsViewModel(
    private val appContext: Context,
) : ViewModel(),
    Container<LevelsState, LevelsIntent, Nothing> {
    private val json = Json

    override val store =
        store(
            initial = loadInitialState(),
            scope = viewModelScope,
        ) {
            reduceLambdas()
        }

    fun onTabSelected(tab: Int) =
        store.intent {
            updateState {
                copy(selectedTab = Tab.entries[tab])
            }
        }

    private fun loadInitialState(): LevelsState =
        LevelsState(
            levelPacks =
                Tab.entries
                    .associateWith { tab ->
                        loadLevelPack(tab.assetPath)
                    }.toImmutableMap(),
        )

    private fun loadLevelPack(assetPath: String): LevelPack =
        runCatching {
            appContext.assets
                .open(assetPath)
                .bufferedReader()
                .use { reader ->
                    json.decodeFromString<LevelPack>(reader.readText())
                }
        }.getOrElse { throwable ->
            Timber.e(throwable, "Failed to load level pack asset: %s", assetPath)
            throw throwable
        }
}
