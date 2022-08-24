package com.gonexwind.myrecipes.favorites

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gonexwind.myrecipes.MainViewModel
import com.gonexwind.myrecipes.core.adapter.FavoriteAdapter
import com.gonexwind.myrecipes.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val adapter = FavoriteAdapter(requireActivity())
        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner) {
            adapter.setData(it)
            if (it.isEmpty()) {
                binding.noDataTextView.isVisible = true
                binding.noDataImageView.isVisible = true
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
    }
}