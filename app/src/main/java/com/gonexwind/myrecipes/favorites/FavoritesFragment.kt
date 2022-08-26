package com.gonexwind.myrecipes.favorites

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gonexwind.myrecipes.MainViewModel
import com.gonexwind.myrecipes.R
import com.gonexwind.myrecipes.core.adapter.FavoriteAdapter
import com.gonexwind.myrecipes.databinding.FragmentFavoritesBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModels<MainViewModel>()
    private val adapter: FavoriteAdapter by lazy {
        FavoriteAdapter(
            requireActivity(),
            mainViewModel
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAllFavorite) {
            if (!binding.favoriteRecipesRecyclerView.isVisible) {
                showSnackBar("Favorites is empty")
            } else {
                mainViewModel.deleteAllFavoriteRecipes()
                showSnackBar("Deleted all favorites")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner) {
            adapter.setData(it)
            if (it.isEmpty()) {
                binding.noDataTextView.isVisible = true
                binding.noDataImageView.isVisible = true
                binding.favoriteRecipesRecyclerView.isVisible = false
            }
        }
        binding.favoriteRecipesRecyclerView.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter.clearContextualActionMode()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}