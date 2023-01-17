package uz.qmgroup.pharmadealers.screens.importer

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.WorkbookFactory
import uz.qmgroup.pharmadealers.features.core.XLSXImporter
import uz.qmgroup.pharmadealers.features.core.database.MedicineDatabase
import uz.qmgroup.pharmadealers.features.core.database.MedicinesRepo
import uz.qmgroup.pharmadealers.features.core.providers.UniversalAutoParser

class ImporterViewModel: ViewModel() {
    private val _state = MutableStateFlow<ImportScreenState>(ImportScreenState.AwaitFileSelect)
    val state = _state.asStateFlow()

    fun newImport() {
        _state.update { ImportScreenState.AwaitFileSelect }
    }

    fun startImport(context: Context, fireUri: Uri) {
        viewModelScope.launch {
            _state.update { ImportScreenState.Calculating }
            val database = MedicineDatabase(context)
            val repository = MedicinesRepo(database)

            val workbook = WorkbookFactory.create(
                context.contentResolver.openInputStream(fireUri)
            )

            workbook.use { book ->
                val sheet = book.first()
                val parser = UniversalAutoParser(sheet)
                val importer = XLSXImporter(
                    storage = repository,
                    sheet = sheet,
                    parser = parser
                )

                val total = importer.countMedicines()

                _state.update { ImportScreenState.InProgress(0f) }

                val counter = launch {
                    database.medicineDao.dealersMedicinesCount(parser.providerName).collect {
                        val percentage = (it+1).toFloat() / total.toFloat()
                        _state.update { ImportScreenState.InProgress(percentage) }
                    }
                }

                importer.startImport(trunkBeforeImport = true)

                _state.update { ImportScreenState.Completed(total, parser.providerName) }

                counter.cancel()
            }
        }
    }
}