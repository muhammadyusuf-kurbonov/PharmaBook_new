package uz.qmgroup.pharmadealers.features.core.providers

import org.apache.poi.ss.usermodel.Row
import uz.qmgroup.pharmadealers.features.core.XLSXParser
import uz.qmgroup.pharmadealers.models.Medicine

class AeroPharmGroupParser: XLSXParser {
    override val providerName: String
        get() = "AERO PHARM GROUP ООО"

    override fun parse(row: Row): Medicine? {
        return try {
            Medicine(
                databaseId = 0,
                id = row.getCell(0).toString(),
                price = row.getCell(7).numericCellValue,
                manufacturer = row.getCell(9).stringCellValue,
                name = row.getCell(1).stringCellValue,
                expireDate = row.getCell(8).stringCellValue,
                dealer = providerName
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}