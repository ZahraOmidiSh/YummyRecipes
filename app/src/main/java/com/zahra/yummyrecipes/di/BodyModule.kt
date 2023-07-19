package com.zahra.yummyrecipes.di

import com.zahra.yummyrecipes.models.register.BodyRegister
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BodyModule {

    @Provides
    fun bodyRegister() = BodyRegister()
}