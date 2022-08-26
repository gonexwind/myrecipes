package com.gonexwind.myrecipes.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gonexwind.myrecipes.core.data.local.entity.FavoriteEntity
import com.gonexwind.myrecipes.core.data.local.entity.FoodJokeEntity
import com.gonexwind.myrecipes.core.data.local.entity.RecipesEntity
import com.gonexwind.myrecipes.core.util.RecipesTypeConverter

@Database(
    entities = [
        RecipesEntity::class,
        FavoriteEntity::class,
        FoodJokeEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

}