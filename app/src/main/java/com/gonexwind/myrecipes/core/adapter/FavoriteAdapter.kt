package com.gonexwind.myrecipes.core.adapter

import android.text.Html
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.gonexwind.myrecipes.MainViewModel
import com.gonexwind.myrecipes.R
import com.gonexwind.myrecipes.core.data.local.entity.FavoriteEntity
import com.gonexwind.myrecipes.core.util.RecipesDiffUtil
import com.gonexwind.myrecipes.databinding.ItemRecipesBinding
import com.gonexwind.myrecipes.favorites.FavoritesFragmentDirections
import com.google.android.material.snackbar.Snackbar

class FavoriteAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel,
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>(), ActionMode.Callback {

    private var recipes = emptyList<FavoriteEntity>()
    private var selectedRecipes = arrayListOf<FavoriteEntity>()
    private var viewHolder = arrayListOf<ViewHolder>()
    private var multiSelection = false
    private lateinit var actionMode: ActionMode
    private lateinit var rootView: View

    inner class ViewHolder(val binding: ItemRecipesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        rootView = holder.itemView.rootView
        viewHolder.add(holder)
        val recipe = recipes[position].result
        val currentRecipes = recipes[position]

        holder.binding.apply {
            recipeImageView.load(recipe.image) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
            titleTextView.text = recipe.title
            descriptionTextView.text = Html.fromHtml(recipe.summary)
            heartTextView.text = recipe.aggregateLikes.toString()
            clockTextView.text = recipe.readyInMinutes.toString()
            if (recipe.vegan) {
                leafTextView.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.green)
                )
                leafImageView.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, R.color.green)
                )
            }
        }
        holder.itemView.apply {
            /*
                Single Click Listener
             */
            setOnClickListener {
                if (multiSelection) applySelection(holder, currentRecipes)
                else findNavController().navigate(
                    FavoritesFragmentDirections.actionFavoritesFragmentToDetailFragment(recipe)
                )
            }

            /*
                Long Click Listener
             */
            setOnLongClickListener {
                if (!multiSelection) {
                    multiSelection = true
                    requireActivity.startActionMode(this@FavoriteAdapter)
                    applySelection(holder, currentRecipes)
                    true
                } else {
                    applySelection(holder, currentRecipes)
                    multiSelection = false
                    false
                }
            }
        }
    }

    private fun applySelection(holder: ViewHolder, favorite: FavoriteEntity) {
        if (selectedRecipes.contains(favorite)) {
            selectedRecipes.remove(favorite)
            changeRecipeStyle(holder, R.color.card_background)
            applyActionModeTitle()
        } else {
            selectedRecipes.add(favorite)
            changeRecipeStyle(holder, R.color.card_selected)
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(holder: ViewHolder, backgroundColor: Int) {
        holder.itemView.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
    }

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> actionMode.finish()
            1 -> actionMode.title = "${selectedRecipes.size} item selected"
            else -> actionMode.title = "${selectedRecipes.size} items selected"
        }
    }

    override fun getItemCount(): Int = recipes.size

    fun setData(newData: List<FavoriteEntity>) {
        val recipesDiffUtil = RecipesDiffUtil(recipes, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

    override fun onCreateActionMode(actionMode: ActionMode, menu: Menu?): Boolean {
        actionMode.menuInflater.inflate(R.menu.favorite_contextual_action_menu, menu)
        this.actionMode = actionMode
        return true
    }

    override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(action: ActionMode, menu: MenuItem): Boolean {
        if (menu.itemId == R.id.deleteRecipesMenu) {
            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipes(it)
            }
            showSnackBar("${selectedRecipes.size} recipe/s removed.")
            multiSelection = false
            selectedRecipes.clear()
            action.finish()
        }
        return true
    }

    override fun onDestroyActionMode(p0: ActionMode?) {
        viewHolder.forEach {
            changeRecipeStyle(it, R.color.card_background)
        }
        multiSelection = false
        selectedRecipes.clear()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
    }

    fun clearContextualActionMode() {
        if (this::actionMode.isInitialized) {
            actionMode.finish()
        }
    }
}

