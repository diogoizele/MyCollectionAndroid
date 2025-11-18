package com.diogo.mycollection.ui.createcollection

import com.diogo.mycollection.data.model.CategoryType
import com.diogo.mycollection.data.model.ImageSource

data class CreateCollectionUiState (
    val title: String = "",
    val categoryType: CategoryType? = null,
    val author: String = "",
    val image: ImageSource = ImageSource.None,
    val rating: Float = 1f,
    val description: String = "",
    val isSaveEnabled: Boolean = false,
    val isSaving: Boolean = false,
    val fieldError: FieldError? = null,
    val errorMessage: String? = null,
    val success: Boolean = false
)