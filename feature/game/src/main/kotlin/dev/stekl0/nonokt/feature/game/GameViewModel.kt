package dev.stekl0.nonokt.feature.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.stekl0.nonokt.core.data.LevelCompletionRepository
import dev.stekl0.nonokt.core.data.levelCompletionId
import dev.stekl0.nonokt.core.model.GameLevel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import timber.log.Timber
import java.io.IOException
import kotlin.time.Duration.Companion.seconds

private object CompletionPersistencePolicy {
    const val MAX_ATTEMPTS: Int = 5

    fun retryDelay() = 2.seconds
}

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

        completionJob =
            viewModelScope.launch {
                var attempt = 1
                while (isActive && attempt <= CompletionPersistencePolicy.MAX_ATTEMPTS) {
                    try {
                        levelCompletionRepository.markCompleted(
                            packId = packId,
                            levelId = state.level.id,
                        )
                        persistedCompletionId = completionId
                        return@launch
                    } catch (throwable: CancellationException) {
                        throw throwable
                    } catch (throwable: IOException) {
                        Timber.e(throwable, "Failed to persist level completion: %s", completionId)
                        if (attempt == CompletionPersistencePolicy.MAX_ATTEMPTS) {
                            return@launch
                        }
                        attempt += 1
                        delay(CompletionPersistencePolicy.retryDelay())
                    }
                }
            }
    }
}
