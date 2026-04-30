package dev.stekl0.nonokt.feature.levels.impl

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class LevelsViewModel(
    levelPackSource: LevelPackSource,
) : ViewModel() {
    private val mutableState =
        MutableStateFlow(
            LevelsState(
                levelPacks = levelPackSource.loadLevelPacks(),
            ),
        )
    val state: StateFlow<LevelsState> = mutableState.asStateFlow()

    fun onTabSelected(tab: Int) {
        mutableState.update { state ->
            state.copy(selectedTab = Tab.entries[tab])
        }
    }
}
