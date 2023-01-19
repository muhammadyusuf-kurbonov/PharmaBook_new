package uz.qmgroup.pharmadealers.screens.importer

sealed class ImportScreenState(val dealers: List<String>) {
    class AwaitFileSelect(dealers: List<String>) : ImportScreenState(dealers)
    class Analyzing(dealers: List<String>) : ImportScreenState(dealers)
    class InProgress(val progresses: Map<String, Float>) :
        ImportScreenState(progresses.keys.toList())

    class Completed(dealers: List<String>, val total: Int) : ImportScreenState(dealers)
}
