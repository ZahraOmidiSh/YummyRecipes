package com.zahra.yummyrecipes.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zahra.yummyrecipes.models.recipe.ResponseRecipes
import com.zahra.yummyrecipes.utils.Constants

@Entity(tableName = Constants.MEAL_PLANNER_TABLE_NAME)
data class MealPlannerEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    var response: ResponseRecipes
)