package com.zahra.yummyrecipes.data.repository

import com.zahra.yummyrecipes.data.source.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RecipeRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    val remote = remoteDataSource
}