package uz.qmgroup.pharmabook.screens.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.qmgroup.pharmabook.R
import uz.qmgroup.pharmabook.screens.importer.ImporterScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(modifier: Modifier = Modifier, viewModel: AppViewModel = viewModel()) {
    Scaffold(modifier = modifier, topBar = {
        TopAppBar(title = {
            Text(text = stringResource(id = R.string.app_name))
        })
    }) {
        ImporterScreen(
            modifier = Modifier.padding(it)
        )
    }
}