package dev.stekl0.nonokt.feature.game.impl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dev.stekl0.nonokt.feature.game.impl.R

private val DialogMinHeight = 220.dp
private val DialogPadding = 20.dp
private val DialogSpacing = 16.dp
private val DialogActionSpacing = 12.dp

@Composable
internal fun GameFailedDialog(
    onLevelsClick: () -> Unit,
    onRestartClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = {},
        properties =
            DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier =
                Modifier
                    .testTag("game_failed_dialog")
                    .fillMaxWidth()
                    .heightIn(min = DialogMinHeight),
        ) {
            Column(
                modifier = Modifier.padding(DialogPadding),
                verticalArrangement = Arrangement.spacedBy(DialogSpacing),
            ) {
                Text(
                    text = stringResource(R.string.game_failure_title),
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = stringResource(R.string.game_failure_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(DialogActionSpacing),
                ) {
                    OutlinedButton(
                        onClick = onLevelsClick,
                        modifier = Modifier.weight(1f).testTag("game_failed_levels"),
                    ) {
                        Text(text = stringResource(R.string.game_completion_levels))
                    }

                    Button(
                        onClick = onRestartClick,
                        modifier = Modifier.weight(1f).testTag("game_failed_restart"),
                    ) {
                        Text(text = stringResource(R.string.game_failure_restart))
                    }
                }
            }
        }
    }
}
