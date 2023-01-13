package uz.qmgroup.pharmabook.features.core.providers

import org.apache.poi.ss.usermodel.Row
import uz.qmgroup.pharmabook.features.core.XLSXParser
import uz.qmgroup.pharmabook.models.Medicine

class PharmGateGroupParser: XLSXParser {
    override val providerName: String
        get() = "Pharm Gate"

    override fun parse(row: Row): Medicine? {
        return try {
            Medicine(
                databaseId = 0,
                id = row.getCell(0).stringCellValue,
                price = row.getCell(4).numericCellValue,
                manufacturer = row.getCell(2).stringCellValue,
                name = row.getCell(1).stringCellValue,
                expireDate = row.getCell(5).stringCellValue,
                dealer = providerName
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}