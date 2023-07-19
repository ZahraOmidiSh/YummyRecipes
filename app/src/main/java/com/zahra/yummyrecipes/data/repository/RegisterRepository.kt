package com.zahra.yummyrecipes.data.repository

import com.zahra.yummyrecipes.data.source.RemoteDataSource
import com.zahra.yummyrecipes.models.register.BodyRegister
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RegisterRepository @Inject constructor(private val remote: RemoteDataSource){
     // API
    suspend fun postRegister(apiKey: String, body: BodyRegister) = remote.postRegister(apiKey, body)
}