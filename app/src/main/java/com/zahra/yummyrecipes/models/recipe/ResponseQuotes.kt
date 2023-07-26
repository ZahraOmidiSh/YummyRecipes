package com.zahra.yummyrecipes.models.recipe


import com.google.gson.annotations.SerializedName

class ResponseQuotes : ArrayList<ResponseQuotes.QuotesResponseItem>(){
    data class QuotesResponseItem(
        @SerializedName("author")
        val author: String?, // Jonathan Sacks
        @SerializedName("category")
        val category: String?, // food
        @SerializedName("quote")
        val quote: String? // Food prices are often kept artificially high. The result is that the Millennium Development Goals set out by the United Nations at the start of the new millennium are not being reached. Fine words have not yet been turned into deeds.
    )
}