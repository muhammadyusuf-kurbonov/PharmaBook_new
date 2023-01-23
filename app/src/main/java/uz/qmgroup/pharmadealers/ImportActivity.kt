package uz.qmgroup.pharmadealers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.apache.poi.ss.usermodel.WorkbookFactory
import uz.qmgroup.pharmadealers.screens.importer.ImporterScreen
import uz.qmgroup.pharmadealers.screens.importer.ImporterViewModel
import uz.qmgroup.pharmadealers.ui.theme.PharmaDealersTheme


class ImportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            PharmaDealersTheme {
                val viewModel: ImporterViewModel = viewModel()

                fun handleUri(uri: Uri) {
                    val contentResolver = context.contentResolver
                    val workbook =
                        WorkbookFactory.create(contentResolver.openInputStream(uri))
                    val cursor = contentResolver.query(uri, null, null, null, null)
                    val fileName = cursor.use {
                        if (it != null && it.moveToFirst()) {
                            val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            it.getString(displayNameIndex)
                        } else {
                            "Unknown"
                        }
                    }
                    viewModel.addWorkbookToQueue(workbook, fileName)
                }

                LaunchedEffect(key1 = intent) {
                    viewModel.setAnalysisStarted()
                    when (intent.action) {
                        Intent.ACTION_SEND -> {
                            @Suppress("DEPRECATION")
                            val uri: Uri =
                                intent.getParcelableExtra(Intent.EXTRA_STREAM)
                                    ?: return@LaunchedEffect

                            handleUri(uri)
                        }

                        Intent.ACTION_SEND_MULTIPLE -> {
                            @Suppress("DEPRECATION")
                            val uris = intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
                                ?: return@LaunchedEffect

                            uris.forEach { handleUri(it) }
                        }
                    }
                }

                LaunchedEffect(key1 = Unit) {
                    addOnNewIntentListener { intent ->
                        viewModel.setAnalysisStarted()
                        when (intent.action) {
                            Intent.ACTION_SEND -> {
                                @Suppress("DEPRECATION")
                                val uri: Uri =
                                    intent.getParcelableExtra(Intent.EXTRA_STREAM)
                                        ?: return@addOnNewIntentListener

                                handleUri(uri)
                            }

                            Intent.ACTION_SEND_MULTIPLE -> {
                                @Suppress("DEPRECATION")
                                val uris =
                                    intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
                                        ?: return@addOnNewIntentListener

                                uris.forEach { handleUri(it) }
                            }
                        }
                    }
                }

                Column {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                    )

                    // A surface container using the 'background' color from the theme
                    ImporterScreen(
                        modifier = Modifier.fillMaxSize(),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
