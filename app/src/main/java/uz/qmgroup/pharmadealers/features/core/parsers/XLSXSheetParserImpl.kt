package uz.qmgroup.pharmadealers.features.core.parsers

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import uz.qmgroup.pharmadealers.features.core.XLSXParser
import uz.qmgroup.pharmadealers.features.core.exceptions.ProviderNotIdentifiedException
import uz.qmgroup.pharmadealers.features.core.utils.stringValueOrNull
import uz.qmgroup.pharmadealers.models.Medicine
import kotlin.jvm.Throws

class XLSXSheetParserImpl @Throws(ProviderNotIdentifiedException::class) constructor(private val sheet: Sheet) : XLSXParser {


    private val parser: XLSXParser
    private val specialParsers: Map<String, XLSXParser> = mapOf(
        "AERO PHARM GROUP ООО" to AeroPharmGroupParser(),
        "ТУРОН МЕД ФАРМ" to TuronMedPharmParser()
    )

    override val providerName: String
        get() = parser.providerName

    @Throws(ProviderNotIdentifiedException::class)
    private fun parseProviderName(): String {
        val credentialsRegex = Regex("(ООО|МЧЖ|MCHJ|OOO|ФАРМ)", RegexOption.IGNORE_CASE)

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
        } else if (rawProviderName.contains(Regex("([0-9]{2})\\.([0-9]{2})\\.([0-9]{4})"))) {
            rawProviderName.replace(Regex("([0-9]{2})\\.([0-9]{2})\\.([0-9]{4})"), "").trim()
        } else {
            rawProviderName
        }

        return providerName
    }

    override fun parse(row: Row): Medicine? {
        return parser.parse(row)
    }

    init {
        val providerName = parseProviderName()
        parser = specialParsers[providerName] ?: UniversalSheetParser(sheet)
    }
}