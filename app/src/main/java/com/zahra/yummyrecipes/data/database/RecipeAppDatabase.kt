package com.zahra.yummyrecipes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zahra.yummyrecipes.data.database.entity.DetailEntity
import com.zahra.yummyrecipes.data.database.entity.FavoriteEntity
import com.zahra.yummyrecipes.data.database.entity.MealPlannerEntity
import com.zahra.yummyrecipes.data.database.entity.RecipeEntity
import com.zahra.yummyrecipes.data.database.entity.ShoppingListEntity

@Database(
    entities = [RecipeEntity::class, DetailEntity::class, FavoriteEntity::class, MealPlannerEntity::class, ShoppingListEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipeAppTypeConverter::class)
abstract class RecipeAppDatabase : RoomDatabase() {
    abstract fun dao(): RecipeAppDao
}