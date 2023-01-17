package uz.qmgroup.pharmadealers.features.core.utils

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row


fun Cell.stringValueOrNull(): String? = try {
    stringCellValue
} catch (e: IllegalStateException) {
    null
}

fun Row.findCellId(regex: Regex): Int? {
    return find { cell -> cell.stringCellValue.contains(regex) }?.columnIndex
}