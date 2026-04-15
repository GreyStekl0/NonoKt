package dev.stekl0.nonokt.feature.levels.impl

import androidx.compose.runtime.Immutable
import dev.stekl0.nonokt.feature.game.api.GameLevel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import pro.respawn.flowmvi.api.MVIState
import pro.respawn.flowmvi.dsl.LambdaIntent

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
}

@Immutable
internal data class LevelsState(
    val selectedTab: Tab = Tab.SMALL,
    val levelPacks: ImmutableMap<Tab, LevelPack> = persistentMapOf(),
) : MVIState {
    val levels: ImmutableList<GameLevel>
        get() = levelPacks[selectedTab]?.levels ?: persistentListOf()
}

internal typealias LevelsIntent = LambdaIntent<LevelsState, Nothing>

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
