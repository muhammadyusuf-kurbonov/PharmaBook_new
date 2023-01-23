package uz.qmgroup.pharmadealers.screens.importer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.qmgroup.pharmadealers.features.core.ui.FileSelectScreen
import uz.qmgroup.pharmadealers.features.core.ui.ImportCompletedScreen
import uz.qmgroup.pharmadealers.features.core.ui.ImportInProgressScreen

@Composable
fun ImporterScreen(modifier: Modifier = Modifier, viewModel: ImporterViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is ImportScreenState.AwaitFileSelect -> {
            val context = LocalContext.current

            FileSelectScreen(
                modifier = modifier,
                dealers = currentState.dealers,
                addWorkbookToQueue = viewModel::addWorkbookToQueue,
                startImport = { viewModel.startImport(context) },
                removeBook = { viewModel.excludeDealerFromImport(it) },
                errorSheets = currentState.invalidFiles
            )
        }

        is ImportScreenState.Analyzing -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text(text = "Analyzing ...")
                }
            }
        }

        is ImportScreenState.InProgress -> {
            ImportInProgressScreen(
                modifier = modifier,
                progresses = currentState.progresses
            )
        }

        is ImportScreenState.Completed -> {
            ImportCompletedScreen(
                modifier = modifier,
                total = currentState.total,
                onReady = viewModel::newImport
            )
        }
    }
}
