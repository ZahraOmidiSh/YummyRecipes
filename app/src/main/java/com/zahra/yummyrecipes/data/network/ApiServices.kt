package com.zahra.yummyrecipes.data.network

import com.zahra.yummyrecipes.models.register.BodyRegister
import com.zahra.yummyrecipes.models.register.ResponseRegister
import com.zahra.yummyrecipes.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServices {
    @POST("users/connect")
    suspend fun postRegister(
        @Query(API_KEY) apikey: String,
        @Body body: BodyRegister
    ): Response<ResponseRegister>
}