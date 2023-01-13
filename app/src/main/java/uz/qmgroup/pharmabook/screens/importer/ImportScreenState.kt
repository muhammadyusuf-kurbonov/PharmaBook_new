package uz.qmgroup.pharmabook.screens.importer

sealed class ImportScreenState {
    object AwaitFileSelect: ImportScreenState()
    object Calculating : ImportScreenState()
    class InProgress(val percentage: Float): ImportScreenState()
    class Completed(val total: Int): ImportScreenState()
}
