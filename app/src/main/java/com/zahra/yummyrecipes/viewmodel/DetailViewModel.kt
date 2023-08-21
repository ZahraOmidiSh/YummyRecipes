package com.zahra.yummyrecipes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.yummyrecipes.data.repository.RecipeRepository
import com.zahra.yummyrecipes.models.detail.ResponseDetail
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository:RecipeRepository):ViewModel() {
    //Api
    val detailData = MutableLiveData<NetworkRequest<ResponseDetail>>()
    fun callDetailApi(id:Int , apikey:String) = viewModelScope.launch {
        detailData.value = NetworkRequest.Loading()
        val response = repository.remote.getDetail(id,apikey)
        detailData.value = NetworkResponse(response).generalNetworkResponse()

    }
}