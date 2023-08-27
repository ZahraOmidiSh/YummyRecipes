package com.zahra.yummyrecipes.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zahra.yummyrecipes.models.detail.ResponseDetail
import com.zahra.yummyrecipes.utils.Constants.DETAIL_TABLE_NAME

@Entity(tableName =DETAIL_TABLE_NAME)
data class DetailEntity(
    @PrimaryKey(autoGenerate = false)
    val id:Int,
    val result : ResponseDetail
)

