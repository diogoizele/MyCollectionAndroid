package com.diogo.mycollection.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diogo.mycollection.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    private val _events = MutableSharedFlow<ProfileEvent>()
    val events = _events.asSharedFlow()

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            try {
                authRepository.logout()
                _events.emit(ProfileEvent.Logout)
            } catch (e: Exception) {
                _events.emit(ProfileEvent.ShowError(e.message.toString()))
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }
}