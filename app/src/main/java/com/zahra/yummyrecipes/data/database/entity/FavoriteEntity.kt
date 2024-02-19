package com.zahra.yummyrecipes.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zahra.yummyrecipes.models.detail.ResponseDetail
import com.zahra.yummyrecipes.utils.Constants.FAVORITE_TABLE_NAME

@Entity(tableName = FAVORITE_TABLE_NAME)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val result: ResponseDetail
)
