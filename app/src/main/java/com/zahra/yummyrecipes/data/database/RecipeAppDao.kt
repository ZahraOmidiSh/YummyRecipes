package com.zahra.yummyrecipes.data.database

import androidx.room.Dao
import androidx.room.Query
import com.zahra.yummyrecipes.data.database.entity.RecipeEntity
import com.zahra.yummyrecipes.utils.Constants.RECIPE_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeAppDao {
    //Recipes
    suspend fun saveRecipes(entity:RecipeEntity)

    @Query("SELECT * FROM $RECIPE_TABLE_NAME ORDER BY ID ASC")
    fun loadRecipes(): Flow<List<RecipeEntity>>
}