package com.diogo.mycollection.ui.profile

sealed class ProfileEvent {
    data object Logout : ProfileEvent()
    data class ShowError(val message: String) : ProfileEvent()
}