package com.zahra.yummyrecipes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zahra.yummyrecipes.data.database.entity.DetailEntity
import com.zahra.yummyrecipes.data.database.entity.FavoriteEntity
import com.zahra.yummyrecipes.data.database.entity.RecipeEntity

@Database(entities = [RecipeEntity::class , DetailEntity::class,FavoriteEntity::class], version = 3 , exportSchema = false)
@TypeConverters(RecipeAppTypeConverter::class)
abstract class RecipeAppDatabase:RoomDatabase() {
    abstract fun dao():RecipeAppDao
}