package uz.qmgroup.pharmadealers.features.core

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row

class XLSXRowValidatorImpl : XLSXRowValidator {
    override fun validate(row: Row): Boolean {
        val idNumberFound = isIdNumberInRow(row)
        val nameFound = true
        val priceFound = true
        val providerFound = true
        return idNumberFound && nameFound && priceFound && providerFound
    }

    internal fun isIdNumberInRow(row: Row): Boolean {
        val cellIterator = row.cellIterator()
        while (cellIterator.hasNext()) {
            val cell = cellIterator.next()
            if (cell.isNumber() || cell.stringCellValue.trim().matches(Regex("^[0-9 -]+\$")))
                return true
        }
        return false
    }

    private fun Cell.isNumber(): Boolean {
        return try {
            numericCellValue
            true
        } catch (e: IllegalStateException) {
            false
        } catch (e: NumberFormatException) {
            false
        }
    }
}