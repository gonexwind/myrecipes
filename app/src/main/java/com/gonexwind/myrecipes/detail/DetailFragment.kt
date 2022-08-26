package com.gonexwind.myrecipes.detail

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.gonexwind.myrecipes.MainViewModel
import com.gonexwind.myrecipes.R
import com.gonexwind.myrecipes.core.adapter.PagerAdapter
import com.gonexwind.myrecipes.core.data.local.entity.FavoriteEntity
import com.gonexwind.myrecipes.core.util.Constants.RECIPE_RESULT_KEY
import com.gonexwind.myrecipes.databinding.FragmentDetailBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: MainViewModel by viewModels()
    private var recipeSaved = false
    private var savedRecipeId = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPageAdapter()
    }

    private fun setupPageAdapter() {
        val titles = arrayListOf("Overview", "Ingredients", "Instructions")
        val fragments = arrayListOf(OverviewFragment(), IngredientFragment(), InstructionFragment())
        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)
        val pagerAdapter =
            PagerAdapter(resultBundle, fragments, requireActivity())

        binding.apply {
            viewPager.adapter = pagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = titles[position]
            }.attach()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        val menuItem = menu.findItem(R.id.favoriteMenu)
        checkSavedRecipes(menuItem)

    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.favoriteMenu && !recipeSaved) saveToFavorites(item)
        else if (item.itemId == R.id.favoriteMenu && recipeSaved) removeFromFavorite(item)
        return super.onOptionsItemSelected(item)
    }


    private fun checkSavedRecipes(menuItem: MenuItem) {
        viewModel.readFavoriteRecipes.observe(requireActivity()) { favorites ->
            try {
                favorites.forEach {
                    if (it.result.recipeId == args.result.recipeId) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedRecipeId = it.id
                        recipeSaved = true
                    } else changeMenuItemColor(menuItem, R.color.icon)
                }
            } catch (e: Exception) {
                Log.d("DetailFragment", e.message.toString())
            }
        }
    }

    private fun saveToFavorites(item: MenuItem) {
        val favorite = FavoriteEntity(0, args.result)
        viewModel.insertFavoriteRecipes(favorite)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe Saved.")
        recipeSaved = true
    }

    private fun removeFromFavorite(item: MenuItem) {
        val favorite = FavoriteEntity(savedRecipeId, args.result)
        viewModel.deleteFavoriteRecipes(favorite)
        changeMenuItemColor(item, R.color.icon)
        showSnackBar("Removed from Favorites.")
        recipeSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun changeMenuItemColor(menuItem: MenuItem, color: Int) {
        menuItem.icon.setTint(ContextCompat.getColor(requireContext(), color))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}