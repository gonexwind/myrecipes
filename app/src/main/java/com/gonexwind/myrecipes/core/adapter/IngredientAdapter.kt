package com.gonexwind.myrecipes.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.gonexwind.myrecipes.R
import com.gonexwind.myrecipes.core.model.ExtendedIngredient
import com.gonexwind.myrecipes.core.util.Constants.BASE_IMAGE_URL
import com.gonexwind.myrecipes.core.util.RecipesDiffUtil
import com.gonexwind.myrecipes.databinding.ItemIngredientBinding

class IngredientAdapter : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    private var ingredients = emptyList<ExtendedIngredient>()

    inner class ViewHolder(val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIngredientBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.binding.apply {
            ingredientImageView.load(BASE_IMAGE_URL + ingredient.image) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
            ingredientName.text = ingredient.name
            ingredientAmount.text = ingredient.amount.toString()
            ingredientUnit.text = ingredient.unit
            ingredientConsistency.text = ingredient.consistency
            ingredientOriginal.text = ingredient.original
        }
    }

    override fun getItemCount(): Int = ingredients.size

    fun setData(ingredients: List<ExtendedIngredient>) {
        val diffUtil = RecipesDiffUtil(this.ingredients, ingredients)
        val result = DiffUtil.calculateDiff(diffUtil)
        this.ingredients = ingredients
        result.dispatchUpdatesTo(this)
    }
}