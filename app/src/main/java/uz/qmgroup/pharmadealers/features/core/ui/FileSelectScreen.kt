package uz.qmgroup.pharmadealers.features.core.ui

import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import uz.qmgroup.pharmadealers.R


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FileSelectScreen(
    modifier: Modifier = Modifier,
    startImport: () -> Unit,
    addWorkbookToQueue: (Workbook) -> Unit,
    removeBook: (String) -> Unit,
    dealers: List<String>
) {
    val context = LocalContext.current

    val pickLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
    ) {
        it.map { fileUri ->
            WorkbookFactory.create(context.contentResolver.openInputStream(fileUri))
        }.forEach { workbook ->
            addWorkbookToQueue(workbook)
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Card(
            onClick = {
                pickLauncher.launch(
                    arrayOf(
                        "application/vnd.ms-excel",
                    ),
                )
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                Text(text = "Add files", modifier = Modifier.align(Alignment.Center))
            }
        }

        if (dealers.isNotEmpty()) {
            Text(
                text = stringResource(R.string.Detected_dealers_to_import),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp, bottom = 4.dp)
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(dealers) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 8.dp)
                                .weight(1f)
                        )

                        // TODO: To complete feature
//                        IconButton(onClick = {
//                            removeBook(it)
//                        }) {
//                            Icon(
//                                imageVector = Icons.Default.Close,
//                                contentDescription = null,
//                                tint = MaterialTheme.colorScheme.onSurface,
//                                modifier = Modifier.size(24.dp)
//                            )
//                        }
                    }
                }
            }
        } else {
            Text(
                text = "No dealers to import",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp, bottom = 4.dp),
            )
        }

        Button(
            onClick = startImport,
            modifier = Modifier.align(Alignment.End),
            enabled = dealers.isNotEmpty()
        ) {
            Text(text = "Next")
        }
    }
}

@Preview(name = "Import completed", showSystemUi = true, showBackground = true)
@Preview(
    name = "Import completed dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = true, showBackground = true
)
@Composable
fun FileSelectScreenPreview() {
    FileSelectScreen(
        startImport = {},
        addWorkbookToQueue = {},
        dealers = listOf(
            "OOO Pharm Group Gate",
            "OOO Pharm Group Gate",
            "OOO Pharm Group Gate",
        ),
        removeBook = {}
    )
}