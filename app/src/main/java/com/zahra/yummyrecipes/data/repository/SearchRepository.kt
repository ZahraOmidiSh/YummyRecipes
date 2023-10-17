package com.zahra.yummyrecipes.data.repository

import com.zahra.yummyrecipes.data.source.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class SearchRepository @Inject constructor(private val remote: RemoteDataSource) {
    suspend fun getSearchRecipes(queries :Map<String,String>)=remote.getRecipe(queries)
}