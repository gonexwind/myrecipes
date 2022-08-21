package com.gonexwind.myrecipes.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.gonexwind.myrecipes.MainViewModel
import com.gonexwind.myrecipes.core.adapter.RecipesAdapter
import com.gonexwind.myrecipes.core.util.NetworkResult
import com.gonexwind.myrecipes.core.util.observeOnce
import com.gonexwind.myrecipes.databinding.FragmentRecipesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private val recipesAdapter by lazy { RecipesAdapter() }
    private val viewModel: MainViewModel by viewModels()
    private val recipesViewModel: RecipesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        readDatabase()
    }

    private fun setupRecyclerView() {
        binding.recyclerview.adapter = recipesAdapter
        showShimmerEffect(true)
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            viewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    Log.d("RecipesFragment", "requestDatabase Called")
                    recipesAdapter.setData(database[0].foodRecipe)
                    showShimmerEffect(false)
                } else requestApi()
            }
        }
    }

    private fun requestApi() {
        viewModel.apply {
            getRecipes(recipesViewModel.applyQueries())
            Log.d("RecipesFragment", "requestApi Called")
            recipesResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Error -> {
                        showError(response.message.toString())
                        if (response.data == null) {
                            binding.errorTextView.isVisible = true
                            binding.errorImageView.isVisible = true
                        }
                    }
                    is NetworkResult.Loading -> showShimmerEffect(true)
                    is NetworkResult.Success -> {
                        showShimmerEffect(false)
                        response.data?.let { recipesAdapter.setData(it) }
                    }
                }
            }
        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            viewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) recipesAdapter.setData(database[0].foodRecipe)
            }
        }
    }


    private fun showShimmerEffect(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmerView.isVisible = true
            binding.shimmerView.startShimmer()
            binding.recyclerview.isVisible = false
        } else {
            binding.shimmerView.isVisible = false
            binding.recyclerview.isVisible = true
        }
    }

    private fun showError(message: String) {
        showShimmerEffect(false)
        loadDataFromCache()
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}