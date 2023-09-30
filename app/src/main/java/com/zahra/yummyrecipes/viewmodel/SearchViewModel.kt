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

class SearchViewModel @Inject constructor() : ViewModel() {
    //Ingredients
    //Limited
    val limitIngredientsList = MutableLiveData<MutableList<IngredientsModel>>()
    fun loadLimitIngredientsList() = viewModelScope.launch(Dispatchers.IO) {
        val carrot = IngredientsModel(0, "Carrot", R.drawable.s_carrot)
        val chicken = IngredientsModel(0, "Chicken", R.drawable.s_chicken)
        val egg = IngredientsModel(0, "Egg", R.drawable.s_egg)
        val pasta = IngredientsModel(0, "Pasta", R.drawable.s_pasta)
        val apple = IngredientsModel(0, "Apple", R.drawable.s_apple)
        val banana = IngredientsModel(0, "Banana", R.drawable.s_banana)
        val cheese = IngredientsModel(0, "Cheese", R.drawable.s_cheese)
        val rice = IngredientsModel(0, "Rice", R.drawable.s_rice)
        val milk = IngredientsModel(0, "Milk", R.drawable.s_milk)
        val fish = IngredientsModel(0, "Fish", R.drawable.s_fish)

        val data =
            mutableListOf(carrot, chicken, egg, pasta, apple, banana, cheese, rice, milk, fish)
        limitIngredientsList.postValue(data)
    }

    //Expanded
    val expandedIngredientsList = MutableLiveData<MutableList<IngredientsModel>>()
    fun loadExpandedIngredientsList() = viewModelScope.launch(Dispatchers.IO) {
        val carrot = IngredientsModel(0, "Carrot", R.drawable.s_carrot)
        val chicken = IngredientsModel(0, "Chicken", R.drawable.s_chicken)
        val egg = IngredientsModel(0, "Egg", R.drawable.s_egg)
        val pasta = IngredientsModel(0, "Pasta", R.drawable.s_pasta)
        val apple = IngredientsModel(0, "Apple", R.drawable.s_apple)
        val banana = IngredientsModel(0, "Banana", R.drawable.s_banana)
        val cheese = IngredientsModel(0, "Cheese", R.drawable.s_cheese)
        val rice = IngredientsModel(0, "Rice", R.drawable.s_rice)
        val milk = IngredientsModel(0, "Milk", R.drawable.s_milk)
        val fish = IngredientsModel(0, "Fish", R.drawable.s_fish)
        val shrimp = IngredientsModel(0, "Shrimp", R.drawable.s_shrimp)
        val avocado = IngredientsModel(0, "Avocado", R.drawable.s_avocado)
        val meat = IngredientsModel(0, "Meat", R.drawable.s_meat)
        val potato = IngredientsModel(0, "Potato", R.drawable.s_potato)
        val honey = IngredientsModel(0, "Honey", R.drawable.s_honey)
        val tomato = IngredientsModel(0, "Tomato", R.drawable.s_tomato)
        val flour = IngredientsModel(0, "Flour", R.drawable.s_flour)
        val broccoli = IngredientsModel(0, "Broccoli", R.drawable.s_broccoli)
        val strawberries = IngredientsModel(0, "Strawberry", R.drawable.s_strawberries)
        val butter = IngredientsModel(0, "Butter", R.drawable.s_butter)
        val darkChocolate = IngredientsModel(0, "Dark Chocolate", R.drawable.s_dark_chocolate)
        val pineapple = IngredientsModel(0, "Pineapple", R.drawable.s_pineapple)
        val beans = IngredientsModel(0, "Beans", R.drawable.s_beans)
        val peanutButter = IngredientsModel(0, "Peanut Butter", R.drawable.s_peanut_butter)


        val data =
            mutableListOf(carrot, chicken, egg
                , pasta, apple, banana, cheese, rice, milk, fish,
            shrimp,avocado,meat,potato,honey,tomato,flour,broccoli,strawberries,butter,darkChocolate,pineapple,beans,peanutButter
            )
        expandedIngredientsList.postValue(data)
    }
}