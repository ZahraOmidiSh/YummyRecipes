package com.zahra.yummyrecipes.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zahra.yummyrecipes.data.network.ApiServices
import com.zahra.yummyrecipes.utils.Constants.BASE_URL
import com.zahra.yummyrecipes.utils.Constants.NETWORK_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkProvider {

    @Provides
    @Singleton
    fun provideBaseUrl()= BASE_URL

    @Provides
    @Singleton
    fun provideNetworkTime()= NETWORK_TIMEOUT

    @Provides
    @Singleton
    fun provideGson():Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideBodyInterceptor()=HttpLoggingInterceptor().apply {
        level=HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideClient(time:Long , body:HttpLoggingInterceptor)=OkHttpClient.Builder()
        .addInterceptor(body)
        .connectTimeout(time,TimeUnit.SECONDS)
        .readTimeout(time,TimeUnit.SECONDS)
        .writeTimeout(time,TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl:String,client:OkHttpClient , gson :Gson): ApiServices =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServices::class.java)

}