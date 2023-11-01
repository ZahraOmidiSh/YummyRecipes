package com.zahra.yummyrecipes.models.search

import androidx.room.PrimaryKey
import com.zahra.yummyrecipes.R

data class IngredientsModel(
    @PrimaryKey(autoGenerate = false)
    val ingredientId :Int,
    var ingredientsName:String="",
    var ingredientsImg:Int= 0,
    var isSelected:Boolean =false
)
