package com.diogo.mycollection.ui.createcollection

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.diogo.mycollection.core.extensions.clearFocusAndHideKeyboard
import com.diogo.mycollection.core.extensions.toDisplayName
import com.diogo.mycollection.data.model.CategoryType
import com.diogo.mycollection.data.model.ImageSource
import com.diogo.mycollection.data.source.local.InMemoryCollectionRepository
import com.diogo.mycollection.databinding.FragmentCreateCollectionBinding
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.diogo.mycollection.core.components.LabeledEditText
import com.diogo.mycollection.data.repository.ImageRepository
import com.diogo.mycollection.data.source.local.InMemoryImageRepository
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class CreateCollectionFragment : Fragment() {

    private lateinit var viewModel: CreateCollectionViewModel
    private lateinit var imageRepository: ImageRepository

    private var _binding: FragmentCreateCollectionBinding? = null
    private val binding get() = _binding!!

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult

        val data = result.data

        val galleryUri = data?.data
        if (galleryUri != null) {
            viewModel.onImageSelected(galleryUri)
            return@registerForActivityResult
        }

        val cameraBitmap = data?.extras?.get("data") as? Bitmap
        if (cameraBitmap != null) {
            val uri = imageRepository.save(cameraBitmap)
            viewModel.onImageSelected(uri)
            return@registerForActivityResult
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreateCollectionBinding.inflate(inflater, container, false)

        viewModel = CreateCollectionViewModel(repository = InMemoryCollectionRepository())

        imageRepository = InMemoryImageRepository(requireContext())

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { render(it) }
        }
    }

    private fun setupListeners() {
        binding.textFieldTitle.setOnTextChanged {
            viewModel.onTitleChanged(it)
            viewModel.clearError()

            binding.textFieldTitle.errorText = null
            binding.textFieldTitle.isActivated = false
        }

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

        binding.addItemButton.setOnClickListener {
            clearFocusAndHideKeyboard()
            renderError(viewModel.uiState.value)
            viewModel.onSaveClicked()

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


        binding.dropdownSelectorCategory.setOnItemClickListener { _, _, pos, _ ->
            viewModel.onCategoryChanged(CategoryType.entries[pos])
            viewModel.clearError()

            binding.textFieldSelectorCategory.hint = null

            binding.textFieldSelectorCategory.error = null
            binding.textFieldSelectorCategory.isActivated = false
            binding.dropdownSelectorCategory.error = null
            binding.dropdownSelectorCategory.clearFocus()
            binding.textFieldSelectorCategory.isErrorEnabled = false
            binding.labelSelectorCategory.error = null

            clearFocusAndHideKeyboard()
        }

        binding.dropdownSelectorCategory.setAdapter(adapter)
    }

    private fun setupKeyboardDismiss() {
        binding.root.setOnClickListener { view ->
            clearFocusAndHideKeyboard()
        }
    }

    private fun render(state: CreateCollectionUiState) {
        binding.progressBar.visibility = if (state.isSaving) View.VISIBLE else View.GONE

        resetAllErrors()
        renderError(state)

        when (val img = state.image) {
            is ImageSource.Local -> binding.imagePicker.showImage(img.path.toUri())
            is ImageSource.Remote -> binding.imagePicker.showImage(img.url.toUri())
            ImageSource.None -> binding.imagePicker.showPlaceholder()

        }

        if (state.success) {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun renderError(state: CreateCollectionUiState) {
        when (state.fieldError) {
            FieldError.Title -> showFieldError(state.errorMessage?: return, binding.textFieldTitle)
            FieldError.Category -> showFieldError(state.errorMessage ?: return, binding.textFieldSelectorCategory)
            null -> Unit
        }
    }

    private fun showFieldError(message: String, field: LabeledEditText) {
        field.isActivated = true
        field.errorText = message
        field.focus()
    }

    private fun showFieldError(message: String, field: TextInputLayout) {
        field.isErrorEnabled = true
        field.error = message
        field.isActivated = true
        field.requestFocus()
    }

    private fun resetAllErrors() {
        binding.textFieldTitle.errorText = null
        binding.textFieldTitle.isActivated = false

        binding.textFieldSelectorCategory.error = null
        binding.textFieldSelectorCategory.isActivated = false
        binding.dropdownSelectorCategory.error = null
    }

}