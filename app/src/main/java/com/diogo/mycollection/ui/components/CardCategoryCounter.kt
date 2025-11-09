package com.diogo.mycollection.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.diogo.mycollection.R
import com.diogo.mycollection.databinding.ViewCardCategoryCounterBinding

class CardCategoryCounter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : LinearLayout (context, attrs, defStyleAttr) {

    private val binding = ViewCardCategoryCounterBinding.inflate(
        LayoutInflater.from(context), this, true
    )
    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        background = ContextCompat.getDrawable(context, R.drawable.bg_card_category_counter)

        binding.count.text = COUNTER_INITIAL_VALUE.toString()

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CardCategoryCounter,
            0, 0
        ).apply {
            try {
                getResourceId(R.styleable.CardCategoryCounter_icon, 0).takeIf { it != 0 }?.let {
                    setIcon(it)
                }
                getString(R.styleable.CardCategoryCounter_countText)?.let {
                    setCountText(it)
                }
                getString(R.styleable.CardCategoryCounter_labelText)?.let {
                    setLabelText(it)
                }
            } finally {
                recycle()
            }

        }
    }

    companion object {
        const val COUNTER_INITIAL_VALUE = 0
    }

    fun setIcon(@DrawableRes iconRes: Int) {
        binding.icon.setImageResource(iconRes)
    }

    fun setCountText(text: String) {
        binding.count.text = text
    }

    fun setLabelText(text: String) {
        binding.label.text = text
    }
}