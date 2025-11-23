package com.diogo.mycollection.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.diogo.mycollection.R
import com.diogo.mycollection.data.model.CategoryType
import com.diogo.mycollection.data.model.CollectionItem
import com.diogo.mycollection.databinding.FragmentHomeBinding
import com.diogo.mycollection.ui.home.adapters.CollectionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels ()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var collectionAdapter: CollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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

        setupListeners()
        setupMenuProvider()
        observeUiState()
    }

    private fun setupListeners() {
        binding.viewCategoryTabs.setOnCategorySelectedListener { category ->
            viewModel.selectCategory(category)
        }

        binding.newItemFabButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_create_collection)
        }

        binding.emptyButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_create_collection)
        }

    }

    private fun setupRecyclerCollectionView(collections: List<CollectionItem>) {

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

    private fun setupMenuProvider() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.home_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when(menuItem.itemId) {
                        R.id.action_profile -> {
                            findNavController().navigate(R.id.navigation_profile)
                            true
                        }
                        R.id.action_search -> true
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is HomeUiState.Loading -> showLoading()
                        is HomeUiState.Success -> showItems(state.items)
                        is HomeUiState.Error -> showError(state.message)
                        is HomeUiState.Empty -> showEmptyState()
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.recyclerViewCollections.visibility = View.GONE
        binding.emptyStateContainer.visibility = View.GONE
    }

    private fun showItems(items: List<CollectionItem>) {
        binding.emptyStateContainer.visibility = View.GONE
        binding.recyclerViewCollections.visibility = View.VISIBLE

        setCategoryCounters(items)
        setupRecyclerCollectionView(items)
    }

    private fun showError(message: String) {
        binding.recyclerViewCollections.visibility = View.GONE
        binding.emptyStateContainer.visibility = View.VISIBLE

        binding.emptyTitle.text = "Erro ao carregar"
        binding.emptySubtitle.text = message
        binding.emptyButton.visibility = View.GONE
    }

    private fun showEmptyState() {
        binding.recyclerViewCollections.visibility = View.GONE
        binding.emptyStateContainer.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedCategory.collect { category ->
                    when(category) {
                        CategoryType.BOOK -> {
                            binding.emptyTitle.text = "Nenhum livro encontrado"
                            binding.emptySubtitle.text = "Adicione um livro para começar"
                        }
                        CategoryType.MOVIE -> {
                            binding.emptyTitle.text = "Nenhum filme encontrado"
                            binding.emptySubtitle.text = "Adicione um filme para começar"
                        }
                        CategoryType.GAME -> {
                            binding.emptyTitle.text = "Nenhum jogo encontrado"
                            binding.emptySubtitle.text = "Adicione um jogo para começar"
                        }
                        else -> {
                            binding.emptyTitle.text = "Nenhum item encontrado"
                            binding.emptySubtitle.text = "Adicione um item para começar"
                        }
                    }
                }
            }
        }

        binding.emptyButton.visibility = View.VISIBLE
    }


    private fun setCategoryCounters(items: List<CollectionItem>) {
        var booksQuantity = 0
        var moviesQuantity = 0
        var gamesQuantity = 0

        items.forEach { item ->
            when(item.type) {
                CategoryType.BOOK -> booksQuantity++
                CategoryType.MOVIE -> moviesQuantity++
                CategoryType.GAME -> gamesQuantity++
                else -> {}
            }
        }

        binding.counterBooks.setCountText(booksQuantity.toString())
        binding.counterMovies.setCountText(moviesQuantity.toString())
        binding.counterGames.setCountText(gamesQuantity.toString())
    }
}
