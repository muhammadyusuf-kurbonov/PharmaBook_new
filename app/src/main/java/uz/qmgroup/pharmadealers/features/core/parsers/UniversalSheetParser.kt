package uz.qmgroup.pharmadealers.features.core.parsers

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import uz.qmgroup.pharmadealers.features.core.XLSXParser
import uz.qmgroup.pharmadealers.features.core.exceptions.HeaderNotFoundException
import uz.qmgroup.pharmadealers.features.core.exceptions.HeadersNotFoundException
import uz.qmgroup.pharmadealers.features.core.exceptions.ProviderNotIdentifiedException
import uz.qmgroup.pharmadealers.features.core.utils.findCellId
import uz.qmgroup.pharmadealers.features.core.utils.stringValueOrNull
import uz.qmgroup.pharmadealers.models.Medicine

class UniversalSheetParser(private val sheet: Sheet) : XLSXParser {
    private val priceRegex = Regex("(Цена)|(тўлов)|(100%)", RegexOption.IGNORE_CASE)
    private val nameRegex =
        Regex("(Nomi)|(Номи)|(Наименование)|(Название)", RegexOption.IGNORE_CASE)
    private val manufacturerRegex =
        Regex("(Ishlab chiqaruvchi)|(Производитель)|(Ишлаб чиқарувчи)", RegexOption.IGNORE_CASE)

    private val specialParsers = mapOf(
        "AERO PHARM GROUP ООО" to AeroPharmGroupParser()
    )

    private val specialParser: XLSXParser?

    private val priceCellId: Int
    private val nameCellId: Int
    private val manufacturerCellId: Int
    override val providerName: String

    init {
        providerName = parseProviderName()
        specialParser = specialParsers[providerName]

        val headersRow = sheet.take(20).find { row ->
            row.any { cell -> cell.stringValueOrNull()?.contains(priceRegex) ?: false }
        } ?: throw HeadersNotFoundException()

        priceCellId = headersRow.findCellId(priceRegex)
            ?: throw IllegalStateException("Cell with price could not be found?!")
        nameCellId = headersRow.findCellId(nameRegex)
            ?: throw HeaderNotFoundException("Name")
        manufacturerCellId = headersRow.findCellId(manufacturerRegex)
            ?: throw HeaderNotFoundException("Manufacturer")
    }

    private fun parseProviderName(): String {
        val credentialsRegex = Regex("(ООО|МЧЖ|MCHJ|OOO)", RegexOption.IGNORE_CASE)

        val credentialsRow = sheet.take(20).find { row ->
            row.any { cell ->
                cell.stringValueOrNull()?.contains(credentialsRegex) ?: false
            }
        } ?: throw ProviderNotIdentifiedException()

        val rawProviderName = credentialsRow.find { cell ->
            cell.stringCellValue.contains(credentialsRegex)
        }?.stringCellValue
            ?: throw IllegalStateException("Cell with credentials could not be found?!")

        val providerName = if (rawProviderName.contains("\n")) {
            rawProviderName.split("\n").find {
                it.contains(credentialsRegex)
            } ?: throw IllegalStateException("Cell with credentials could not be found?!")
        } else {
            rawProviderName
        }

        return providerName
    }

    override fun parse(row: Row): Medicine? {
        if (specialParser != null)
            return specialParser.parse(row)
        try {
            val medicine = Medicine(
                databaseId = 0,
                id = row.getCell(1).toString(),
                price = row.getCell(priceCellId).numericCellValue,
                manufacturer = row.getCell(manufacturerCellId).stringCellValue,
                name = row.getCell(nameCellId).stringCellValue,
                expireDate = row.getCell(5).toString(),
                dealer = providerName
            )

            if (medicine.name.isEmpty())
                return null

            if (medicine.price == 0.0)
                return null

            return medicine
        } catch (e: IllegalStateException) {
            return null
        }
    }
}