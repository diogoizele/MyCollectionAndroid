package com.diogo.mycollection.ui.createcollection

import com.diogo.mycollection.core.components.StarRatingView
import com.diogo.mycollection.data.model.CategoryType
import com.diogo.mycollection.data.model.ImageSource

data class CreateCollectionUiState (
    val title: String = "",
    val categoryType: CategoryType? = null,
    val author: String = "",
    val image: ImageSource = ImageSource.None,
    val isImageUrlValid: Boolean = true,
    val rating: Float = StarRatingView.DEFAULT_VALUE,
    val description: String = "",
    val isSaveEnabled: Boolean = false,
    val fieldError: FieldError? = null,
    val errorMessage: String? = null,
)