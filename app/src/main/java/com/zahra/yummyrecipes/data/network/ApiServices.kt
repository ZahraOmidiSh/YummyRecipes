package com.zahra.yummyrecipes.data.network

import com.zahra.yummyrecipes.models.detail.ResponseDetail
import com.zahra.yummyrecipes.models.recipe.ResponseRecipes
import com.zahra.yummyrecipes.models.register.BodyRegister
import com.zahra.yummyrecipes.models.register.ResponseRegister
import com.zahra.yummyrecipes.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiServices {
    @POST("users/connect")
    suspend fun postRegister(@Query(API_KEY) apikey: String, @Body body: BodyRegister
    ): Response<ResponseRegister>

    @GET("recipes/complexSearch")
    suspend fun getRecipe(@QueryMap queries : Map<String,String>):Response<ResponseRecipes>

    @GET ("recipes/{id}/information")
    suspend fun getDetail(@Path ("id") id:Int , @Query (API_KEY) apikey: String):Response<ResponseDetail>
}