package com.diogo.mycollection.core.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.diogo.mycollection.R
import com.diogo.mycollection.databinding.ViewLabeledEditTextBinding
import androidx.core.content.withStyledAttributes

class LabeledEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewLabeledEditTextBinding

    init {
        orientation = VERTICAL
        binding = ViewLabeledEditTextBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

        attrs?.let {
            context.withStyledAttributes(it, R.styleable.LabeledEditText, 0, 0) {
                val labelText = getString(R.styleable.LabeledEditText_labelText)
                val hintText = getString(R.styleable.LabeledEditText_placeholderText)

                binding.label.text = labelText
                binding.editText.hint = hintText


            }
        }
    }

    
    var text: String
        get() = binding.editText.text.toString()
        set(value) { binding.editText.setText(value) }
}
