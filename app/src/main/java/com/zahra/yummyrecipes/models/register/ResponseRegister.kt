package com.zahra.yummyrecipes.models.register


import com.google.gson.annotations.SerializedName

data class ResponseRegister(
    @SerializedName("hash")
    val hash: String?,
    @SerializedName("spoonacularPassword")
    val spoonacularPassword: String?,
    @SerializedName("username")
    val username: String?
)