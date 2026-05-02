package dev.stekl0.nonokt.feature.levels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.stekl0.nonokt.core.data.LevelCompletionRepository
import dev.stekl0.nonokt.core.data.LevelPack
import dev.stekl0.nonokt.core.data.LevelRepository
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf
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

@KoinViewModel
internal class LevelsViewModel(
    private val levelRepository: LevelRepository,
    levelCompletionRepository: LevelCompletionRepository,
) : ViewModel() {
    private val levelPacks: MutableStateFlow<ImmutableMap<String, LevelPack>> = MutableStateFlow(persistentMapOf())
    private val selectedTab = MutableStateFlow(defaultTab)
    val state: StateFlow<LevelsState> =
        combine(
            selectedTab,
            levelPacks,
            levelCompletionRepository.completedLevelIds,
        ) { selectedTab, levelPacks, completedLevelIds ->
            LevelsState(
                selectedTab = selectedTab,
                levelPacks = levelPacks,
                completedLevelIds = completedLevelIds,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue =
                LevelsState(
                    selectedTab = defaultTab,
                    levelPacks = persistentMapOf(),
                    completedLevelIds = persistentSetOf(),
                ),
        )

    init {
        viewModelScope.launch {
            levelPacks.value =
                withContext(Dispatchers.IO) {
                    levelRepository.loadLevelPacks()
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
