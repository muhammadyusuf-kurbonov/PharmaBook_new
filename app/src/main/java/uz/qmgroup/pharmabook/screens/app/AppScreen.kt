package uz.qmgroup.pharmabook.screens.app

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.qmgroup.pharmabook.R
import uz.qmgroup.pharmabook.features.core.database.MedicineDatabase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(modifier: Modifier = Modifier, viewModel: AppViewModel = viewModel()) {
    val context = LocalContext.current

    val pickLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) {
        if (it != null) viewModel.startImport(context, it)
    }

    val count by MedicineDatabase(context).medicineDao.allMedicinesCount().collectAsState(initial = 0)

    Scaffold(modifier = modifier, topBar = {
        TopAppBar(title = {
            Text(text = stringResource(id = R.string.app_name))
        })
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {

            Button(onClick = {
                pickLauncher.launch(
                    arrayOf(
                        "application/vnd.ms-excel",
                        "*/*"
                    )
                )
            }) {
                Text(text = "Import file")
            }

            Text(text = "All medicines count = $count")
        }
    }
}