package com.diogo.mycollection.core.extensions

import com.diogo.mycollection.R
import android.content.Context

import com.diogo.mycollection.data.model.CategoryType

fun CategoryType.toDisplayName(context: Context): String = when (this) {
    CategoryType.BOOK -> context.getString(R.string.category_book)
    CategoryType.MOVIE -> context.getString(R.string.category_movie)
    CategoryType.GAME -> context.getString(R.string.category_game)
    CategoryType.ALBUM -> context.getString(R.string.category_album)
}