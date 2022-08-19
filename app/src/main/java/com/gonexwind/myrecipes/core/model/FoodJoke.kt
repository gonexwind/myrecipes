package com.gonexwind.myrecipes.core.model

import com.google.gson.annotations.SerializedName

data class FoodJoke(

    @SerializedName("text")
    val text: String
)