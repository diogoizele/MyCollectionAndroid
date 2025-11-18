package com.diogo.mycollection.core.components

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.diogo.mycollection.databinding.ImagePickerViewBinding
import jp.wasabeef.glide.transformations.BlurTransformation

class ImagePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ImagePickerViewBinding.inflate(LayoutInflater.from(context), this)

    private var onPickRequest: (() -> Unit)? = null

    init {
        binding.root.setOnClickListener { onPickRequest?.invoke() }
    }

    fun setOnPickRequest(onPickRequest: () -> Unit) {
        this.onPickRequest = onPickRequest
    }

    fun showImage(uri: Uri) {
        binding.emptyState.visibility = GONE
        binding.imageState.visibility = VISIBLE

        Glide.with(context)
            .load(uri)
            .into(binding.selectedImage)

        Glide.with(context)
            .load(uri)
            .transform(BlurTransformation(20, 3))
            .into(binding.backgroundBlur)
    }

    fun showPlaceholder() {
        binding.emptyState.visibility = VISIBLE
        binding.imageState.visibility = GONE
    }
}