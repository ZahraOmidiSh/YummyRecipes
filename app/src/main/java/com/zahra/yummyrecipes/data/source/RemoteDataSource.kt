package com.zahra.yummyrecipes.data.source

import com.zahra.yummyrecipes.data.network.ApiServices
import com.zahra.yummyrecipes.models.register.BodyRegister
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: ApiServices,
) {
    suspend fun postRegister(apiKey: String, body: BodyRegister) = api.postRegister(apiKey, body)

    suspend fun getRecipe(queries: Map<String, String>) = api.getRecipe(queries)


    suspend fun getDetail(id: Int, apiKey: String, includeNutrition: Boolean) =
        api.getDetail(id, apiKey, includeNutrition)

    suspend fun getSimilarRecipes(id: Int, apiKey: String) = api.getSimilarRecipes(id, apiKey)

}