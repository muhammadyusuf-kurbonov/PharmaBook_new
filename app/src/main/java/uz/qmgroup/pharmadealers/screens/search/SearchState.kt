package uz.qmgroup.pharmadealers.screens.search

import uz.qmgroup.pharmadealers.models.Medicine

sealed class SearchState {
    object Searching: SearchState()
    object EmptyResponse: SearchState()
    class ItemsFound(val items: List<Medicine>): SearchState()
}
