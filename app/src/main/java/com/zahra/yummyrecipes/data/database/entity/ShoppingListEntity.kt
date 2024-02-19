package com.zahra.yummyrecipes.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zahra.yummyrecipes.utils.Constants.SHOPPING_LIST_TABLE_NAME

@Entity(tableName = SHOPPING_LIST_TABLE_NAME)
data class ShoppingListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val amount: Double,
    val unit: String,
    val image: String = ""
)
