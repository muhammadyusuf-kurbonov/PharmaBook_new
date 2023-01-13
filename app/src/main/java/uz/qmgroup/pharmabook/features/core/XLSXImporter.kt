package uz.qmgroup.pharmabook.features.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.Sheet

class XLSXImporter(
    val parser: XLSXParser,
    val sheet: Sheet,
    val storage: MedicineStorage
) {
    fun countMedicines(): Int {
        var count = 0
        sheet.rowIterator().forEach {
            if (parser.parse(it) != null) count++
        }
        return count
    }

    suspend fun startImport() = withContext(Dispatchers.IO) {
        sheet.rowIterator().forEach {
            val medicine = parser.parse(it)
            if (medicine != null)
                launch {
                    storage.saveMedicine(medicine = medicine)
                }
        }
    }
}