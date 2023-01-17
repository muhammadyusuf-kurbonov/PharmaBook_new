package uz.qmgroup.pharmadealers.screens.importer

sealed class ImportScreenState {
    object AwaitFileSelect: ImportScreenState()
    object Analyzing : ImportScreenState()
    class InProgress(val progresses: Map<String, Float>): ImportScreenState()
    class Completed(val total: Int): ImportScreenState()
}
