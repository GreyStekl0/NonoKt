package dev.stekl0.nonokt.feature.levels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.stekl0.nonokt.core.data.LevelCompletionRepository
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class LevelsViewModel(
    levelPackSource: LevelPackSource,
    levelCompletionRepository: LevelCompletionRepository,
) : ViewModel() {
    private val levelPacks = levelPackSource.loadLevelPacks()
    private val selectedTab = MutableStateFlow(Tab.SMALL)
    val state: StateFlow<LevelsState> =
        combine(
            selectedTab,
            levelCompletionRepository.completedLevelIds,
        ) { selectedTab, completedLevelIds ->
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
                    selectedTab = Tab.SMALL,
                    levelPacks = levelPacks,
                    completedLevelIds = persistentSetOf(),
                ),
        )

    fun onTabSelected(tab: Int) {
        val selectedTabEntry = Tab.entries.getOrNull(tab) ?: return
        this.selectedTab.update {
            selectedTabEntry
        }
    }
}
