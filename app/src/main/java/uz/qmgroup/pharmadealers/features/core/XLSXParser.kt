package uz.qmgroup.pharmadealers.features.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import uz.qmgroup.pharmadealers.features.core.utils.sheets
import uz.qmgroup.pharmadealers.models.Medicine

interface XLSXParser {
    val providerName: String

    fun parse(row: Row): Medicine?

    fun parseSheet(sheet: Sheet): Flow<Medicine> {
        return flow {
            sheet.rowIterator().forEach {
                val medicine = parse(it)
                if (medicine != null)
                    emit(medicine)
            }
        }
    }

    fun parseBook(workbook: Workbook): Flow<Medicine> {
        return workbook.sheets().map { parseSheet(it) }.merge()
    }

    fun parseBooks(workbooks: List<Workbook>): Flow<Medicine> {
        val flows = mutableListOf<Flow<Medicine>>()
        workbooks.forEach { flows.add(parseBook(it)) }
        return merge(*flows.toTypedArray())
    }
}