package com.zahra.yummyrecipes.models.search

import androidx.room.PrimaryKey

data class IngredientsModel(
    @PrimaryKey(autoGenerate = false)
    val ingredientId: Int,
    var ingredientsName: String = "",
    var ingredientsImg: Int = 0,
    var isSelected: Boolean = false
)
