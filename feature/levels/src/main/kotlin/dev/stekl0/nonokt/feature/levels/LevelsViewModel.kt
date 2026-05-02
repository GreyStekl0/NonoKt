package dev.stekl0.nonokt.feature.levels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.stekl0.nonokt.core.data.LevelCompletionRepository
import dev.stekl0.nonokt.core.data.LevelPack
import dev.stekl0.nonokt.core.data.LevelRepository
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class LevelsViewModel(
    private val levelRepository: LevelRepository,
    levelCompletionRepository: LevelCompletionRepository,
) : ViewModel() {
    private val levelPacks: MutableStateFlow<ImmutableMap<String, LevelPack>> = MutableStateFlow(persistentMapOf())
    private val selectedTab = MutableStateFlow(defaultTab)
    private val loadState = MutableStateFlow<LevelsLoadState>(LevelsLoadState.Loading)
    val state: StateFlow<LevelsState> =
        combine(
            selectedTab,
            levelPacks,
            levelCompletionRepository.completedLevelIds,
            loadState,
        ) { selectedTab, levelPacks, completedLevelIds, loadState ->
            LevelsState(
                selectedTab = selectedTab,
                levelPacks = levelPacks,
                completedLevelIds = completedLevelIds,
                loadState = loadState,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue =
                LevelsState(
                    selectedTab = defaultTab,
                    levelPacks = persistentMapOf(),
                    completedLevelIds = persistentSetOf(),
                    loadState = LevelsLoadState.Loading,
                ),
        )

    init {
        loadLevelPacks(force = true)
    }

    fun retryLoadLevelPacks() {
        if (loadState.value !is LevelsLoadState.Error) return
        loadLevelPacks()
    }

    private fun loadLevelPacks(force: Boolean = false) {
        if (!force && loadState.value == LevelsLoadState.Loading) return

        loadState.value = LevelsLoadState.Loading
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    levelRepository.loadLevelPacks()
                }
            }.onSuccess { packs ->
                levelPacks.value = packs
                loadState.value = LevelsLoadState.Content
            }.onFailure { throwable ->
                if (throwable is CancellationException) throw throwable
                Timber.e(throwable, "Failed to load level packs.")
                loadState.value =
                    LevelsLoadState.Error(
                        message = throwable.message.orEmpty(),
                    )
            }
        }
    }

    fun onTabSelected(tab: Int) {
        val selectedTabEntry = Tab.entries.getOrNull(tab) ?: return
        this.selectedTab.update {
            selectedTabEntry
        }
    }

    private companion object {
        val defaultTab: Tab = Tab.SMALL
    }
}
