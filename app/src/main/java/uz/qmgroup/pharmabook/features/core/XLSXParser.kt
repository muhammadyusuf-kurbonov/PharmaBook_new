package uz.qmgroup.pharmabook.features.core

import org.apache.poi.ss.usermodel.Row
import uz.qmgroup.pharmabook.models.Medicine

interface XLSXParser {
    val providerName: String

    fun parse(row: Row): Medicine?
}