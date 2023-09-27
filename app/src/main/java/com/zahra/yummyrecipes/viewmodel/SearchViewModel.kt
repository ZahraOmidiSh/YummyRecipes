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
    val limitIngredientsList = MutableLiveData<MutableList<IngredientsModel>>()

    fun loadLimitIngredientsList() = viewModelScope.launch(Dispatchers.IO) {
        val carrot = IngredientsModel(0,"Carrot", R.drawable.s_carrot)
        val chicken = IngredientsModel(0,"Chicken", R.drawable.s_chicken)
        val egg = IngredientsModel(0,"Egg", R.drawable.s_egg)
        val pasta = IngredientsModel(0,"Pasta", R.drawable.s_pasta)
        val apple = IngredientsModel(0,"Apple", R.drawable.s_apple)
        val banana = IngredientsModel(0,"Banana", R.drawable.s_banana)
        val cheese = IngredientsModel(0,"Cheese", R.drawable.s_cheese)
        val rice = IngredientsModel(0,"Rice", R.drawable.s_rice)
        val milk = IngredientsModel(0,"Milk", R.drawable.s_milk)
        val fish = IngredientsModel(0,"Fish", R.drawable.s_fish)

        val data = mutableListOf(carrot, chicken, egg,pasta,apple,banana,cheese,rice,milk,fish)
        limitIngredientsList.postValue(data)
    }
}