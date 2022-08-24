package com.gonexwind.myrecipes.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.gonexwind.myrecipes.R
import com.gonexwind.myrecipes.core.data.local.entity.FavoriteEntity
import com.gonexwind.myrecipes.databinding.ItemRecipesBinding
import com.gonexwind.myrecipes.core.util.RecipesDiffUtil

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private var recipes = emptyList<FavoriteEntity>()

    inner class ViewHolder(val binding: ItemRecipesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position].result
        holder.binding.apply {
            recipeImageView.load(recipe.image) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
            titleTextView.text = recipe.title
            descriptionTextView.text = recipe.summary
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
    }

    override fun getItemCount(): Int = recipes.size

    fun setData(newData: List<FavoriteEntity>) {
        val recipesDiffUtil = RecipesDiffUtil(recipes, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}