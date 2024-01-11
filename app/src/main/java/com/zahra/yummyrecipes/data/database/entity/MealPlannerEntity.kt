package com.zahra.yummyrecipes.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zahra.yummyrecipes.models.detail.ResponseDetail
import com.zahra.yummyrecipes.utils.Constants
import com.zahra.yummyrecipes.utils.Constants.MEAL_PLANNER_TABLE_NAME

@Entity(tableName = MEAL_PLANNER_TABLE_NAME)
data class MealPlannerEntity(
    @PrimaryKey(autoGenerate = false)
    var id:Int,
    var date:String,
    val result:ResponseDetail
)