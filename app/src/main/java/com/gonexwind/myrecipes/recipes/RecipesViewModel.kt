package com.gonexwind.myrecipes.recipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gonexwind.myrecipes.BuildConfig
import com.gonexwind.myrecipes.core.util.Constants.DEFAULT_DIET_TYPE
import com.gonexwind.myrecipes.core.util.Constants.DEFAULT_MEAL_TYPE
import com.gonexwind.myrecipes.core.util.Constants.DEFAULT_RECIPES_NUMBER
import com.gonexwind.myrecipes.core.util.Constants.QUERY_ADD_RECIPE_INFORMATION
import com.gonexwind.myrecipes.core.util.Constants.QUERY_API_KEY
import com.gonexwind.myrecipes.core.util.Constants.QUERY_DIET
import com.gonexwind.myrecipes.core.util.Constants.QUERY_FILL_INGREDIENTS
import com.gonexwind.myrecipes.core.util.Constants.QUERY_NUMBER
import com.gonexwind.myrecipes.core.util.Constants.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = BuildConfig.API_KEY
        queries[QUERY_TYPE] = DEFAULT_MEAL_TYPE
        queries[QUERY_DIET] = DEFAULT_DIET_TYPE
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }
}