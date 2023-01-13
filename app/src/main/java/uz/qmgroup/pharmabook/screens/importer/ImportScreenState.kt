package uz.qmgroup.pharmabook.screens.importer

sealed class ImportScreenState {
    object Calculating : ImportScreenState()
    class InProgress(val percentage: Float): ImportScreenState()
    object AwaitFileSelect: ImportScreenState()
}
