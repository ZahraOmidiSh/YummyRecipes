package com.zahra.yummyrecipes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.yummyrecipes.data.repository.RecipeRepository
import com.zahra.yummyrecipes.models.recipe.ResponseQuotes
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository,
) : ViewModel() {
    //Quotes
    val quoteData = MutableLiveData<NetworkRequest<ResponseQuotes>>()
    fun callQuoteApi(apiKey: String, category: String) =viewModelScope.launch {
        quoteData.value= NetworkRequest.Loading()
        val response = repository.remote.getQuote(apiKey, category)
        quoteData.value= NetworkResponse(response).generalNetworkResponse()
    }
}