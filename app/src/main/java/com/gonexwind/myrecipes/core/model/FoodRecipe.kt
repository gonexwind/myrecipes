package com.gonexwind.myrecipes.core.model

import com.google.gson.annotations.SerializedName

data class FoodRecipe(

    @SerializedName("results")
    val results: List<Result>
)