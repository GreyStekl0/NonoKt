package dev.stekl0.nonokt.core.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import timber.log.Timber
import java.io.IOException
import kotlin.time.Duration.Companion.seconds

private val Context.levelCompletionDataStore by preferencesDataStore(name = "level_completion")

private val CompletedLevelIdsKey = stringSetPreferencesKey("completed_level_ids")

private object CompletionPersistencePolicy {
    const val MAX_ATTEMPTS: Int = 5

    val retryDelay = 2.seconds
}

public fun levelCompletionId(
    packId: String,
    levelId: String,
): String {
    require(packId.isNotBlank() && levelId.isNotBlank()) {
        "Level completion packId and levelId must not be blank."
    }
    require('/' !in packId && '/' !in levelId) {
        "Level completion IDs must not contain '/'."
    }
    return "$packId/$levelId"
}

@Single
public class LevelCompletionRepository(
    private val appContext: Context,
) {
    public val completedLevelIds: Flow<ImmutableSet<String>> =
        appContext.levelCompletionDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Timber.e(exception, "Failed to read level completion data.")
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[CompletedLevelIdsKey]
                    .orEmpty()
                    .toPersistentSet()
            }

    public suspend fun markCompleted(
        packId: String,
        levelId: String,
    ) {
        var attempt = 1
        while (attempt <= CompletionPersistencePolicy.MAX_ATTEMPTS) {
            try {
                markCompletedOnce(
                    packId = packId,
                    levelId = levelId,
                )
                return
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: IOException) {
                Timber.e(throwable, "Failed to persist level completion.")
                if (attempt == CompletionPersistencePolicy.MAX_ATTEMPTS) throw throwable
                attempt += 1
                delay(CompletionPersistencePolicy.retryDelay)
            }
        }
    }

    private suspend fun markCompletedOnce(
        packId: String,
        levelId: String,
    ) {
        val completedLevelId = levelCompletionId(packId = packId, levelId = levelId)
        appContext.levelCompletionDataStore.edit { preferences ->
            val completedLevelIds = preferences[CompletedLevelIdsKey].orEmpty()
            preferences[CompletedLevelIdsKey] = completedLevelIds + completedLevelId
        }
    }
}
