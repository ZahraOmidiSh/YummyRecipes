package com.zahra.yummyrecipes.data.source

import com.zahra.yummyrecipes.data.network.QuotesApiServices
import com.zahra.yummyrecipes.data.network.SpoonacularApiServices
import com.zahra.yummyrecipes.models.register.BodyRegister
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val spoonacularApi: SpoonacularApiServices,
    private val quotesApi: QuotesApiServices
) {
    suspend fun postRegister(apiKey: String, body: BodyRegister) = spoonacularApi.postRegister(apiKey, body)
    suspend fun getQuote(apiKey: String, category: String) = quotesApi.getQuote(apiKey, category)


}