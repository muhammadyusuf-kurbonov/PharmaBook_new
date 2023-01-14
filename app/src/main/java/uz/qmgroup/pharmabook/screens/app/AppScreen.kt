package uz.qmgroup.pharmabook.screens.app

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.qmgroup.pharmabook.R
import uz.qmgroup.pharmabook.features.core.database.MedicineDatabase
import uz.qmgroup.pharmabook.features.core.database.MedicinesRepo
import uz.qmgroup.pharmabook.screens.importer.ImporterScreen
import uz.qmgroup.pharmabook.screens.search.SearchScreen
import uz.qmgroup.pharmabook.screens.search.SearchViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AppScreen(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = viewModel(),
) {
    val currentState by viewModel.state.collectAsState()
    val context = LocalContext.current

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.app_name))
            })
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentState == AppScreenState.SearchScreenActive,
                    onClick = { viewModel.setScreen(AppScreenState.SearchScreenActive) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                )
                NavigationBarItem(
                    selected = currentState == AppScreenState.ImportScreenActive,
                    onClick = { viewModel.setScreen(AppScreenState.ImportScreenActive) },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_archive_24),
                            contentDescription = "Import"
                        )
                    }
                )
            }
        }
    ) { padding ->
        AnimatedContent(targetState = currentState) {
            when (it) {
                AppScreenState.ImportScreenActive -> {
                    ImporterScreen(modifier = Modifier
                        .fillMaxSize()
                        .padding(padding))
                }

                AppScreenState.SearchScreenActive -> {
                    SearchScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        viewModel = viewModel {
                            SearchViewModel(
                                MedicinesRepo(
                                    MedicineDatabase(context)
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}