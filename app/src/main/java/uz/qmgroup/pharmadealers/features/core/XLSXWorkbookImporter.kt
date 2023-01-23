package uz.qmgroup.pharmadealers.features.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.Sheet
import uz.qmgroup.pharmadealers.features.core.parsers.XLSXSheetParserImpl

class XLSXWorkbookImporter(
    private val sheet: Sheet,
    private val storage: MedicineStorage,
    private val parser: XLSXParser = XLSXSheetParserImpl(sheet)
) {

    suspend fun countMedicines(): Int {
        return parser.parseSheet(sheet).toList().size
    }

    suspend fun trunk() = withContext(Dispatchers.IO) {
        val provider = parser.providerName

        async {
            storage.removeOldMedicines(provider)
        }.await()
    }

    suspend fun startImport() = withContext(Dispatchers.IO) {
        parser.parseSheet(sheet).collect {
            storage.saveMedicine(it)
        }
    }
}