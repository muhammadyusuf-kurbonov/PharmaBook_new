package uz.qmgroup.pharmadealers.features.core

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import uz.qmgroup.pharmadealers.features.core.parsers.XLSXSheetParserImpl
import uz.qmgroup.pharmadealers.features.core.utils.sheets

class XLSXWorkbookImporter(
    private val workbook: Workbook,
    private val storage: MedicineStorage,
    private val parserFactory: (Sheet) -> XLSXParser = { XLSXSheetParserImpl(it) }
) {
    private fun getAllSheets() = workbook.sheets()

    suspend fun countMedicines(): Int {
        return getAllSheets().map { parserFactory(it).parseSheet(it) }.merge().toList().size
    }

    suspend fun trunk() = withContext(Dispatchers.IO) {
        Log.d("Importer", "trunk() called")
        val providers = getAllSheets().map { parserFactory(it).providerName }
        providers.map {
            async {
                storage.removeOldMedicines(it)
            }
        }.awaitAll()
        Log.d("Importer", "trunk() completed")
    }

    suspend fun startImport() = withContext(Dispatchers.IO) {
        getAllSheets().map { parserFactory(it).parseSheet(it) }.merge().collect {
            storage.saveMedicine(it)
        }
    }
}