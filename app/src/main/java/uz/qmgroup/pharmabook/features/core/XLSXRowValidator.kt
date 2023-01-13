package uz.qmgroup.pharmabook.features.core

import org.apache.poi.ss.usermodel.Row

interface XLSXRowValidator {
    fun validate(row: Row): Boolean
}