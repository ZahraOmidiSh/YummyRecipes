package com.zahra.yummyrecipes.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zahra.yummyrecipes.models.recipe.ResponseRecipes
import com.zahra.yummyrecipes.utils.Constants.RECIPE_TABLE_NAME

@Entity(tableName = RECIPE_TABLE_NAME)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String ,
    var response: ResponseRecipes
)