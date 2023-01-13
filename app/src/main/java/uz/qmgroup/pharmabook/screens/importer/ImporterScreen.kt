package uz.qmgroup.pharmabook.screens.importer

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImporterScreen(modifier: Modifier = Modifier, viewModel: ImporterViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    when (val currentState = state) {
        ImportScreenState.AwaitFileSelect -> {
            var selectedFileUri by remember {
                mutableStateOf<Uri?>(null)
            }

            val pickLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument(),
            ) {
                selectedFileUri = it
            }

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                val uri = selectedFileUri
                Card(
                    onClick = {
                        if (selectedFileUri == null) {
                            pickLauncher.launch(
                                arrayOf(
                                    "application/vnd.ms-excel",
                                    "*/*"
                                )
                            )
                        } else {
                            selectedFileUri = null
                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (uri == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp)
                        ) {
                            Text(text = "Select file", modifier = Modifier.align(Alignment.Center))
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp)
                        ) {
                            Text(
                                text = "${uri.lastPathSegment?.split("/")?.last()}",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }


                Button(onClick = {
                    viewModel.startImport(context = context, fireUri = uri!!)
                }, modifier = Modifier.align(Alignment.End), enabled = uri != null) {
                    Text(text = "Next")
                }
            }
        }

        ImportScreenState.Calculating -> {
            Box(
                modifier = modifier
                    .fillMaxHeight()
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
            Box(
                modifier = modifier
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LinearProgressIndicator(progress = currentState.percentage / 100)
                    Text(text = "Import ${currentState.percentage}")
                }
            }
        }
    }
}