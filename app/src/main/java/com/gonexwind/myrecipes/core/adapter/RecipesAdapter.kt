package com.gonexwind.myrecipes.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.gonexwind.myrecipes.R
import com.gonexwind.myrecipes.core.model.FoodRecipe
import com.gonexwind.myrecipes.core.model.Result
import com.gonexwind.myrecipes.core.util.RecipesDiffUtil
import com.gonexwind.myrecipes.databinding.RecipesRowLayoutBinding

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {

    private var recipes = emptyList<Result>()

    inner class ViewHolder(val binding: RecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) {
            binding.apply {
                recipeImageView.load(result.image) {
                    crossfade(600)
                    error(R.drawable.ic_error_placeholder)
                }
                titleTextView.text = result.title
                descriptionTextView.text = result.summary
                heartTextView.text = result.aggregateLikes.toString()
                clockTextView.text = result.readyInMinutes.toString()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecipesRowLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)

        if (recipe.vegan) {
            holder.binding.apply {
                leafTextView.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.green
                    )
                )
                leafImageView.setColorFilter(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.green
                    )
                )
            }
        }

    }

    override fun getItemCount(): Int = recipes.size

    fun setData(newData: FoodRecipe) {
        val recipesDiffUtil = RecipesDiffUtil(recipes, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}