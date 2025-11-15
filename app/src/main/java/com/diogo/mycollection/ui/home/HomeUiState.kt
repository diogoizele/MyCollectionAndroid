package com.diogo.mycollection.ui.home

import com.diogo.mycollection.data.model.CollectionItem

sealed class HomeUiState {
    object Loading : HomeUiState()
    object Empty : HomeUiState()
    data class Success(val items: List<CollectionItem>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}