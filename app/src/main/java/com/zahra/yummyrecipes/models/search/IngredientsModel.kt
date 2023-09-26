package com.zahra.yummyrecipes.models.search

import androidx.room.PrimaryKey
import com.zahra.yummyrecipes.R

data class IngredientsModel(
    @PrimaryKey(autoGenerate = true)
    var ingredientsId:Int=0,
    var ingredientsName:String="",
    var ingredientsImg:Int= 0
)
