package com.zahra.yummyrecipes.models.register


import com.google.gson.annotations.SerializedName

data class BodyRegister(
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("firstName")
    var firstName: String? = null,
    @SerializedName("lastName")
    var lastName: String? = null,
    @SerializedName("username")
    var username: String? = null
)