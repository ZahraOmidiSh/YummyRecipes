package com.zahra.yummyrecipes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zahra.yummyrecipes.data.database.entity.DetailEntity
import com.zahra.yummyrecipes.data.database.entity.FavoriteEntity
import com.zahra.yummyrecipes.data.database.entity.ShoppingListEntity
import com.zahra.yummyrecipes.data.repository.RecipeRepository
import com.zahra.yummyrecipes.models.detail.ResponseDetail
import com.zahra.yummyrecipes.models.detail.ResponseSimilar
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: RecipeRepository) : ViewModel() {
    //Api
    val detailData = MutableLiveData<NetworkRequest<ResponseDetail>>()
    fun callDetailApi(id: Int, apikey: String) = viewModelScope.launch {
        detailData.value = NetworkRequest.Loading()
        val response = repository.remote.getDetail(id, apikey, true)
        detailData.value = NetworkResponse(response).generalNetworkResponse()
        //Cache
        val cache = detailData.value?.data
        if (cache != null) {
            cacheDetail(cache.id!!, cache)
        }
    }

    //Local
    private fun saveDetail(entity: DetailEntity) = viewModelScope.launch {
        repository.local.saveDetail(entity)
    }

    fun readDetailFromDb(id: Int) = repository.local.loadDetail(id).asLiveData()

    val existsDetailData = MutableLiveData<Boolean>()
    fun existsDetail(id: Int) = viewModelScope.launch {
        repository.local.existsDetail(id).collect {
            existsDetailData.postValue(it)
        }
    }

    private fun cacheDetail(id: Int, response: ResponseDetail) {
        val entity = DetailEntity(id, response)
        saveDetail(entity)
    }

    //Similar
    val similarData = MutableLiveData<NetworkRequest<ResponseSimilar>>()
    fun callSimilarApi(id: Int, apikey: String) = viewModelScope.launch {
        similarData.value = NetworkRequest.Loading()
        val response = repository.remote.getSimilarRecipes(id, apikey)
        similarData.value = NetworkResponse(response).generalNetworkResponse()
    }

    //Favorite
    fun saveFavorite(entity: FavoriteEntity) = viewModelScope.launch {
        repository.local.saveFavorite(entity)
    }

    fun deleteFavorite(entity: FavoriteEntity) = viewModelScope.launch {
        repository.local.deleteFavorite(entity)
    }

    val existsFavoriteData = MutableLiveData<Boolean>()
    fun existsFavorite(id: Int) = viewModelScope.launch {
        repository.local.existsFavorite(id).collect {
            existsFavoriteData.postValue(it)
        }
    }

    //Shopping List
    fun saveIngredientToShoppingList(entity: ShoppingListEntity) = viewModelScope.launch {
        repository.local.saveShoppingList(entity)
    }


}