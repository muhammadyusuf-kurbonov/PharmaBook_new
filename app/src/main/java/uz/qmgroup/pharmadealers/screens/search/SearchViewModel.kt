package uz.qmgroup.pharmadealers.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.qmgroup.pharmadealers.features.core.database.MedicinesRepo

class SearchViewModel(
    private val repo: MedicinesRepo
): ViewModel() {
    private val _state = MutableStateFlow<SearchState>(SearchState.Searching)
    val state = _state.asStateFlow()

    fun search(query: String) {
        viewModelScope.launch {
            _state.update { SearchState.Searching }
            val items = repo.queryMedicines(query = query)
            if (items.isEmpty())
                _state.update { SearchState.EmptyResponse }
            else
                _state.update { SearchState.ItemsFound(items) }
        }
    }
}