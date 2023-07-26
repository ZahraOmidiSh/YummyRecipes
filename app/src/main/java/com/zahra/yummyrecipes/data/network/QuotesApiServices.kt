package com.zahra.yummyrecipes.data.network

import com.zahra.yummyrecipes.models.recipe.ResponseQuotes
import com.zahra.yummyrecipes.utils.Constants.QUOTES_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface QuotesApiServices {
    @GET("/v1/quotes")
    suspend fun getQuote(@Header(QUOTES_API_KEY) apikey: String, @Query("category") category: String
    ): Response<ResponseQuotes>
}