package uz.qmgroup.pharmabook.features.core

import org.apache.poi.ss.usermodel.Row
import uz.qmgroup.pharmabook.models.Medicine

interface XLSXParser {
    fun firstValidRowId(): Int

    fun parse(row: Row): Medicine?
}