package com.gonexwind.myrecipes.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.gonexwind.myrecipes.core.data.DataStoreRepository.PreferenceKeys.selectedDietType
import com.gonexwind.myrecipes.core.data.DataStoreRepository.PreferenceKeys.selectedDietTypeId
import com.gonexwind.myrecipes.core.data.DataStoreRepository.PreferenceKeys.selectedMealType
import com.gonexwind.myrecipes.core.data.DataStoreRepository.PreferenceKeys.selectedMealTypeId
import com.gonexwind.myrecipes.core.util.Constants.DEFAULT_DIET_TYPE
import com.gonexwind.myrecipes.core.util.Constants.DEFAULT_MEAL_TYPE
import com.gonexwind.myrecipes.core.util.Constants.PREFERENCES_DIET_TYPE
import com.gonexwind.myrecipes.core.util.Constants.PREFERENCES_DIET_TYPE_ID
import com.gonexwind.myrecipes.core.util.Constants.PREFERENCES_MEAL_TYPE
import com.gonexwind.myrecipes.core.util.Constants.PREFERENCES_MEAL_TYPE_ID
import com.gonexwind.myrecipes.core.util.Constants.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val dataStore: DataStore<Preferences> = context.dataStore

    private object PreferenceKeys {
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
    }

    suspend fun saveMealAndDietType(
        mealType: String, mealTypeId: Int,
        dietType: String, dietTypeId: Int
    ) {
        dataStore.edit {
            it[selectedMealType] = mealType
            it[selectedMealTypeId] = mealTypeId
            it[selectedDietType] = dietType
            it[selectedDietTypeId] = dietTypeId
        }
    }

    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map {
            MealAndDietType(
                it[selectedMealType] ?: DEFAULT_MEAL_TYPE,
                it[selectedMealTypeId] ?: 0,
                it[selectedDietType] ?: DEFAULT_DIET_TYPE,
                it[selectedDietTypeId] ?: 0
            )
        }
}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)