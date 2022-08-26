package com.gonexwind.myrecipes.joke

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.gonexwind.myrecipes.R
import com.gonexwind.myrecipes.core.util.NetworkResult
import com.gonexwind.myrecipes.databinding.FragmentJokeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JokeFragment : Fragment() {

    private var _binding: FragmentJokeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<JokeViewModel>()
    private var foodJoke = "No Food Joke"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)
        loadJoke()
    }

    private fun loadJoke() {
        viewModel.apply {
            getFoodJoke()
            foodJokeResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Error -> showError(response.message.toString())
                    is NetworkResult.Loading -> showLoading(true)
                    is NetworkResult.Success -> {
                        showLoading(false)
                        response.data?.let {
                            binding.foodJokeTextView.text = it.text
                            foodJoke = it.text
                        }
                    }
                }
            }
        }
    }

    private fun showError(message: String) {
        loadCache()
        showLoading(false)
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.isVisible = state
        binding.foodJokeCardView.isVisible = !state
    }

    private fun loadCache() {
        lifecycleScope.launch {
            viewModel.readFoodJoke.observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {
                    binding.foodJokeTextView.text = it[0].foodJoke.text
                    foodJoke = it.first().foodJoke.text
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.joke_menu, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.shareMenu) {
            val share = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, foodJoke)
                type = "text/plain"
            }
            startActivity(share)
        }
        @Suppress("DEPRECATION")
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJokeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}