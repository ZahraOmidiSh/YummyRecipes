package com.zahra.yummyrecipes.viewmodel

import android.app.SearchManager.QUERY
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.data.repository.SearchRepository
import com.zahra.yummyrecipes.models.recipe.ResponseRecipes
import com.zahra.yummyrecipes.models.search.IngredientsModel
import com.zahra.yummyrecipes.utils.Constants.ADD_RECIPE_INFORMATION
import com.zahra.yummyrecipes.utils.Constants.API_KEY
import com.zahra.yummyrecipes.utils.Constants.FULL_COUNT
import com.zahra.yummyrecipes.utils.Constants.INGREDIENTS
import com.zahra.yummyrecipes.utils.Constants.MY_API_KEY
import com.zahra.yummyrecipes.utils.Constants.NUMBER
import com.zahra.yummyrecipes.utils.Constants.TRUE
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class SearchViewModel @Inject constructor(private val repository: SearchRepository) : ViewModel() {
    //Limited
    /*
    val limitIngredientsList = MutableLiveData<MutableList<IngredientsModel>>()
    fun loadLimitIngredientsList() = viewModelScope.launch(Dispatchers.IO) {
        val data = loadIngredientsList(
            "carrot",
            "chicken",
            "egg",
            "pasta",
            "apple",
            "banana",
            "cheese",
            "rice",
            "milk",
            "fish"
        )
        limitIngredientsList.postValue(data)
    }
*/

    //Expanded
     val _expandedIngredientsList = MutableLiveData<List<IngredientsModel>>()
    val expandedIngredientsList: LiveData<List<IngredientsModel>> get() = _expandedIngredientsList
    fun loadExpandedIngredientsList() = viewModelScope.launch(Dispatchers.IO) {
        val data = loadIngredientsList(
            "carrot",
            "chicken",
            "egg",
            "pasta",
            "apple",
            "banana",
            "cheese",
            "rice",
            "milk",
            "fish",
            "shrimp",
            "avocado",
            "meat",
            "potato",
            "honey",
            "tomato",
            "flour",
            "broccoli",
            "strawberries",
            "butter",
            "darkChocolate",
            "pineapple",
            "beans",
            "peanutButter"
        )
        _expandedIngredientsList.postValue(data)
    }

    private fun loadIngredientsList(vararg strings: String): MutableList<IngredientsModel> {
        val ingredients = mutableListOf<IngredientsModel>()
        strings.forEach { ingredient ->
            when (ingredient) {
                "carrot" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_carrot))
                "chicken" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_chicken))
                "egg" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_egg))
                "pasta" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_pasta))
                "apple" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_apple))
                "banana" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_banana))
                "cheese" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_cheese))
                "rice" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_rice))
                "milk" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_milk))
                "fish" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_fish))
                "shrimp" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_shrimp))
                "avocado" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_avocado))
                "meat" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_meat))
                "potato" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_potato))
                "honey" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_honey))
                "tomato" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_tomato))
                "flour" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_flour))
                "broccoli" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_broccoli))
                "strawberries" -> ingredients.add(
                    IngredientsModel(
                        ingredient,
                        R.drawable.s_strawberries
                    )
                )

                "butter" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_butter))
                "darkChocolate" -> ingredients.add(
                    IngredientsModel(
                        "dark chocolate",
                        R.drawable.s_dark_chocolate
                    )
                )

                "pineapple" -> ingredients.add(
                    IngredientsModel(
                        "pine apple",
                        R.drawable.s_pineapple
                    )
                )

                "beans" -> ingredients.add(IngredientsModel(ingredient, R.drawable.s_beans))
                "peanutButter" -> ingredients.add(
                    IngredientsModel(
                        "peanut butter",
                        R.drawable.s_peanut_butter
                    )
                )
            }
        }
        return ingredients
    }

    fun searchQueries(search:String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()   //همینجا اینیشیالایز میکنیم
        queries[API_KEY] = MY_API_KEY
        queries[NUMBER] = FULL_COUNT.toString()
        queries[ADD_RECIPE_INFORMATION] = TRUE
        queries[QUERY] =search
        return queries
    }

    val searchData = MutableLiveData<NetworkRequest<ResponseRecipes>>()

    fun callSearchApi(queries: Map<String, String>) = viewModelScope.launch {
        searchData.value = NetworkRequest.Loading()
        //داخل پرانتز پایین باید QueryMap را ست کنیم
        val response = repository.getSearchRecipes(queries)
        searchData.value = NetworkResponse(response).generalNetworkResponse()
    }


    //SelectedItems
    val selectedItems = MutableLiveData<MutableList<IngredientsModel>>()

    fun fillSelectedItems(list:MutableList<IngredientsModel>)= viewModelScope.launch(Dispatchers.IO) {
        selectedItems.postValue(list)
    }

}