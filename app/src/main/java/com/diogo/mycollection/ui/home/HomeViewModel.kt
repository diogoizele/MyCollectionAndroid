package com.diogo.mycollection.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diogo.mycollection.data.model.CategoryType
import com.diogo.mycollection.data.repository.CollectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val collectionRepository: CollectionRepository
) : ViewModel() {


    private val _selectedCategory = MutableStateFlow<CategoryType?>(null)
    val selectedCategory: StateFlow<CategoryType?> = _selectedCategory


    val uiState: StateFlow<HomeUiState> = combine(
        collectionRepository.observeCollectionItems(selectedCategory.value),
        selectedCategory
    ) { items, category ->
        val filtered = category?.let { cat ->
            items.filter { it.type == cat }
        } ?: items

        if (filtered.isEmpty()) HomeUiState.Empty
        else HomeUiState.Success(filtered)
    }
    .catch { error ->
        emit(HomeUiState.Error(error.message ?: "Unknown error"))
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )

    fun selectCategory(category: CategoryType?) {
        _selectedCategory.value = category
    }
}