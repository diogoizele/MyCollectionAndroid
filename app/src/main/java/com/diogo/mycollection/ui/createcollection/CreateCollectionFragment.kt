package com.diogo.mycollection.ui.createcollection

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.diogo.mycollection.core.extensions.clearFocusAndHideKeyboard
import com.diogo.mycollection.core.extensions.toDisplayName
import com.diogo.mycollection.data.model.CategoryType
import com.diogo.mycollection.databinding.FragmentCreateCollectionBinding
import java.io.File
import java.io.FileOutputStream

class CreateCollectionFragment : Fragment() {

    private var _binding: FragmentCreateCollectionBinding? = null
    private val binding get() = _binding!!
    private val pickImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult

        val data = result.data

        val galleryUri = data?.data
        if (galleryUri != null) {
            binding.imagePicker.showImage(galleryUri)
            return@registerForActivityResult
        }

        // Se veio da câmera
        val cameraBitmap = data?.extras?.get("data") as? Bitmap
        if (cameraBitmap != null) {
            val uri = saveBitmapToCache(cameraBitmap)
            binding.imagePicker.showImage(uri)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreateCollectionBinding.inflate(inflater, container, false)

        val root = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupCategoryDropdown()
        setupKeyboardDismiss()
    }


    private fun setupListeners() {
        binding.imagePicker.setOnPickRequest {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            val galleryIntent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }

            val chooser = Intent.createChooser(galleryIntent, null).apply {
                putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
            }

            pickImage.launch(chooser)
        }
    }

    private fun setupCategoryDropdown() {
        val items = listOf(
            CategoryType.GAME.toDisplayName(requireContext()),
            CategoryType.MOVIE.toDisplayName(requireContext()),
            CategoryType.BOOK.toDisplayName(requireContext()),
            CategoryType.ALBUM.toDisplayName(requireContext())
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            items
        )


        binding.dropdownSelectorCategory.setOnItemClickListener { _, _, _, _ ->
            binding.textFieldSelectorCategory.hint = null
        }

        binding.dropdownSelectorCategory.setAdapter(adapter)
    }

    private fun setupKeyboardDismiss() {
        binding.root.setOnClickListener { view ->
            clearFocusAndHideKeyboard()
        }
    }

    private fun saveBitmapToCache(bitmap: Bitmap): Uri {
        val file = File(requireContext().cacheDir, "captured_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            file
        )
    }

}