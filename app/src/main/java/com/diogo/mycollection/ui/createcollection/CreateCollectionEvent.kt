package com.diogo.mycollection.ui.createcollection

sealed class CreateCollectionEvent {
    object Loading : CreateCollectionEvent()
    object Success : CreateCollectionEvent()
    data class Error(val message: String) : CreateCollectionEvent()
}