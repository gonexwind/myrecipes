package com.gonexwind.myrecipes.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gonexwind.myrecipes.core.util.RecipesTypeConverter

@Database(
    entities = [
        RecipesEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

}