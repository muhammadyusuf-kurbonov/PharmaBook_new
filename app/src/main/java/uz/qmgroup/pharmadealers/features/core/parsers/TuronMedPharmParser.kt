package uz.qmgroup.pharmadealers.features.core.parsers

import org.apache.poi.ss.usermodel.Row
import uz.qmgroup.pharmadealers.features.core.XLSXParser
import uz.qmgroup.pharmadealers.models.Medicine

class TuronMedPharmParser: XLSXParser {
    override val providerName: String
        get() = "ТУРОН МЕД ФАРМ"

    override fun parse(row: Row): Medicine? {
        return try {
            Medicine(
                databaseId = 0,
                id = row.rowNum.toString(),
                price = row.getCell(2).numericCellValue,
                manufacturer = row.getCell(4).stringCellValue,
                name = row.getCell(0).stringCellValue,
                expireDate = row.getCell(3).stringCellValue,
                dealer = providerName
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}