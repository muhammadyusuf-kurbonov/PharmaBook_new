package uz.qmgroup.pharmabook.screens.importer

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.WorkbookFactory
import uz.qmgroup.pharmabook.features.core.XLSXImporter
import uz.qmgroup.pharmabook.features.core.database.MedicineDatabase
import uz.qmgroup.pharmabook.features.core.database.MedicinesRepo
import uz.qmgroup.pharmabook.features.core.providers.PharmGateGroupParser

class ImporterViewModel: ViewModel() {
    private val _state = MutableStateFlow<ImportScreenState>(ImportScreenState.AwaitFileSelect)
    val state = _state.asStateFlow()

    fun startImport(context: Context, fireUri: Uri) {
        viewModelScope.launch {
            _state.update { ImportScreenState.Calculating }
            val database = MedicineDatabase(context)
            val repository = MedicinesRepo(database)

            val parser = PharmGateGroupParser()

            val workbook = WorkbookFactory.create(
                context.contentResolver.openInputStream(fireUri)
            )

            workbook.use { book ->
                val importer = XLSXImporter(
                    storage = repository,
                    sheet = book.first(),
                    parser = parser
                )

                val count = importer.countMedicines()

                _state.update { ImportScreenState.InProgress(0f) }

                val counter = launch {
                    database.medicineDao.allMedicinesCount().collect {
                        val percentage = it / count * 100
                        _state.update { ImportScreenState.InProgress(percentage.toFloat()) }
                    }
                }

                importer.startImport()

                counter.cancel()
            }
        }
    }
}