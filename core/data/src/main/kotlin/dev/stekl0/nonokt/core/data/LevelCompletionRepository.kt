package dev.stekl0.nonokt.core.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import java.io.IOException

private val Context.levelCompletionDataStore by preferencesDataStore(name = "level_completion")

private val CompletedLevelIdsKey = stringSetPreferencesKey("completed_level_ids")

public fun levelCompletionId(
    packId: String,
    levelId: String,
): String {
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
        val completedLevelId = levelCompletionId(packId = packId, levelId = levelId)
        appContext.levelCompletionDataStore.edit { preferences ->
            val completedLevelIds = preferences[CompletedLevelIdsKey].orEmpty()
            preferences[CompletedLevelIdsKey] = completedLevelIds + completedLevelId
        }
    }
}
