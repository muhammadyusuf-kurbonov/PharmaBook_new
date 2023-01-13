package uz.qmgroup.pharmabook.features.core

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

internal class XLSXRowValidatorImplTest {
    private lateinit var row: Row
    private lateinit var workbook: Workbook

    @Before
    fun loadExcel() {
        workbook = WorkbookFactory.create(
            this.javaClass.classLoader?.getResourceAsStream("test-xlsx.xlsx")
                ?: throw IllegalArgumentException()
        )
        row = workbook.first().getRow(13)
    }

    @Test
    fun validate() {
    }

    @Test
    fun isIdNumberInRow() {
        val idFound = XLSXRowValidatorImpl().isIdNumberInRow(row)
        assertEquals(true, idFound)

        val notFound = XLSXRowValidatorImpl().isIdNumberInRow(workbook.first().getRow(1))
        assertEquals(false, notFound)
    }

    @After
    fun close() {
        workbook.close()
    }
}