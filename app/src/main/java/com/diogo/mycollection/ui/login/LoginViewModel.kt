package com.diogo.mycollection.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diogo.mycollection.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("Preencha todos os campos")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            val result = authRepository.login(email, password)
            _uiState.value = if (result.isSuccess) LoginUiState.Success
            else LoginUiState.Error("Erro ao autenticar")
        }
    }
}

