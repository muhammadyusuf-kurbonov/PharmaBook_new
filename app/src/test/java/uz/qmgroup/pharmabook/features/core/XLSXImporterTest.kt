package uz.qmgroup.pharmabook.features.core

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.junit.Assert.*
import org.junit.Test
import uz.qmgroup.pharmabook.features.core.providers.PharmGateGroupParser

class XLSXImporterTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun startImport() {
        val storage = mockk<MedicineStorage>(relaxed = true)
        val parser = PharmGateGroupParser()
        val workbook = WorkbookFactory.create(
            javaClass.classLoader?.getResourceAsStream("test-pharm-group.xls")
                ?: throw IllegalArgumentException()
        )

        val importer = XLSXImporter(
            parser = parser,
            sheet = workbook.first(),
            storage = storage
        )

        return runTest {
            importer.startImport()

            coVerify(exactly = 4321) { storage.saveMedicine(any()) }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun startImportWithTrunk() {
        val storage = mockk<MedicineStorage>(relaxed = true)
        val parser = PharmGateGroupParser()
        val workbook = WorkbookFactory.create(
            javaClass.classLoader?.getResourceAsStream("test-pharm-group.xls")
                ?: throw IllegalArgumentException()
        )

        val importer = XLSXImporter(
            parser = parser,
            sheet = workbook.first(),
            storage = storage
        )

        return runTest {
            importer.startImport(
                trunkBeforeImport = true
            )

            coVerify(exactly = 1) { storage.removeOldMedicines("Pharm Gate") }
            coVerify(exactly = 4321) { storage.saveMedicine(any()) }
        }
    }
}