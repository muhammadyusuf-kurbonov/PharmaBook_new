package uz.qmgroup.pharmadealers.features.core

import android.util.Log
import org.apache.poi.ss.usermodel.Workbook
import uz.qmgroup.pharmadealers.features.core.exceptions.ProviderNotIdentifiedException
import uz.qmgroup.pharmadealers.features.core.parsers.UniversalSheetParser
import uz.qmgroup.pharmadealers.features.core.utils.sheets

class XLSXWorkbooksParser(
    private val workbooks: List<Workbook>,
) {
    fun getAllAvailableProviders(): List<String> {
        val sheets = workbooks.flatMap { it.sheets() }
        return sheets.mapNotNull {
            try {
                UniversalSheetParser(it).providerName
            } catch (e: ProviderNotIdentifiedException) {
                Log.e(
                    "Importer",
                    "getAllAvailableProviders: Getting provider failed for sheet ${it.sheetName} from ${it.workbook.allNames}",
                    e
                )
                null
            }
        }
    }
}