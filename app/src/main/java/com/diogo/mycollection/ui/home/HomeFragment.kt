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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.diogo.mycollection.R
import com.diogo.mycollection.data.model.CategoryType
import com.diogo.mycollection.data.model.CollectionItem
import com.diogo.mycollection.data.source.local.DatabaseProvider
import com.diogo.mycollection.data.source.local.RoomCollectionRepository
import com.diogo.mycollection.databinding.FragmentHomeBinding
import com.diogo.mycollection.ui.home.adapters.CollectionAdapter
import kotlinx.coroutines.launch
import java.util.UUID

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var collectionAdapter: CollectionAdapter


    private fun mockCollections() = listOf(
        CollectionItem(
            UUID.randomUUID(),
            "O Senhor dos Anéis - A Sociedade do Anel",
            "J.R.R. Tolkien",
            CategoryType.BOOK,
            4.9,
            "https://m.media-amazon.com/images/I/81MZ8OjmQrL._AC_UF1000,1000_QL80_.jpg"
        ),
        CollectionItem(
            UUID.randomUUID(),
            "The Witcher 3",
            "CD Projekt Red",
            CategoryType.GAME,
            4.7
        ),
        CollectionItem(
            UUID.randomUUID(),
            "Interestelar",
            "Christopher Nolan",
            CategoryType.MOVIE,
            4.8
        ),
        CollectionItem(
            UUID.randomUUID(),
            "Dark Side of the Moon",
            "Pink Floyd",
            CategoryType.ALBUM,
            4.9
        )
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = HomeViewModel(RoomCollectionRepository(DatabaseProvider.getDatabase(requireContext())))

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
        setupRecyclerCollectionView()
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
        println("Loading chamado as ${System.currentTimeMillis()}")
    }
    private fun showItems(items: List<CollectionItem>) {
        println("Items chamados as ${System.currentTimeMillis()}")
    }

    private fun showError(message: String) {
        println("Error chamado as ${System.currentTimeMillis()}")
    }

    private fun showEmptyState() {
        println("Empty chamado as ${System.currentTimeMillis()}")
    }

}