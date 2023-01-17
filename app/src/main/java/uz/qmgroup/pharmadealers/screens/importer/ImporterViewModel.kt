package uz.qmgroup.pharmadealers.screens.importer

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import uz.qmgroup.pharmadealers.features.core.XLSXWorkbookImporter
import uz.qmgroup.pharmadealers.features.core.XLSXWorkbooksParser
import uz.qmgroup.pharmadealers.features.core.database.MedicineDatabase
import uz.qmgroup.pharmadealers.features.core.database.MedicinesRepo
import uz.qmgroup.pharmadealers.features.core.parsers.UniversalSheetParser

class ImporterViewModel : ViewModel() {
    private val _state = MutableStateFlow<ImportScreenState>(ImportScreenState.AwaitFileSelect)
    val state = _state.asStateFlow()

    private val mutex = Mutex()

    fun newImport() {
        _state.update { ImportScreenState.AwaitFileSelect }
    }

    fun startImport(context: Context, filesUris: List<Uri>) {
        viewModelScope.launch {
            _state.update { ImportScreenState.Analyzing }
            val database = MedicineDatabase(context)
            val repository = MedicinesRepo(database)

            val workbooks = filesUris.map {
                WorkbookFactory.create(
                    context.contentResolver.openInputStream(it)
                )
            }

            _state.update { ImportScreenState.Analyzing }

            extractProvidersNames(workbooks = workbooks)

            val imports = workbooks.map { workbook ->
                async(Dispatchers.IO) {
                    workbook.use { book ->
                        val sheet = book.first()
                        val parser = UniversalSheetParser(sheet)
                        val importer = XLSXWorkbookImporter(
                            storage = repository,
                            workbook = book
                        )

                        val total = importer.countMedicines()

                        importer.trunk()

                        val counter = launch {
                            database.medicineDao.dealersMedicinesCount(parser.providerName)
                                .collect {
                                    val percentage = (it + 1).toFloat() / total.toFloat()
                                    updateProgress(parser.providerName, percentage)
                                }
                        }

                        importer.startImport()

                        counter.cancel()

                        return@async total
                    }
                }
            }
            val totalImported = imports.awaitAll().sumOf { it }
            _state.update { ImportScreenState.Completed(totalImported) }
        }
    }

    private suspend fun updateProgress(dealer: String, progress: Float) {
        mutex.withLock {
            val currentState = _state.value
            if (currentState !is ImportScreenState.InProgress) throw IllegalStateException()
            val progresses = currentState.progresses.toMutableMap()
            progresses[dealer] = progress
            _state.emit(ImportScreenState.InProgress(progresses))
        }
    }

    private fun extractProvidersNames(workbooks: List<Workbook>) {
        val parser = XLSXWorkbooksParser(workbooks)
        _state.update {
            val progresses = mutableMapOf<String, Float>()
            parser.getAllAvailableProviders().forEach {
                progresses[it] = 0f
            }

            ImportScreenState.InProgress(progresses)
        }
    }
}