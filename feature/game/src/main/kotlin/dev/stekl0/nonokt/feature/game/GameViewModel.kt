package dev.stekl0.nonokt.feature.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.stekl0.nonokt.core.data.LevelCompletionRepository
import dev.stekl0.nonokt.core.data.levelCompletionId
import dev.stekl0.nonokt.core.model.GameLevel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class GameViewModel(
    @InjectedParam private val packId: String,
    @InjectedParam level: GameLevel,
    private val levelCompletionRepository: LevelCompletionRepository,
) : ViewModel() {
    private val mutableState = MutableStateFlow(GameState.create(level))
    val state: StateFlow<GameState> = mutableState.asStateFlow()

    private var completionJob: Job? = null
    private var persistedCompletionId: String? = null

    fun onCellPressed(
        row: Int,
        column: Int,
    ) {
        mutableState.update { state ->
            state.onCellPressed(row = row, column = column)
        }
        persistCompletionIfNeeded()
    }

    fun onModeChanged(mode: GameMode) {
        mutableState.update { state ->
            state.withMode(mode)
        }
    }

    fun undo() {
        mutableState.update { state ->
            state.undo()
        }
    }

    fun redo() {
        mutableState.update { state ->
            state.redo()
        }
        persistCompletionIfNeeded()
    }

    private fun persistCompletionIfNeeded() {
        val state = mutableState.value
        if (!state.isSolved) return

        val completionId = levelCompletionId(packId = packId, levelId = state.level.id)
        if (persistedCompletionId == completionId || completionJob?.isActive == true) return
        mutableState.update { currentState ->
            currentState.copy(completionPersistenceState = CompletionPersistenceState.SAVING)
        }

        val exceptionHandler =
            CoroutineExceptionHandler { _, throwable ->
                markCompletionPersistenceFailed(throwable, completionId)
            }

        completionJob =
            viewModelScope.launch(exceptionHandler) {
                levelCompletionRepository.markCompleted(
                    packId = packId,
                    levelId = state.level.id,
                )
                persistedCompletionId = completionId
                mutableState.update { currentState ->
                    currentState.copy(completionPersistenceState = CompletionPersistenceState.SAVED)
                }
            }
    }

    private fun markCompletionPersistenceFailed(
        throwable: Throwable,
        completionId: String,
    ) {
        Timber.e(throwable, "Failed to persist level completion: %s", completionId)
        mutableState.update { currentState ->
            currentState.copy(completionPersistenceState = CompletionPersistenceState.FAILED)
        }
    }
}
