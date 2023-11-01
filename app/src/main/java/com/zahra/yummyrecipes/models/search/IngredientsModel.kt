package com.zahra.yummyrecipes.models.search

import androidx.room.PrimaryKey
import com.zahra.yummyrecipes.R

data class IngredientsModel(
    @PrimaryKey(autoGenerate = true)
    var ingredientId :Int=-1,
    var ingredientsName:String="",
    var ingredientsImg:Int= 0,
    var isSelected:Boolean =false
)
