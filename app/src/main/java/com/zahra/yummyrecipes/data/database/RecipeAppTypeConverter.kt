package com.zahra.yummyrecipes.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.zahra.yummyrecipes.models.detail.ResponseDetail
import com.zahra.yummyrecipes.models.recipe.ResponseRecipes

class RecipeAppTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun recipeToJson(recipe: ResponseRecipes): String {
        return gson.toJson(recipe)
    }

    @TypeConverter
    fun stringToRecipe(data: String): ResponseRecipes {
        return gson.fromJson(data, ResponseRecipes::class.java)
    }

    @TypeConverter
    fun detailToJson(detail: ResponseDetail): String {
        return gson.toJson(detail)
    }

    @TypeConverter
    fun stringToDetail(data: String): ResponseDetail {
        return gson.fromJson(data, ResponseDetail::class.java)
    }
}