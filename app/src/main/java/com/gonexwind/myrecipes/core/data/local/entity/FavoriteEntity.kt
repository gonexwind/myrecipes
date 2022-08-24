package com.gonexwind.myrecipes.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gonexwind.myrecipes.core.util.Constants.FAVORITE_RECIPES_TABLE
import com.gonexwind.myrecipes.core.model.Result

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoriteEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val result: Result
)
