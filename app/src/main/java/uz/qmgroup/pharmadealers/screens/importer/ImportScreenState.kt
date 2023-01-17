package uz.qmgroup.pharmadealers.screens.importer

sealed class ImportScreenState {
    object AwaitFileSelect: ImportScreenState()
    object Calculating : ImportScreenState()
    class InProgress(val percentage: Float): ImportScreenState()
    class Completed(val total: Int, val dealerName: String): ImportScreenState()
}
