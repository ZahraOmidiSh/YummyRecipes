package com.zahra.yummyrecipes.data.source

import com.zahra.yummyrecipes.data.database.RecipeAppDao
import com.zahra.yummyrecipes.data.database.entity.RecipeEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao:RecipeAppDao) {
//Recipes
    suspend fun saveRecipes(entity: RecipeEntity) = dao.saveRecipes(entity)
    fun loadRecipes() = dao.loadRecipes()

}