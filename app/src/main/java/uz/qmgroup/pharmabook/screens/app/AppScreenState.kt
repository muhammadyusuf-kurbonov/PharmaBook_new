package uz.qmgroup.pharmabook.screens.app

sealed class AppScreenState {
    object SearchScreenActive: AppScreenState()
    object ImportScreenActive: AppScreenState()
}