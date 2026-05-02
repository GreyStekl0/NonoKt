package dev.stekl0.nonokt.core.data

import android.content.Context
import dev.stekl0.nonokt.core.model.GameLevel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single
import timber.log.Timber

public object LevelPackIds {
    public const val SMALL: String = "small"
    public const val MEDIUM: String = "medium"
    public const val LARGE: String = "large"
}

@Serializable
public data class LevelPack(
    @Serializable(with = GameLevelImmutableListSerializer::class)
    val levels: ImmutableList<GameLevel>,
) {
    public constructor(levels: List<GameLevel>) : this(levels.toImmutableList())
}

@Single
public class LevelRepository(
    private val appContext: Context,
) {
    private val json = Json { ignoreUnknownKeys = true }
    private val levelPacks: ImmutableMap<String, LevelPack> by lazy(::readLevelPacks)

    public fun loadLevelPacks(): ImmutableMap<String, LevelPack> = levelPacks

    public fun loadLevels(packId: String): List<GameLevel>? = levelPacks[packId]?.levels

    private fun readLevelPacks(): ImmutableMap<String, LevelPack> =
        LevelPackAsset.entries
            .associate { pack ->
                pack.packId to loadLevelPack(pack.assetPath)
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

private enum class LevelPackAsset(
    val packId: String,
    val assetPath: String,
) {
    SMALL(LevelPackIds.SMALL, "levels/small.json"),
    MEDIUM(LevelPackIds.MEDIUM, "levels/medium.json"),
    LARGE(LevelPackIds.LARGE, "levels/large.json"),
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
