package com.gonexwind.myrecipes.core.data.remote

import com.gonexwind.myrecipes.core.model.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getRecipes(queries: Map<String, String>) : Response<FoodRecipe> =
        apiService.getRecipes(queries)

    suspend fun searchRecipes(queries: Map<String, String>) : Response<FoodRecipe> =
        apiService.searchRecipes(queries)
}