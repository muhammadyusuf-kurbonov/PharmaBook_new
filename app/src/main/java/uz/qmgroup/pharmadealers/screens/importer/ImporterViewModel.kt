package uz.qmgroup.pharmadealers.screens.importer

import android.content.Context
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
import uz.qmgroup.pharmadealers.features.core.XLSXWorkbookImporter
import uz.qmgroup.pharmadealers.features.core.XLSXWorkbooksParser
import uz.qmgroup.pharmadealers.features.core.database.MedicineDatabase
import uz.qmgroup.pharmadealers.features.core.database.MedicinesRepo
import uz.qmgroup.pharmadealers.features.core.parsers.UniversalSheetParser

class ImporterViewModel : ViewModel() {
    private val _state = MutableStateFlow<ImportScreenState>(
        ImportScreenState.AwaitFileSelect(emptyList())
    )
    val state = _state.asStateFlow()

    private val books = mutableListOf<Workbook>()
    private val exceptedDealers = mutableListOf<String>()
    private val mutex = Mutex()

    fun addWorkbookToQueue(book: Workbook) {
        if (_state.value !is ImportScreenState.AwaitFileSelect)
            throw IllegalStateException("State must be awaiting file select")
        books.add(book)
        extractProvidersNames(true)
    }

    fun excludeDealerFromImport(dealer: String) {
        if (_state.value !is ImportScreenState.AwaitFileSelect)
            throw IllegalStateException("State must be awaiting file select")
        exceptedDealers.add(dealer)
        extractProvidersNames()
    }

    fun newImport() {
        _state.update { ImportScreenState.AwaitFileSelect(emptyList()) }
    }

    fun startImport(context: Context) {
        _state.update { ImportScreenState.Analyzing(it.dealers) }
        viewModelScope.launch {
            val database = MedicineDatabase(context)
            val repository = MedicinesRepo(database)

            val workbooks = books

            _state.update {
                ImportScreenState.InProgress(
                    mapOf(
                        *it.dealers.map { dealer ->
                            (dealer to 0f)
                        }.toTypedArray()
                    )
                )
            }

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
            _state.update { ImportScreenState.Completed(it.dealers, totalImported) }
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

    private fun extractProvidersNames(overrideExceptions: Boolean = false) {
        val parser = XLSXWorkbooksParser()
        _state.update {
            var allAvailableProviders = parser.getAllAvailableProviders(books)

            if (!overrideExceptions)
                allAvailableProviders =
                    allAvailableProviders.filter { dealer -> !exceptedDealers.contains(dealer) }
            ImportScreenState.AwaitFileSelect(
                allAvailableProviders
            )
        }
    }
}