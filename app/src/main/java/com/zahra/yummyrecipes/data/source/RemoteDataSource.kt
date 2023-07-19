package com.zahra.yummyrecipes.data.source

import com.zahra.yummyrecipes.data.network.ApiServices
import com.zahra.yummyrecipes.models.register.BodyRegister
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val api: ApiServices) {
    suspend fun postRegister(apiKey: String, body: BodyRegister) = api.postRegister(apiKey, body)


}