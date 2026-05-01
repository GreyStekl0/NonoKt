package dev.stekl0.nonokt.feature.game.impl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.stekl0.nonokt.feature.game.impl.R

private val DialogMinHeight = 220.dp

@Composable
internal fun GameFailedDialog(
    onLevelsClick: () -> Unit,
    onRestartClick: () -> Unit,
) {
    GameOutcomeDialogScaffold(
        title = R.string.feature_game_impl_game_failure_title,
        testTag = "game_failed_dialog",
        minHeight = DialogMinHeight,
    ) {
        Text(
            text = stringResource(R.string.feature_game_impl_game_failure_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(GameOutcomeDialogActionSpacing),
        ) {
            OutlinedButton(
                onClick = onLevelsClick,
                modifier = Modifier.weight(1f).testTag("game_failed_levels"),
            ) {
                Text(text = stringResource(R.string.feature_game_impl_game_completion_levels))
            }

            Button(
                onClick = onRestartClick,
                modifier = Modifier.weight(1f).testTag("game_failed_restart"),
            ) {
                Text(text = stringResource(R.string.feature_game_impl_game_failure_restart))
            }
        }
    }
}
