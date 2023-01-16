package uz.qmgroup.pharmabook.features.core.utils

import org.apache.poi.ss.usermodel.Cell

fun Cell.stringValueOrNull(): String? = try {
    stringCellValue
} catch (e: IllegalStateException) {
    null
}