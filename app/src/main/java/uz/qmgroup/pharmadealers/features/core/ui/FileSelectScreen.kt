package uz.qmgroup.pharmadealers.features.core.ui

import android.content.res.Configuration
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    addWorkbookToQueue: (Workbook, fileName: String) -> Unit,
    removeBook: (String) -> Unit,
    dealers: List<String>,
    errorSheets: List<String>,
) {
    val context = LocalContext.current

    var showBadge by remember {
        mutableStateOf(true)
    }

    val pickLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
    ) {
        it.map { fileUri ->
            val contentResolver = context.contentResolver

            val workbook =
                WorkbookFactory.create(contentResolver.openInputStream(fileUri))
            val cursor = contentResolver.query(fileUri, null, null, null, null)
            val fileName = cursor.use { currentCursor ->
                if (currentCursor != null && currentCursor.moveToFirst()) {
                    val displayNameIndex =
                        currentCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    currentCursor.getString(displayNameIndex)
                } else {
                    "Unknown"
                }
            }

            return@map fileName to workbook
        }.forEach { (fileName, workbook) ->
            addWorkbookToQueue(workbook, fileName)
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        AnimatedVisibility(errorSheets.isNotEmpty() && showBadge) {
            ElevatedCard(
                modifier = Modifier.padding(8.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.failed_to_recognize),
                            style = MaterialTheme.typography.titleSmall
                        )

                        IconButton(onClick = { showBadge = false }, modifier = Modifier.size(24.dp)) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    errorSheets.forEach {
                        Text(text = "\u2022 $it", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        Card(
            onClick = {
                pickLauncher.launch(
                    arrayOf(
                        "application/vnd.ms-excel",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
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
@Preview(
    name = "Import completed dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = true, showBackground = true, locale = "ru"
)
@Composable
fun FileSelectScreenPreview() {
    FileSelectScreen(
        startImport = {},
        addWorkbookToQueue = { _, _ -> },
        dealers = listOf(
            "OOO Pharm Group Gate",
            "OOO Pharm Group Gate",
            "OOO Pharm Group Gate",
        ),
        removeBook = {},
        errorSheets = listOf(
            "MacGregor.Name",
            "MacGregor.Name",
            "MacGregor.Name",
        )
    )
}