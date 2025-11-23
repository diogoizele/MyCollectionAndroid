package com.diogo.mycollection.ui.createcollection

import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.diogo.mycollection.core.extensions.clearFocusAndHideKeyboard
import com.diogo.mycollection.core.extensions.toDisplayName
import com.diogo.mycollection.data.model.CategoryType
import com.diogo.mycollection.data.model.ImageSource
import com.diogo.mycollection.databinding.FragmentCreateCollectionBinding
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.diogo.mycollection.core.components.LabeledEditText
import com.diogo.mycollection.core.network.RemoteImageValidator
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateCollectionFragment : Fragment() {

    private val viewModel: CreateCollectionViewModel by viewModels()

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
            viewModel.onImagePicked(cameraBitmap)
            return@registerForActivityResult
        }
    }

    private var suppress = false
    private var lastRequestId = 0

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
        setupEvents()
        setupCategoryDropdown()
        setupKeyboardDismiss()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { render(it) }
        }
    }

    private fun setupListeners() {

        binding.textFieldUrlImage.setOnTextChanged {
            if (suppress) return@setOnTextChanged
            viewModel.onImageUrlChanged(it)
        }
        binding.textFieldUrlImage.setOnIconEndClickListener {
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = clipboard.primaryClip
            val item = clip?.getItemAt(0)
            val text = item?.text?.toString()

            if (text != null) {
                binding.textFieldUrlImage.text = text
                setupImageUrlValidation(text)
            }
        }

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

            clearErrorWhenPickImage()

            pickImage.launch(chooser)
        }

        binding.addItemButton.setOnClickListener {
            clearFocusAndHideKeyboard()
            renderError(viewModel.uiState.value)
            viewModel.onSaveClicked()
        }

        binding.ratingView.setOnRatingChangeListener {
            viewModel.onRatingChanged(it)
        }

        binding.textFieldAuthor.setOnTextChanged {
            viewModel.onAuthorChanged(it)
        }

        binding.textFieldDescription.addTextChangedListener {
            viewModel.onDescriptionChanged(it.toString())
        }
    }

    private fun setupEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when(event) {
                        is CreateCollectionEvent.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is CreateCollectionEvent.Success -> {
                            binding.progressBar.visibility = View.GONE
                            findNavController().popBackStack()
                        }
                        is CreateCollectionEvent.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), event.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupCategoryDropdown() {
        val items = CategoryType.entries.map { it.toDisplayName(requireContext()) }

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


    private fun setupImageUrlValidation(url: String) {
        val requestId = ++lastRequestId

        lifecycleScope.launch {
            val isValid = RemoteImageValidator.isValidImageUrl(url)

            if (requestId != lastRequestId) return@launch

            if (!isValid) {
                binding.textFieldUrlImage.isActivated = true
                binding.textFieldUrlImage.errorText = "URL inválida"
                viewModel.setImageNone()
            } else {
                binding.textFieldUrlImage.isActivated = false
                binding.textFieldUrlImage.errorText = null
                viewModel.setImageRemote(url)
            }
        }
    }

    private fun setupKeyboardDismiss() {
        binding.root.setOnClickListener { view ->
            clearFocusAndHideKeyboard()
        }
    }

    private fun render(state: CreateCollectionUiState) {

        resetAllErrors()
        renderError(state)

        when (val img = state.image) {
            is ImageSource.Local -> {
                binding.imagePicker.showImage(img.path.toUri())
                binding.textFieldUrlImage.isActivated = false
                binding.textFieldUrlImage.errorText = null
                binding.textFieldUrlImage.clearFocus()
                clearTextSilently()
                clearErrorWhenPickImage()
            }
            is ImageSource.Remote -> binding.imagePicker.showImage(img.url.toUri())
            ImageSource.None -> binding.imagePicker.showPlaceholder()
        }
    }

    private fun renderError(state: CreateCollectionUiState) {
        when (state.fieldError) {
            FieldError.Title -> showFieldError(state.errorMessage?: return, binding.textFieldTitle)
            FieldError.Category -> showFieldError(state.errorMessage ?: return, binding.textFieldSelectorCategory)
            FieldError.ImageUrl -> showFieldError(state.errorMessage ?: return, binding.textFieldUrlImage)
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



    private fun clearErrorWhenPickImage() {
        val state = viewModel.uiState.value

        if (state.fieldError != null && state.fieldError is FieldError.ImageUrl) {
            viewModel.clearError()
        }
    }

    private fun clearTextSilently() {
        suppress = true
        binding.textFieldUrlImage.text = ""
        suppress = false
    }
}