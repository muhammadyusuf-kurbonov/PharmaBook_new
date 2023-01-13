package uz.qmgroup.pharmabook.screens.app

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.WorkbookFactory
import uz.qmgroup.pharmabook.features.core.XLSXImporter
import uz.qmgroup.pharmabook.features.core.database.MedicineDatabase
import uz.qmgroup.pharmabook.features.core.database.MedicinesRepo
import uz.qmgroup.pharmabook.features.core.providers.PharmGateGroupParser

class AppViewModel: ViewModel() {
    fun startImport(context: Context, fireUri: Uri) {
        viewModelScope.launch {
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
                importer.startImport()
            }
        }
    }
}