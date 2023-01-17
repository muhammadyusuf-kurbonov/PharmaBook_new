package uz.qmgroup.pharmadealers.features.core

import org.apache.poi.ss.usermodel.Row
import uz.qmgroup.pharmadealers.models.Medicine

interface XLSXParser {
    val providerName: String

    fun parse(row: Row): Medicine?
}