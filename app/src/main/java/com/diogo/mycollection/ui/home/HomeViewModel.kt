package com.diogo.mycollection.ui.home

import androidx.lifecycle.ViewModel
import com.diogo.mycollection.data.model.CategoryType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val _selectedCategory = MutableStateFlow<CategoryType?>(null)
    val selectedCategory: StateFlow<CategoryType?> = _selectedCategory

    fun selectCategory(category: CategoryType?) {
        _selectedCategory.value = category
    }
}