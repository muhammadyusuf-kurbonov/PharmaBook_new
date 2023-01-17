package uz.qmgroup.pharmadealers.features.core

class XLSXImporterTest {
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun startImport() {
//        val storage = mockk<MedicineStorage>(relaxed = true)
//        val workbook = WorkbookFactory.create(
//            javaClass.classLoader?.getResourceAsStream("test-pharm-group.xls")
//                ?: throw IllegalArgumentException()
//        )
//        val sheet = workbook.first()
//        val parser = UniversalSheetParser(sheet)
//
//        val importer = XLSXWorkbookImporter(
//            parser = parser,
//            sheet = sheet,
//            storage = storage
//        )
//
//        return runTest {
//            importer.startImport()
//
//            coVerify(exactly = 4321) { storage.saveMedicine(any()) }
//        }
//    }
//
//    @Test
//    fun `count medicines`() {
//        val storage = mockk<MedicineStorage>(relaxed = true)
//        val workbook = WorkbookFactory.create(
//            javaClass.classLoader?.getResourceAsStream("test-auto-import-1107.xls")
//                ?: throw IllegalArgumentException()
//        )
//        val sheet = workbook.first()
//        val parser = UniversalSheetParser(sheet)
//
//        val importer = XLSXWorkbookImporter(
//            parser = parser,
//            sheet = sheet,
//            storage = storage
//        )
//
//        val medicines = importer.parse()
//        assertEquals(1107, medicines.size)
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun startImportWithTrunk() {
//        val storage = mockk<MedicineStorage>(relaxed = true)
//        val workbook = WorkbookFactory.create(
//            javaClass.classLoader?.getResourceAsStream("test-pharm-group.xls")
//                ?: throw IllegalArgumentException()
//        )
//
//        val sheet = workbook.first()
//        val parser = UniversalSheetParser(sheet)
//
//        val importer = XLSXWorkbookImporter(
//            parser = parser,
//            sheet = sheet,
//            storage = storage
//        )
//
//        return runTest {
//            importer.startImport()
//
//            coVerify(exactly = 1) { storage.removeOldMedicines("OOO «Pharm Gate Group»") }
//            coVerify(exactly = 4321) { storage.saveMedicine(any()) }
//        }
//    }
}