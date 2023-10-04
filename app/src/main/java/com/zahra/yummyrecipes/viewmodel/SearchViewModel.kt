package com.zahra.yummyrecipes.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.models.detail.ResponseDetail
import com.zahra.yummyrecipes.models.search.IngredientsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class SearchViewModel @Inject constructor() : ViewModel() {

     val _ingredientItems  = mutableListOf<IngredientsModel>()
    val ingredientItems : List<IngredientsModel> get() = _ingredientItems

    fun loadIngredients() {
        _ingredientItems.clear()
        _ingredientItems.addAll(
            listOf(
                IngredientsModel("carrot", R.drawable.s_carrot),
                IngredientsModel("chicken", R.drawable.s_chicken),
                IngredientsModel("egg", R.drawable.s_egg),
                IngredientsModel("pasta", R.drawable.s_pasta),
                IngredientsModel("apple", R.drawable.s_apple),
                IngredientsModel("banana", R.drawable.s_banana)
            )
        )
    }
    fun getSelectedItems(): List<IngredientsModel> {
        return _ingredientItems.filter { it.isSelected }
    }
    fun toggleItemSelected(position: Int) {
        _ingredientItems[position].isSelected = !_ingredientItems[position].isSelected
    }
    fun clearSelection() {
        _ingredientItems.forEach { it.isSelected = false }
    }
    //Limited
    val limitIngredientsList = MutableLiveData<MutableList<IngredientsModel>>()
    fun loadLimitIngredientsList() = viewModelScope.launch(Dispatchers.IO) {
        val data =loadIngredientsList("carrot", "chicken", "egg", "pasta", "apple", "banana", "cheese", "rice", "milk", "fish")
        limitIngredientsList.postValue(data)
    }
    //Expanded
//    val expandedIngredientsList = MutableLiveData<MutableList<IngredientsModel>>()
//    fun loadExpandedIngredientsList() = viewModelScope.launch(Dispatchers.IO) {
//        val data =loadIngredientsList("carrot", "chicken", "egg", "pasta", "apple", "banana", "cheese", "rice", "milk", "fish",
//            "shrimp","avocado","meat","potato","honey","tomato","flour","broccoli","strawberries","butter","darkChocolate","pineapple","beans","peanutButter")
//        expandedIngredientsList.postValue(data)
//    }
    private fun loadIngredientsList(vararg strings: String):MutableList<IngredientsModel> {
        val ingredients = mutableListOf<IngredientsModel>()

        strings.forEach { ingredient->
            when(ingredient){
                "carrot"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_carrot))
                "chicken"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_chicken))
                "egg"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_egg))
                "pasta"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_pasta))
                "apple"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_apple))
                "banana"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_banana))
                "cheese"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_cheese))
                "rice"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_rice))
                "milk"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_milk))
                "fish"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_fish))
                "shrimp"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_shrimp))
                "avocado"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_avocado))
                "meat"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_meat))
                "potato"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_potato))
                "honey"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_honey))
                "tomato"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_tomato))
                "flour"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_flour))
                "broccoli"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_broccoli))
                "strawberries"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_strawberries))
                "butter"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_butter))
                "darkChocolate"-> ingredients.add(IngredientsModel("dark chocolate", R.drawable.s_dark_chocolate))
                "pineapple"-> ingredients.add(IngredientsModel("pine apple", R.drawable.s_pineapple))
                "beans"-> ingredients.add(IngredientsModel(ingredient, R.drawable.s_beans))
                "peanutButter"-> ingredients.add(IngredientsModel("peanut butter", R.drawable.s_peanut_butter))
            }
        }
        return ingredients
    }



}