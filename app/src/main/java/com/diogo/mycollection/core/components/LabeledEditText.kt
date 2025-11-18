package com.diogo.mycollection.core.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.diogo.mycollection.R
import com.diogo.mycollection.databinding.ViewLabeledEditTextBinding
import androidx.core.content.withStyledAttributes
import androidx.core.widget.addTextChangedListener

class LabeledEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewLabeledEditTextBinding

    private var onIconEndClickListener: (() -> Unit)? = null

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
                val iconRes = getResourceId(R.styleable.LabeledEditText_iconEnd, 0)

                binding.label.text = labelText
                binding.editText.hint = hintText
                binding.editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconRes, 0)
            }
        }

        binding.editText.setOnTouchListener { _: View, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawable = binding.editText.compoundDrawables[2]
                if (drawable != null) {
                    val touchStart = binding.editText.width - binding.editText.paddingRight - drawable.bounds.width()
                    if (event.x >= touchStart) {
                        onIconEndClickListener?.invoke()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
    }


    var text: String
        get() = binding.editText.text.toString()
        set(value) { binding.editText.setText(value) }

    fun setOnIconEndClickListener(listener: () -> Unit) {
        onIconEndClickListener = listener
    }

    fun setOnTextChanged(listener: (String) -> Unit) {
        binding.editText.addTextChangedListener {
            listener(it.toString())
        }
    }

    var errorText: String?
        get() = binding.errorText.text.toString()
        set(value) {
            binding.errorText.text = value

            val hasError = !value.isNullOrBlank()
            binding.errorText.visibility = if (hasError) VISIBLE else GONE

            if (hasError) {
                binding.label.setTextColor(context.getColor(R.color.error))
            } else {
                binding.label.setTextColor(context.getColor(R.color.neutral_900))
            }
        }

    fun focus() {
        binding.editText.requestFocus()
    }
}
