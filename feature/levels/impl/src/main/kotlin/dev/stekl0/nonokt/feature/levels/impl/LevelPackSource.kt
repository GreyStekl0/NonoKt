package dev.stekl0.nonokt.feature.levels.impl

import android.content.Context
import dev.stekl0.nonokt.feature.game.impl.GameLevel
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single
import timber.log.Timber

@Single
public class LevelPackSource(
    private val appContext: Context,
) {
    private val json = Json
    private val levelPacks: ImmutableMap<String, LevelPack> by lazy(::readLevelPacks)

    internal fun loadLevelPacks(): ImmutableMap<String, LevelPack> = levelPacks

    public fun loadLevels(packId: String): List<GameLevel>? = levelPacks[packId]?.levels

    private fun readLevelPacks(): ImmutableMap<String, LevelPack> =
        Tab.entries
            .associate { tab ->
                tab.packId to loadLevelPack(tab.assetPath)
            }.toImmutableMap()

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
