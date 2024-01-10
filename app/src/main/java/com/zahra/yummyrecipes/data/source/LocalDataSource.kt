package com.zahra.yummyrecipes.data.source

import com.zahra.yummyrecipes.data.database.RecipeAppDao
import com.zahra.yummyrecipes.data.database.entity.DetailEntity
import com.zahra.yummyrecipes.data.database.entity.FavoriteEntity
import com.zahra.yummyrecipes.data.database.entity.MealPlannerEntity
import com.zahra.yummyrecipes.data.database.entity.RecipeEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: RecipeAppDao) {
    //Recipes
    suspend fun saveRecipes(entity: RecipeEntity) = dao.saveRecipes(entity)
    fun loadRecipes() = dao.loadRecipes()

    //Detail
    suspend fun saveDetail(entity: DetailEntity) = dao.saveDetail(entity)
    fun loadDetail(id: Int) = dao.loadDetail(id)
    fun existsDetail(id: Int) = dao.existsDetail(id)

    //Favorite
    suspend fun saveFavorite(entity: FavoriteEntity) = dao.saveFavorite(entity)
    suspend fun deleteFavorite(entity: FavoriteEntity) = dao.deleteFavorite(entity)
    fun loadFavorite() = dao.loadFavorite()
    fun existsFavorite(id: Int) = dao.existsFavorite(id)

    //Meal
    suspend fun savePlannedMeal(entity: MealPlannerEntity) = dao.saveMeal(entity)
    suspend fun deletePlannedMeal(entity: MealPlannerEntity) = dao.deleteMeal(entity)
    fun loadPlannedMeals(date:Long) = dao.loadMeal(date)

}