package com.diogo.mycollection.ui.createcollection

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diogo.mycollection.data.model.CategoryType
import com.diogo.mycollection.data.model.CollectionItem
import com.diogo.mycollection.data.model.ImageSource
import com.diogo.mycollection.data.repository.CollectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class CreateCollectionViewModel(
    private val repository: CollectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateCollectionUiState())
    val uiState: StateFlow<CreateCollectionUiState> = _uiState

    fun onTitleChanged(value: String) {
        _uiState.update { it.copy(title = value) }
    }

    fun onCategoryChanged(value: CategoryType) {
        _uiState.update { it.copy(categoryType = value) }
    }

    fun onAuthorChanged(value: String) {
        _uiState.update { it.copy(author = value) }
    }

    fun onDescriptionChanged(value: String) {
        _uiState.update { it.copy(description = value) }
    }

    fun onRatingChanged(value: Float) {
        _uiState.update { it.copy(rating = value) }
    }

    fun onImageSelected(uri: Uri) {
        _uiState.update { it.copy(image = ImageSource.Local(uri.toString())) }
    }

    fun onSaveClicked() {

        println("On error clicked")
        val error = validate(_uiState.value)

        println("Error: $error")

        if (error != null) {
            _uiState.update { it.copy(
                fieldError = error.first,
                errorMessage = error.second
            ) }
            return
        }

        viewModelScope.launch {
            save(_uiState.value)
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null, fieldError = null) }
    }

    private fun validate(state: CreateCollectionUiState): Pair<FieldError, String>? {
        if (state.title.isBlank()) {
            return FieldError.Title to "Título é obrigatório"
        }

        if (state.categoryType == null) {
            return FieldError.Category to "Categoria é obrigatória"
        }

        return null
    }


    private suspend fun save(state: CreateCollectionUiState) {
        _uiState.update { it.copy(isSaving = true, errorMessage = null) }

        val item = toDomain(state)
        repository.save(item)

        _uiState.update { it.copy(isSaving = false, success = true) }
    }

    private fun toDomain(state: CreateCollectionUiState): CollectionItem = CollectionItem(
        id = UUID.randomUUID(),
        title = state.title,
        author = state.author,
        description = state.description,
        image = state.image,
        type = state.categoryType!!,
        rating = state.rating
    )

}