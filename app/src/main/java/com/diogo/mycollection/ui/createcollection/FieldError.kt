package com.diogo.mycollection.ui.createcollection

sealed class FieldError {
    data object Title: FieldError()
    data object Category : FieldError()
}