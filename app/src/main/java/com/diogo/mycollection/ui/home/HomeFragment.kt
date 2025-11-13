package com.diogo.mycollection.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.diogo.mycollection.data.model.CategoryType
import com.diogo.mycollection.data.model.CollectionItem
import com.diogo.mycollection.databinding.FragmentHomeBinding
import com.diogo.mycollection.ui.home.adapters.CollectionAdapter
import kotlinx.coroutines.launch
import java.util.UUID

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var collectionAdapter: CollectionAdapter


    private fun mockCollections() = listOf(
        CollectionItem(UUID.randomUUID(), "O Senhor dos Anéis - A Sociedade do Anel", "J.R.R. Tolkien", CategoryType.BOOK, 4.9, "https://m.media-amazon.com/images/I/81MZ8OjmQrL._AC_UF1000,1000_QL80_.jpg"),
        CollectionItem(UUID.randomUUID(), "The Witcher 3", "CD Projekt Red", CategoryType.GAME, 4.7),
        CollectionItem(UUID.randomUUID(), "Interestelar", "Christopher Nolan", CategoryType.MOVIE, 4.8),
        CollectionItem(UUID.randomUUID(), "Dark Side of the Moon", "Pink Floyd", CategoryType.ALBUM, 4.9)
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategoryTabs()
        setupRecyclerCollectionView()
        observeCategorySelection()
    }

    private fun setupCategoryTabs() {
        binding.viewCategoryTabs.setOnCategorySelectedListener { category ->
            viewModel.selectCategory(category)
        }
    }

    private fun observeCategorySelection() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedCategory.collect { categoryType ->
                    // aqui você pode refletir a seleção na UI se necessário:
                    // binding.viewCategoryTabs.updateCategorySelection(categoryType)
                }
            }
        }
    }

    private fun setupRecyclerCollectionView() {
        val collections = mockCollections()

        collectionAdapter = CollectionAdapter(
            items = collections,
            onItemClick = { item ->
                println("Item clicado: ${item.title}")
            }
        )

        binding.recyclerViewCollections.apply {
            adapter = collectionAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}