package com.zahra.yummyrecipes.utils

import retrofit2.Response

class NetworkResponse<T>(private val response: Response<T>) {
    fun generalNetworkResponse():NetworkRequest<T> {
        return when{
            response.code() == 401 -> NetworkRequest.Error("You are not authorized")
            response.code() == 402 -> NetworkRequest.Error("Your free plan is finished")
            response.code() == 422 -> NetworkRequest.Error("Api key not found!")
            response.code() == 500 -> NetworkRequest.Error("Try again!")
            response.isSuccessful ->NetworkRequest.Success(response.body()!!)
            else -> NetworkRequest.Error(response.message())
        }
    }
}