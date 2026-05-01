package dev.stekl0.nonokt.feature.levels.impl

import androidx.compose.runtime.Immutable
import dev.stekl0.nonokt.feature.game.impl.GameLevel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal enum class Tab {
    SMALL,
    MEDIUM,
    LARGE,
    ;

    val assetPath: String
        get() =
            when (this) {
                SMALL -> "levels/small.json"
                MEDIUM -> "levels/medium.json"
                LARGE -> "levels/large.json"
            }

    val packId: String
        get() = name.lowercase()
}

@Immutable
internal data class LevelsState(
    val selectedTab: Tab = Tab.SMALL,
    val levelPacks: ImmutableMap<String, LevelPack> = persistentMapOf(),
    val completedLevelIds: ImmutableSet<String> = persistentSetOf(),
) {
    val levels: ImmutableList<GameLevel>
        get() = levelPacks[selectedTab.packId]?.levels ?: persistentListOf()
}

@Serializable
@Immutable
internal data class LevelPack(
    @Serializable(with = GameLevelImmutableListSerializer::class)
    val levels: ImmutableList<GameLevel>,
) {
    internal constructor(levels: List<GameLevel>) : this(levels.toImmutableList())
}

private class ImmutableListSerializer<T>(
    elementSerializer: KSerializer<T>,
) : KSerializer<ImmutableList<T>> {
    private val delegate = ListSerializer(elementSerializer)

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(
        encoder: Encoder,
        value: ImmutableList<T>,
    ) {
        delegate.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): ImmutableList<T> = delegate.deserialize(decoder).toImmutableList()
}

private object GameLevelImmutableListSerializer :
    KSerializer<ImmutableList<GameLevel>> by ImmutableListSerializer(GameLevel.serializer())
