package uz.qmgroup.pharmadealers.features.core

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.junit.Test
import uz.qmgroup.pharmadealers.features.core.parsers.XLSXSheetParserImpl

class XLSXImporterTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `import SAIDA PHARM MEDSERVICE`() {
        val storage = mockk<MedicineStorage>(relaxed = true)
        val workbook = WorkbookFactory.create(
            javaClass.classLoader?.getResourceAsStream("test-3957.xls")
                ?: throw IllegalArgumentException()
        )
        val sheet = workbook.first()
        val parser = XLSXSheetParserImpl(sheet)

        val importer = XLSXWorkbookImporter(
            parser = parser,
            sheet = sheet,
            storage = storage
        )

        return runTest {
            importer.startImport()

            coVerify(exactly = 3957) { storage.saveMedicine(any()) }
        }
    }
}