package dev.stekl0.nonokt.core.data

import android.content.res.AssetManager
import dev.stekl0.nonokt.core.model.GameLevel
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.serialization.json.Json
import timber.log.Timber

internal class LevelPackAssetSource(
    private val assets: AssetManager,
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun readLevelPacks(): ImmutableMap<String, LevelPack> =
        LevelPackAsset.entries
            .associate { pack ->
                pack.packId to readLevelPack(pack.assetPath)
            }.toImmutableMap()

    private fun readLevelPack(assetPath: String): LevelPack =
        runCatching {
            assets
                .open(assetPath)
                .bufferedReader()
                .use { reader ->
                    json.decodeFromString<LevelPack>(reader.readText())
                }
        }.getOrElse { throwable ->
            Timber.e(throwable, "Failed to load level pack asset: %s", assetPath)
            throw throwable
        }.also { levelPack ->
            runCatching {
                validateLevelPack(levelPack)
            }.getOrElse { throwable ->
                Timber.e(throwable, "Level pack validation failed: %s", assetPath)
                throw throwable
            }
        }

    private fun validateLevelPack(levelPack: LevelPack) {
        val duplicateIds =
            levelPack.levels
                .groupingBy(GameLevel::id)
                .eachCount()
                .filterValues { count -> count > 1 }
                .keys
        require(duplicateIds.isEmpty()) {
            "Level pack contains duplicate level IDs: ${duplicateIds.joinToString()}."
        }
    }
}

private enum class LevelPackAsset(
    val packId: String,
    val assetPath: String,
) {
    SMALL(LevelPackIds.SMALL, "levels/small.json"),
    MEDIUM(LevelPackIds.MEDIUM, "levels/medium.json"),
    LARGE(LevelPackIds.LARGE, "levels/large.json"),
}
