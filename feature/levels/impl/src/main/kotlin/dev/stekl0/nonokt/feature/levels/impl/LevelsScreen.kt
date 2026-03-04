package dev.stekl0.nonokt.feature.levels.impl

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.stekl0.nonokt.core.designsystem.icon.BorderAll
import dev.stekl0.nonokt.core.designsystem.icon.CropSquare
import dev.stekl0.nonokt.core.designsystem.icon.GridOn
import org.koin.compose.viewmodel.koinViewModel
import pro.respawn.flowmvi.api.IntentReceiver
import pro.respawn.flowmvi.compose.dsl.subscribe
import pro.respawn.flowmvi.compose.preview.EmptyReceiver

private val Tab.icon: ImageVector
    get() =
        when (this) {
            Tab.SMALL -> CropSquare
            Tab.MEDIUM -> BorderAll
            Tab.LARGE -> GridOn
        }

@Composable
internal fun LevelsScreen(viewModel: LevelsViewModel = koinViewModel()) =
    with(viewModel.store) {
        val state by subscribe { action ->
            when (action) {
                else -> TODO()
            }
        }

        LevelsScreenContent(state, onTabSelect = viewModel::onTabSelected)
    }

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun IntentReceiver<LevelsIntent>.LevelsScreenContent(
    state: LevelsState,
    onTabSelect: (Int) -> Unit,
) {
    when (state) {
        is LevelsState.Content -> {
            Scaffold(
                topBar = {
                    PrimaryTabRow(
                        selectedTabIndex = state.selectedTab.ordinal,
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                    ) {
                        Tab.entries.forEachIndexed { index, tab ->
                            Tab(
                                selected = state.selectedTab == tab,
                                onClick = { onTabSelect(index) },
                            ) {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(100.dp)
                                            .padding(top = 16.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        Icon(
                                            imageVector = tab.icon,
                                            contentDescription = null,
                                        )
                                        Text(
                                            text = tab.name,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.primary,
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
            ) { }
        }

        else -> {
            TODO()
        }
    }
}

@Composable
@Preview
private fun LevelsScreenPreview() =
    EmptyReceiver {
        LevelsScreenContent(LevelsState.Content(), {})
    }
