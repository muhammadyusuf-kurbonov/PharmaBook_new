package uz.qmgroup.pharmadealers.screens.app

sealed class AppScreenState {
    object SearchScreenActive: AppScreenState()
    object ImportScreenActive: AppScreenState()
}