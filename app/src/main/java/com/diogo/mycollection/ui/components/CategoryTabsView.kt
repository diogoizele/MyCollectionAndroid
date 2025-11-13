package com.diogo.mycollection.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.diogo.mycollection.databinding.ViewCategoryTabsBinding
import com.diogo.mycollection.data.model.CategoryType

class CategoryTabsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewCategoryTabsBinding.inflate(LayoutInflater.from(context), this)

    private var onCategorySelectedListener: ((CategoryType?) -> Unit)? = null

    init {
        setupChips()
    }

    private fun setupChips() {
        binding.chipAll.isChecked = true
        binding.categoryChipGroup.setOnCheckedStateChangeListener { isTransitionGroup,  checkedIds ->
            val selectedChipId = checkedIds.firstOrNull() ?: return@setOnCheckedStateChangeListener
            val categoryType = when (selectedChipId) {
                binding.chipAll.id -> null
                binding.chipBooks.id -> CategoryType.BOOK
                binding.chipMovies.id -> CategoryType.MOVIE
                binding.chipGames.id -> CategoryType.GAME
                else -> null
            }
            onCategorySelectedListener?.invoke(categoryType)
        }
    }

    fun setOnCategorySelectedListener(listener: (CategoryType?) -> Unit) {
        onCategorySelectedListener = listener
    }
}