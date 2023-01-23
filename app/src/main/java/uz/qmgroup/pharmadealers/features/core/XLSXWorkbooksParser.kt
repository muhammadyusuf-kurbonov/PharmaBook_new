package uz.qmgroup.pharmadealers.features.core

import android.util.Log
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import uz.qmgroup.pharmadealers.features.core.exceptions.ProviderNotIdentifiedException
import uz.qmgroup.pharmadealers.features.core.parsers.XLSXSheetParserImpl
import uz.qmgroup.pharmadealers.features.core.utils.sheets

class XLSXWorkbooksParser(val parserGenerator: (Sheet) -> XLSXParser = ::XLSXSheetParserImpl) {
    fun getAllAvailableProviders(workbooks: List<Workbook>): List<String> {
        val sheets = workbooks.flatMap { it.sheets() }
        return sheets.mapNotNull {
            try {
                parserGenerator(it).providerName
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