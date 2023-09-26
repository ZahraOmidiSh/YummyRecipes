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
    val tenIngredientsList = MutableLiveData<MutableList<IngredientsModel>>()

    fun loadTenIngredientsList() = viewModelScope.launch(Dispatchers.IO) {
        val carrot = IngredientsModel(0,"Carrot", R.drawable.carrot)
        val chicken = IngredientsModel(0,"Chicken", R.drawable.chicken)
        val egg = IngredientsModel(0,"Egg", R.drawable.egg)
        val pasta = IngredientsModel(0,"pasta", R.drawable.pasta)
        val apple = IngredientsModel(0,"apple", R.drawable.apple)
        val banana = IngredientsModel(0,"banana", R.drawable.banana)
        val cheese = IngredientsModel(0,"cheese", R.drawable.cheese)
        val rice = IngredientsModel(0,"rice", R.drawable.rice)
        val milk = IngredientsModel(0,"milk", R.drawable.milk)
        val fish = IngredientsModel(0,"fish", R.drawable.fish)

        val data = mutableListOf(carrot, chicken, egg,pasta,apple,banana,cheese,rice,milk,fish)
        tenIngredientsList.postValue(data)
    }
}