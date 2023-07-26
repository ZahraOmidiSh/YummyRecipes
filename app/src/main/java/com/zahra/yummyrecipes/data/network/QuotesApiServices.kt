package com.zahra.yummyrecipes.data.network

import com.zahra.yummyrecipes.models.recipe.QuotesResponse
import com.zahra.yummyrecipes.models.register.BodyRegister
import com.zahra.yummyrecipes.models.register.ResponseRegister
import com.zahra.yummyrecipes.utils.Constants.API_KEY
import com.zahra.yummyrecipes.utils.Constants.QUOTES_API_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface QuotesApiServices {
    @GET("/v1/quotes")
    suspend fun getQuote(@Header(QUOTES_API_KEY) apikey: String, @Query("category") category: String
    ): Response<QuotesResponse>
}