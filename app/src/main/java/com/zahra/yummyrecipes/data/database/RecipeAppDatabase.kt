package com.zahra.yummyrecipes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zahra.yummyrecipes.data.database.entity.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1 , exportSchema = false)
@TypeConverters(RecipeAppTypeConverter::class)
abstract class RecipeAppDatabase:RoomDatabase() {
    abstract fun dao():RecipeAppDao
}