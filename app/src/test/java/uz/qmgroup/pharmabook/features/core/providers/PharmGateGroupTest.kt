package uz.qmgroup.pharmabook.features.core.providers

import io.mockk.mockk
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.junit.Assert
import org.junit.Test
import uz.qmgroup.pharmabook.features.core.XLSXImporter

class PharmGateGroupTest {
    @Test
    fun testParser() {
        val workbook = WorkbookFactory.create(
            javaClass.classLoader?.getResourceAsStream("test-pharm-group.xls")
                ?: throw IllegalArgumentException()
        )

        val countMedicines = XLSXImporter(
            parser = PharmGateGroupParser(),
            sheet = workbook.first(),
            mockk()
        ).countMedicines()
        Assert.assertEquals(4321, countMedicines)
    }
}