package com.zahra.yummyrecipes.data.source

import com.zahra.yummyrecipes.data.network.SpoonacularApiServices
import com.zahra.yummyrecipes.models.register.BodyRegister
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val api: SpoonacularApiServices) {
    suspend fun postRegister(apiKey: String, body: BodyRegister) = api.postRegister(apiKey, body)


}