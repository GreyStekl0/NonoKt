package dev.stekl0.nonokt.core.data

import android.content.Context
import dev.stekl0.nonokt.core.model.GameLevel
import kotlinx.collections.immutable.ImmutableMap
import org.koin.core.annotation.Single

@Single
public class LevelRepository(
    private val appContext: Context,
) {
    private val assetSource = LevelPackAssetSource(appContext.assets)
    private val levelPacks: ImmutableMap<String, LevelPack> by lazy(assetSource::readLevelPacks)

    public fun loadLevelPacks(): ImmutableMap<String, LevelPack> = levelPacks

    public fun loadLevels(packId: String): List<GameLevel>? = levelPacks[packId]?.levels
}
