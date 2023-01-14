package uz.qmgroup.pharmabook.screens.search

import uz.qmgroup.pharmabook.models.Medicine

sealed class SearchState {
    object Searching: SearchState()
    object EmptyResponse: SearchState()
    class ItemsFound(val items: List<Medicine>): SearchState()
}
