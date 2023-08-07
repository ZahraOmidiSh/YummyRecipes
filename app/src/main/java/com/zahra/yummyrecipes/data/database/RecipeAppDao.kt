package com.zahra.yummyrecipes.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.zahra.yummyrecipes.data.database.entity.RecipeEntity
import com.zahra.yummyrecipes.utils.Constants.RECIPE_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeAppDao {
    //Recipes
    @Insert(onConflict = REPLACE)
    suspend fun saveRecipes(entity:RecipeEntity)

    @Query("SELECT * FROM $RECIPE_TABLE_NAME ORDER BY ID ASC")
    fun loadRecipes(): Flow<List<RecipeEntity>>
}