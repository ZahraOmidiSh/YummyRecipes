package com.zahra.yummyrecipes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.models.search.IngredientsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel@Inject constructor(): ViewModel() {
    //Ingredients
//    val ingredientsList = MutableLiveData<MutableList<IngredientsModel>>()

//    fun loadIngredientsList() = viewModelScope.launch(Dispatchers.IO) {
//        val hotDog = IngredientsModel(0,"hot_dog", R.drawable.hot_dog)
//        val cupcake = IngredientsModel(0,"cupcake", R.drawable.cupcake)
//        val doughnut = IngredientsModel(0,"doughnut", R.drawable.doughnut)
//
//        val data = mutableListOf(hotDog, cupcake, doughnut)
//        ingredientsList.postValue(data)
//    }
}