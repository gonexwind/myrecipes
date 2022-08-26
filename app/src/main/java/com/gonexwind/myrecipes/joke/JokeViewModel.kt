package com.gonexwind.myrecipes.joke

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.gonexwind.myrecipes.core.data.Repository
import com.gonexwind.myrecipes.core.data.local.entity.FoodJokeEntity
import com.gonexwind.myrecipes.core.model.FoodJoke
import com.gonexwind.myrecipes.core.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class JokeViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /*
        ROOM
     */
    val readFoodJoke: LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()

    private fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFoodJoke(foodJokeEntity)
        }

    private fun offlineCacheRecipes(foodJoke: FoodJoke) {
        val jokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(jokeEntity)
    }

    /*
     REMOTE
     */
    var foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    fun getFoodJoke() = viewModelScope.launch {
        foodJokeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getFoodJoke()
                foodJokeResponse.value = handleJokeFoodResponse(response)

                val joke = foodJokeResponse.value!!.data
                if (joke != null) {
                    offlineCacheRecipes(joke)
                }
            } catch (e: Exception) {
                foodJokeResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else foodJokeResponse.value = NetworkResult.Error("No Internet Connection")
    }

    private fun handleJokeFoodResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? =
        when {
            response.message().toString().contains("timeout") -> NetworkResult.Error("Timeout")
            response.code() == 402 -> NetworkResult.Error("API Key Limited")
            response.body()!!.text.isEmpty() -> NetworkResult.Error("Recipes not found")
            response.isSuccessful -> {
                val foodRecipes = response.body()
                NetworkResult.Success(foodRecipes!!)
            }
            else -> NetworkResult.Error(response.message())
        }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}