package com.zahra.yummyrecipes.models.detail


import com.google.gson.annotations.SerializedName

class ResponseSimilar : ArrayList<ResponseSimilar.ResponseSimilarItem>() {
    data class ResponseSimilarItem(
        @SerializedName("id")
        val id: Int?, // 9751
        @SerializedName("imageType")
        val imageType: String?, // jpg
        @SerializedName("readyInMinutes")
        val readyInMinutes: Int?, // 45
        @SerializedName("servings")
        val servings: Int?, // 4
        @SerializedName("sourceUrl")
        val sourceUrl: String?, // http://www.seriouseats.com/recipes/2007/08/dinner-tonight-roasted-beet-bruschetta.html
        @SerializedName("title")
        val title: String? // Dinner Tonight: Roasted Beet Bruschetta
    )
}