package com.zahra.yummyrecipes.di

import android.content.Context
import androidx.room.Room
import com.zahra.yummyrecipes.data.database.RecipeAppDatabase
import com.zahra.yummyrecipes.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, RecipeAppDatabase::class.java, DATABASE_NAME
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(database: RecipeAppDatabase) = database.dao()
}