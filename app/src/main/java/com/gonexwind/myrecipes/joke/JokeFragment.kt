package com.gonexwind.myrecipes.joke

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.gonexwind.myrecipes.core.util.NetworkResult
import com.gonexwind.myrecipes.databinding.FragmentJokeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JokeFragment : Fragment() {

    private var _binding: FragmentJokeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<JokeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {
            getFoodJoke()
            foodJokeResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Error -> showError(response.message.toString())
                    is NetworkResult.Loading -> showLoading(true)
                    is NetworkResult.Success -> {
                        showLoading(false)
                        binding.foodJokeTextView.text = response.data?.text
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
                if (it.isNotEmpty() && it != null) {
                    binding.foodJokeTextView.text = it[0].foodJoke.text
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJokeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}