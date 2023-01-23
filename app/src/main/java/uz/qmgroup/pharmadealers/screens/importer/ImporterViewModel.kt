package uz.qmgroup.pharmadealers.screens.importer

import android.content.Context
import android.util.Log
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
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import uz.qmgroup.pharmadealers.features.core.XLSXWorkbookImporter
import uz.qmgroup.pharmadealers.features.core.database.MedicineDatabase
import uz.qmgroup.pharmadealers.features.core.database.MedicinesRepo
import uz.qmgroup.pharmadealers.features.core.exceptions.HeaderNotFoundException
import uz.qmgroup.pharmadealers.features.core.exceptions.HeadersNotFoundException
import uz.qmgroup.pharmadealers.features.core.exceptions.ProviderNotIdentifiedException
import uz.qmgroup.pharmadealers.features.core.parsers.XLSXSheetParserImpl
import uz.qmgroup.pharmadealers.features.core.utils.sheets

class ImporterViewModel : ViewModel() {
    private val _state = MutableStateFlow<ImportScreenState>(
        ImportScreenState.AwaitFileSelect(emptyList(), emptyList())
    )
    val state = _state.asStateFlow()

    private val sheets = mutableListOf<Sheet>()
    private val mutex = Mutex()

    fun addWorkbookToQueue(book: Workbook, fileName: String) {
        Log.d("Importer", "addWorkbookToQueue() called with: book = $book, fileName = $fileName")
        if ((_state.value !is ImportScreenState.AwaitFileSelect) and (_state.value !is ImportScreenState.Analyzing))
            throw IllegalStateException("State must be awaiting file select")

        _state.update { ImportScreenState.Analyzing(it.dealers) }

        viewModelScope.launch {
            val errorSheets = mutableListOf(
                *(_state.value as? ImportScreenState.AwaitFileSelect)?.invalidFiles.orEmpty()
                    .toTypedArray()
            )

            book.use {
                it.sheets().forEach { sheet ->
                    try {
                        XLSXSheetParserImpl(sheet)

                        sheets.add(sheet)
                    } catch (e: ProviderNotIdentifiedException) {
                        errorSheets.add("$fileName -> ${sheet.sheetName}")
                    } catch (e: HeadersNotFoundException) {
                        errorSheets.add("$fileName -> ${sheet.sheetName}")
                    } catch (e: HeaderNotFoundException) {
                        errorSheets.add("$fileName -> ${sheet.sheetName}")
                    } catch (e: IllegalStateException) {
                        Log.e("XLSXParser", e.localizedMessage, e)
                        errorSheets.add("$fileName -> ${sheet.sheetName}")
                    }
                }
            }

            extractProvidersNames(errorSheets)
        }
    }

    fun excludeDealerFromImport(dealer: String) {
        TODO("Not implemented excluding providers yet")
//
//        if (_state.value !is ImportScreenState.AwaitFileSelect)
//            throw IllegalStateException("State must be awaiting file select")
//
//        extractProvidersNames()
    }

    fun newImport() {
        _state.update { ImportScreenState.AwaitFileSelect(emptyList(), emptyList()) }
    }

    fun setAnalysisStarted() {
        _state.update { ImportScreenState.Analyzing(it.dealers) }
    }

    fun startImport(context: Context) {
        _state.update { ImportScreenState.Analyzing(it.dealers) }
        viewModelScope.launch {
            val database = MedicineDatabase(context)
            val repository = MedicinesRepo(database)

            _state.update {
                ImportScreenState.InProgress(
                    mapOf(
                        *it.dealers.map { dealer ->
                            (dealer to 0f)
                        }.toTypedArray()
                    )
                )
            }

            val imports = sheets.map { sheet ->
                async(Dispatchers.IO) {
                    val parser = XLSXSheetParserImpl(sheet)
                    val importer = XLSXWorkbookImporter(
                        storage = repository,
                        sheet = sheet
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

    private fun extractProvidersNames(failedSheets: List<String>) {
        val allAvailableProviders = sheets.map { XLSXSheetParserImpl(it).providerName }

        _state.update {
            ImportScreenState.AwaitFileSelect(
                allAvailableProviders,
                failedSheets
            )
        }
    }
}