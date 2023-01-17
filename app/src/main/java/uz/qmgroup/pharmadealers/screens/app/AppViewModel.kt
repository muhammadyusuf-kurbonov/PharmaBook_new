package uz.qmgroup.pharmadealers.screens.app

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel: ViewModel() {
    private val _state = MutableStateFlow<AppScreenState>(AppScreenState.SearchScreenActive)
    val state = _state.asStateFlow()

    fun setScreen(screenState: AppScreenState) {
        _state.update { screenState }
    }
}