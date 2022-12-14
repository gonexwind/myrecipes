package com.gonexwind.myrecipes.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gonexwind.myrecipes.core.model.FoodRecipe
import com.gonexwind.myrecipes.core.util.Constants.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {

    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}