package uz.qmgroup.pharmabook.features.core.providers

import org.apache.poi.ss.usermodel.WorkbookFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class UniversalAutoParserTest {

    @Test
    fun `parse Aero Pharm Group`() {
        val workbook = WorkbookFactory.create(
            javaClass.classLoader?.getResourceAsStream("test-auto-import-1.xls")
                ?: throw IllegalArgumentException()
        )

        val sheet = workbook.first()
        val parser = UniversalAutoParser(sheet)
        assertEquals("AERO PHARM GROUP ООО", parser.providerName)
        val parseResult = parser.parse(sheet.getRow(9))
        assertNotNull(parseResult)
        assertEquals("Йодомарин 100мг №100", parseResult?.name)
        assertEquals(28791.43, parseResult?.price)
        assertEquals("Берлин Хеми", parseResult?.manufacturer)
    }

    @Test
    fun `parse Azimboy Mega Pharm`() {
        val workbook = WorkbookFactory.create(
            javaClass.classLoader?.getResourceAsStream("test-auto-import-1107.xls")
                ?: throw IllegalArgumentException()
        )

        val sheet = workbook.first()
        val parser = UniversalAutoParser(sheet)
        assertEquals(" \"АЗИМБОЙ МЕГА ФАРМ\" МЧЖ", parser.providerName)
        val parseResult = parser.parse(sheet.getRow(9))
        assertNotNull(parseResult)
        assertEquals(" Азедцин-500мг таб №3 (Азитромицин Авантика)", parseResult?.name)
        assertEquals(11900.0, parseResult?.price)
        assertEquals("авантика", parseResult?.manufacturer)
    }

    @Test
    fun `parse OOO Muhtasar`() {
        val workbook = WorkbookFactory.create(
            javaClass.classLoader?.getResourceAsStream("test-auto-import-4577.xlsx")
                ?: throw IllegalArgumentException()
        )

        val sheet = workbook.first()
        val parser = UniversalAutoParser(sheet)
        assertEquals("ООО \"МУХТАСАР\"  МЧЖ", parser.providerName)
        val parseResult = parser.parse(sheet.getRow(9))
        assertNotNull(parseResult)
        assertEquals("АБЕНОЛ  25МГ ТАБ N10", parseResult?.name)
        assertEquals(24100.0, parseResult?.price)
        assertEquals("УЗБЕКИСТАН", parseResult?.manufacturer)
    }

    @Test
    fun `parse OOO Pharm Gate`() {
        val workbook = WorkbookFactory.create(
            javaClass.classLoader?.getResourceAsStream("test-pharm-group.xls")
                ?: throw IllegalArgumentException()
        )

        val sheet = workbook.first()
        val parser = UniversalAutoParser(sheet)
        assertEquals("OOO «Pharm Gate Group»", parser.providerName)
        val parseResult = parser.parse(sheet.getRow(9))
        assertNotNull(parseResult)
        assertEquals("L- тироксин 100 таб №50", parseResult?.name)
        assertEquals(21600.0, parseResult?.price)
        assertEquals("Berlin-Chemie Германия", parseResult?.manufacturer)
    }
}