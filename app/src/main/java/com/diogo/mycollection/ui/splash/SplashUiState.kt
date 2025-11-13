package com.diogo.mycollection.ui.splash

sealed class SplashUiState {
    object Loading : SplashUiState()
    object LoggedIn : SplashUiState()
    object LoggedOut : SplashUiState()
}