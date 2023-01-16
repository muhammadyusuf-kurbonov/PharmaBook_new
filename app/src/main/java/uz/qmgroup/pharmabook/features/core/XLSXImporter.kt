package uz.qmgroup.pharmabook.features.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.Sheet
import uz.qmgroup.pharmabook.models.Medicine

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

    fun parse(): List<Medicine> {
        val medicines = mutableListOf<Medicine>()
        sheet.rowIterator().forEach {
            val medicine = parser.parse(it)
            if (medicine != null)
                medicines.add(medicine)
        }
        return medicines
    }

    suspend fun startImport(trunkBeforeImport: Boolean = false) = withContext(Dispatchers.IO) {
        if (trunkBeforeImport)
            storage.removeOldMedicines(parser.providerName)

        sheet.rowIterator().forEach {
            val medicine = parser.parse(it)
            if (medicine != null)
                launch {
                    storage.saveMedicine(medicine = medicine)
                }
        }
    }
}