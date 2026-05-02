package dev.stekl0.nonokt.core.data

import dev.stekl0.nonokt.core.model.GameLevel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

public object LevelPackIds {
    public const val SMALL: String = "small"
    public const val MEDIUM: String = "medium"
    public const val LARGE: String = "large"
}

@Serializable
public data class LevelPack(
    @Serializable(with = GameLevelImmutableListSerializer::class)
    public val levels: ImmutableList<GameLevel>,
) {
    public constructor(levels: List<GameLevel>) : this(levels.toImmutableList())
}
