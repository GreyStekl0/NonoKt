package dev.stekl0.nonokt.feature.levels.impl

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.stekl0.nonokt.core.designsystem.icon.BorderAll
import dev.stekl0.nonokt.core.designsystem.icon.CropSquare
import dev.stekl0.nonokt.core.designsystem.icon.GridOn
import dev.stekl0.nonokt.feature.game.impl.GameLevel
import dev.stekl0.nonokt.feature.levels.impl.ui.LevelsGrid
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import org.koin.compose.viewmodel.koinViewModel

private val Tab.labelRes: Int
    @StringRes
    get() =
        when (this) {
            Tab.SMALL -> R.string.feature_levels_impl_levels_tab_small
            Tab.MEDIUM -> R.string.feature_levels_impl_levels_tab_medium
            Tab.LARGE -> R.string.feature_levels_impl_levels_tab_large
        }

private val Tab.icon: ImageVector
    get() =
        when (this) {
            Tab.SMALL -> CropSquare
            Tab.MEDIUM -> BorderAll
            Tab.LARGE -> GridOn
        }

@Composable
internal fun LevelsScreen(
    onLevelClick: (String, Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LevelsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LevelsContent(
        state = state,
        onTabSelect = viewModel::onTabSelected,
        onLevelClick = onLevelClick,
        modifier = modifier,
    )
}

@Composable
private fun LevelsContent(
    state: LevelsState,
    onTabSelect: (Int) -> Unit,
    onLevelClick: (String, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            LevelsTabRow(
                selectedTab = state.selectedTab,
                onTabSelect = onTabSelect,
            )
        },
    ) { innerPadding ->
        if (state.levels.isEmpty()) {
            EmptyLevelsState(
                tab = state.selectedTab,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
            )
        } else {
            LevelsGrid(
                packId = state.selectedTab.packId,
                levels = state.levels,
                onLevelClick = onLevelClick,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
            )
        }
    }
}

@Composable
private fun LevelsTabRow(
    selectedTab: Tab,
    onTabSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    PrimaryTabRow(
        selectedTabIndex = selectedTab.ordinal,
        modifier =
            modifier
                .fillMaxWidth()
                .statusBarsPadding(),
    ) {
        Tab.entries.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelect(index) },
                text = {
                    Text(
                        text = stringResource(tab.labelRes),
                        style = MaterialTheme.typography.labelLarge,
                    )
                },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = null,
                    )
                },
            )
        }
    }
}

@Composable
private fun EmptyLevelsState(
    tab: Tab,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 2.dp,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    imageVector = tab.icon,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = stringResource(R.string.feature_levels_impl_levels_empty_title),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = stringResource(R.string.feature_levels_impl_levels_empty_description),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private val PreviewSmallLevels: List<GameLevel> =
    listOf(
        GameLevel(
            id = "small_5x5",
            solution =
                listOf(
                    "00100",
                    "01110",
                    "11111",
                    "01110",
                    "00100",
                ),
        ),
        GameLevel(
            id = "small_9x9",
            solution =
                listOf(
                    "000111000",
                    "001111100",
                    "011111110",
                    "111111111",
                    "111001111",
                    "111111111",
                    "011111110",
                    "001111100",
                    "000111000",
                ),
        ),
        GameLevel(
            id = "small_7x7",
            solution =
                listOf(
                    "1000001",
                    "0100010",
                    "0010100",
                    "0001000",
                    "0010100",
                    "0100010",
                    "1000001",
                ),
        ),
        GameLevel(
            id = "small_8x8",
            solution =
                listOf(
                    "11111111",
                    "10000001",
                    "10111101",
                    "10100101",
                    "10100101",
                    "10111101",
                    "10000001",
                    "11111111",
                ),
        ),
    )

private val PreviewMediumLevels: List<GameLevel> =
    listOf(
        GameLevel(
            id = "medium_10x10",
            solution =
                listOf(
                    "0000110000",
                    "0001111000",
                    "0011111100",
                    "0111111110",
                    "1111111111",
                    "1111111111",
                    "1111111111",
                    "0110110110",
                    "0000110000",
                    "0001111000",
                ),
        ),
        GameLevel(
            id = "medium_12x12",
            solution =
                listOf(
                    "000001100000",
                    "000011110000",
                    "000111111000",
                    "001111111100",
                    "011111111110",
                    "111111111111",
                    "111110011111",
                    "111100001111",
                    "011000000110",
                    "001100001100",
                    "000111111000",
                    "000011110000",
                ),
        ),
        GameLevel(
            id = "medium_11x11",
            solution =
                listOf(
                    "10000000001",
                    "11000000011",
                    "01100000110",
                    "00110001100",
                    "00011011000",
                    "00001110000",
                    "00011011000",
                    "00110001100",
                    "01100000110",
                    "11000000011",
                    "10000000001",
                ),
        ),
        GameLevel(
            id = "medium_9x9",
            solution =
                listOf(
                    "111000111",
                    "101101101",
                    "111111111",
                    "001111100",
                    "000111000",
                    "001111100",
                    "111111111",
                    "101101101",
                    "111000111",
                ),
        ),
    )

@Composable
@Preview(showBackground = true)
private fun LevelsScreenSmallPreview() {
    LevelsContent(
        state =
            LevelsState(
                selectedTab = Tab.SMALL,
                levelPacks = previewLevelPacks(),
            ),
        onTabSelect = {},
        onLevelClick = { _, _ -> },
    )
}

@Composable
@Preview(showBackground = true)
private fun LevelsScreenMediumPreview() {
    LevelsContent(
        state =
            LevelsState(
                selectedTab = Tab.MEDIUM,
                levelPacks = previewLevelPacks(),
            ),
        onTabSelect = {},
        onLevelClick = { _, _ -> },
    )
}

private fun previewLevelPacks(): ImmutableMap<String, LevelPack> =
    mapOf(
        Tab.SMALL.packId to LevelPack(levels = PreviewSmallLevels),
        Tab.MEDIUM.packId to LevelPack(levels = PreviewMediumLevels),
    ).toImmutableMap()
