package com.zahra.yummyrecipes.data.repository

import com.zahra.yummyrecipes.data.source.LocalDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FavoriteRepository @Inject constructor(private val localDataSource: LocalDataSource) {
    val local = localDataSource
}