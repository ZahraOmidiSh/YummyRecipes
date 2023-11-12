package com.zahra.yummyrecipes.viewmodel

import android.app.SearchManager.QUERY
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
import com.zahra.yummyrecipes.utils.Constants.MY_API_KEY
import com.zahra.yummyrecipes.utils.Constants.NUMBER
import com.zahra.yummyrecipes.utils.Constants.TRUE
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections.list
import javax.inject.Inject

@HiltViewModel

class SearchViewModel @Inject constructor(private val repository: SearchRepository) : ViewModel() {

    //SlideOffset for Button Position
    var slideOffset = 0f

    //Selected Ingredient List
    val _selectedIngredientsNameData = MutableLiveData<List<String>>()
    val selectedIngredientsNameData: LiveData<List<String>> get() = _selectedIngredientsNameData
    fun updateSelectedIngredientsName() {
        val updatedSelectedList = expandedIngredientsList.value?.filter {
            it.isSelected
        }
        val nameList = mutableListOf<String>()
        updatedSelectedList?.forEach {
            nameList.add(it.ingredientsName)
        }
        _selectedIngredientsNameData.value = nameList
    }

    //Expanded
    val _expandedIngredientsList = MutableLiveData<List<IngredientsModel>>()
    val expandedIngredientsList: LiveData<List<IngredientsModel>> get() = _expandedIngredientsList

    fun updateExpandedIngredientByName(ingredientName: String, isSelected: Boolean) {
        val currentList = _expandedIngredientsList.value ?: return
        val updatedList = currentList.toMutableList()
        val itemToUpdate = updatedList.find { it.ingredientsName == ingredientName }
        itemToUpdate?.isSelected = isSelected
        _expandedIngredientsList.value = updatedList
    }

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
                "carrot" -> ingredients.add(IngredientsModel(0, ingredient, R.drawable.s_carrot))
                "chicken" -> ingredients.add(IngredientsModel(1, ingredient, R.drawable.s_chicken))
                "egg" -> ingredients.add(IngredientsModel(2, ingredient, R.drawable.s_egg))
                "pasta" -> ingredients.add(IngredientsModel(3, ingredient, R.drawable.s_pasta))
                "apple" -> ingredients.add(IngredientsModel(4, ingredient, R.drawable.s_apple))
                "banana" -> ingredients.add(IngredientsModel(5, ingredient, R.drawable.s_banana))
                "cheese" -> ingredients.add(IngredientsModel(6, ingredient, R.drawable.s_cheese))
                "rice" -> ingredients.add(IngredientsModel(7, ingredient, R.drawable.s_rice))
                "milk" -> ingredients.add(IngredientsModel(8, ingredient, R.drawable.s_milk))
                "fish" -> ingredients.add(IngredientsModel(9, ingredient, R.drawable.s_fish))
                "shrimp" -> ingredients.add(IngredientsModel(10, ingredient, R.drawable.s_shrimp))
                "avocado" -> ingredients.add(IngredientsModel(11, ingredient, R.drawable.s_avocado))
                "meat" -> ingredients.add(IngredientsModel(12, ingredient, R.drawable.s_meat))
                "potato" -> ingredients.add(IngredientsModel(13, ingredient, R.drawable.s_potato))
                "honey" -> ingredients.add(IngredientsModel(14, ingredient, R.drawable.s_honey))
                "tomato" -> ingredients.add(IngredientsModel(15, ingredient, R.drawable.s_tomato))
                "flour" -> ingredients.add(IngredientsModel(16, ingredient, R.drawable.s_flour))
                "broccoli" -> ingredients.add(
                    IngredientsModel(
                        17,
                        ingredient,
                        R.drawable.s_broccoli
                    )
                )

                "strawberries" -> ingredients.add(
                    IngredientsModel(
                        18, ingredient,
                        R.drawable.s_strawberries
                    )
                )

                "butter" -> ingredients.add(IngredientsModel(19, ingredient, R.drawable.s_butter))
                "darkChocolate" -> ingredients.add(
                    IngredientsModel(
                        20,
                        "dark chocolate",
                        R.drawable.s_dark_chocolate
                    )
                )

                "pineapple" -> ingredients.add(
                    IngredientsModel(
                        21,
                        "pine apple",
                        R.drawable.s_pineapple
                    )
                )

                "beans" -> ingredients.add(IngredientsModel(22, ingredient, R.drawable.s_beans))
                "peanutButter" -> ingredients.add(
                    IngredientsModel(
                        23,
                        "peanut butter",
                        R.drawable.s_peanut_butter
                    )
                )
            }
        }
        return ingredients
    }

    fun searchQueries(search: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[API_KEY] = MY_API_KEY
        queries[NUMBER] = FULL_COUNT.toString()
        queries[ADD_RECIPE_INFORMATION] = TRUE
        queries[QUERY] = search
        return queries
    }

    val searchData = MutableLiveData<NetworkRequest<ResponseRecipes>>()

    fun callSearchApi(queries: Map<String, String>) = viewModelScope.launch {
        searchData.value = NetworkRequest.Loading()
        val response = repository.getSearchRecipes(queries)
        searchData.value = NetworkResponse(response).generalNetworkResponse()
    }

}