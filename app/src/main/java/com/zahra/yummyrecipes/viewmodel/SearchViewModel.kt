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
import com.zahra.yummyrecipes.utils.Constants.CUISINE
import com.zahra.yummyrecipes.utils.Constants.DIET
import com.zahra.yummyrecipes.utils.Constants.EQUIPMENT
import com.zahra.yummyrecipes.utils.Constants.INCLUDE_INGREDIENTS
import com.zahra.yummyrecipes.utils.Constants.INTOLERANCES
import com.zahra.yummyrecipes.utils.Constants.MAX_CALORIES
import com.zahra.yummyrecipes.utils.Constants.MAX_READY_TIME
import com.zahra.yummyrecipes.utils.Constants.MIN_CALORIES
import com.zahra.yummyrecipes.utils.Constants.NUMBER
import com.zahra.yummyrecipes.utils.Constants.TRUE
import com.zahra.yummyrecipes.utils.Constants.TYPE
import com.zahra.yummyrecipes.utils.Constants.setAPIKEY
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class SearchViewModel @Inject constructor(private val repository: SearchRepository) : ViewModel() {
    //total search
    var totalSearch = MutableLiveData<Boolean>()

    //method for giving value to total search
    fun updateTotalSearchValue() {
        totalSearch.value =
            (isSearchWithFilters.value == true || isSearchWithIngredient.value == true
                    || isSearchWithDietsOrAllergies.value == true || searchString.value.toString()
                .isNotEmpty())
    }

    //search String
    var searchString = MutableLiveData<String>()

    //Close Button is pressed
    var isCloseButtonPressed = MutableLiveData<Boolean>()

    //Search With Ingredient
    var isSearchWithIngredient = MutableLiveData<Boolean>()

    //SlideOffset for Button Position in Ingredient Bottom Sheet Fragment
    var ingredientSlideOffset = 0f

    //SlideOffset for Button Position in Filters Bottom Sheet Fragment
    var filterSlideOffset = 0f

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

    fun updateExpandedIngredientBySelectedNames() {
        _expandedIngredientsList.value!!.forEach {
            if (it.isSelected) {
                it.isSelected = false
            }
        }
        _selectedIngredientsNameData.value?.forEach {
            updateExpandedIngredientByName(it, true)
        }
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
        //Ingredients
        if (selectedIngredientsToString() != "NO") {
            queries[INCLUDE_INGREDIENTS] = selectedIngredientsToString()
        }
        //Meals
        if (isSearchWithMeals.value == true) {
            var meals = ""
            selectedMealsData.value?.forEach {
                meals = "$meals,$it".trim()
            }
            queries[TYPE] = meals
        }

        //Tools
        if (isSearchWithTools.value == true) {
            var tools = ""
            selectedToolsData.value?.forEach {
                tools = "$tools,$it".trim()
            }
            queries[EQUIPMENT] = tools
        }

        //Region
        if (isSearchWithRegion.value == true) {
            var region = ""
            selectedRegionData.value?.forEach {
                region = "$region,$it".trim()
            }
            queries[CUISINE] = region
        }
        //Diet
        if (isSearchWithDiets.value == true) {
            var diets = ""
            selectedDietsData.value?.forEach {
                diets = "$diets|$it".trim()
            }
            queries[DIET] = diets
        }
        //Allergies
        if (isSearchWithAllergies.value == true) {
            var allergies = ""
            selectedAllergiesData.value?.forEach {
                allergies = "$allergies,$it".trim()
            }
            queries[INTOLERANCES] = allergies
        }
        //Time
        if (isSearchWithTime.value == true) {
            selectedTimeData.value?.forEach {
                queries[MAX_READY_TIME] = it
            }
        }

        //Calorie
        if (isSearchWithCalorie.value == true) {
            selectedCalorieData.value?.forEach {
                when (it) {
                    "0-200" -> {
                        queries[MAX_CALORIES] = 200.toString()
                        queries[MIN_CALORIES] = 0.toString()
                    }

                    "200-400" -> {
                        queries[MAX_CALORIES] = 400.toString()
                        queries[MIN_CALORIES] = 200.toString()
                    }

                    "400-600" -> {
                        queries[MAX_CALORIES] = 600.toString()
                        queries[MIN_CALORIES] = 400.toString()
                    }

                    "600-800" -> {
                        queries[MAX_CALORIES] = 600.toString()
                        queries[MIN_CALORIES] = 400.toString()
                    }
                }
            }
        }
        queries[API_KEY] = setAPIKEY()
        queries[NUMBER] = 8.toString()
        queries[ADD_RECIPE_INFORMATION] = TRUE
        queries[QUERY] = search
        return queries
    }

    //Diets and Allergies
    var isSearchWithDietsOrAllergies = MutableLiveData<Boolean>()

    //Diets
    var isSearchWithDiets = MutableLiveData<Boolean>()

    //Selected Diets
    val _selectedDietsData = MutableLiveData<List<String>>()
    val selectedDietsData: LiveData<List<String>> get() = _selectedDietsData

    //Allergies
    var isSearchWithAllergies = MutableLiveData<Boolean>()

    //Selected Allergies
    val _selectedAllergiesData = MutableLiveData<List<String>>()
    val selectedAllergiesData: LiveData<List<String>> get() = _selectedAllergiesData

    //Meal and Time and Region and Calorie and Tools
    var isSearchWithFilters = MutableLiveData<Boolean>()

    //Meal
    var isSearchWithMeals = MutableLiveData<Boolean>()

    //Selected Meals
    val _selectedMealsData = MutableLiveData<List<String>>()
    val selectedMealsData: LiveData<List<String>> get() = _selectedMealsData

    //Tools
    var isSearchWithTools = MutableLiveData<Boolean>()

    //Selected Tools
    val _selectedToolsData = MutableLiveData<List<String>>()
    val selectedToolsData: LiveData<List<String>> get() = _selectedToolsData

    //Region
    var isSearchWithRegion = MutableLiveData<Boolean>()

    //Selected Region
    val _selectedRegionData = MutableLiveData<List<String>>()
    val selectedRegionData: LiveData<List<String>> get() = _selectedRegionData

    //Time
    var isSearchWithTime = MutableLiveData<Boolean>()

    //Selected Time
    val _selectedTimeData = MutableLiveData<List<String>>()
    val selectedTimeData: LiveData<List<String>> get() = _selectedTimeData

    //Calorie per Serving
    var isSearchWithCalorie = MutableLiveData<Boolean>()

    //Selected Calorie
    val _selectedCalorieData = MutableLiveData<List<String>>()
    val selectedCalorieData: LiveData<List<String>> get() = _selectedCalorieData


    private fun selectedIngredientsToString(): String {
        var ingredients = ""
        if (isSearchWithIngredient.value == true) {
            selectedIngredientsNameData.value?.forEach {
                ingredients = "$ingredients&$it"
            }
            ingredients = ingredients.removeRange(0, 1)
            return ingredients
        } else {
            return "NO"
        }
    }

    //Debounce
    val searchData = MutableLiveData<NetworkRequest<ResponseRecipes>>()
    private val searchQueryChannel = Channel<Map<String, String>>()

    init {
        viewModelScope.launch {
            searchQueryChannel.receiveAsFlow().debounce(600).collectLatest { query ->
                searchData.value = NetworkRequest.Loading()
                val response = repository.getSearchRecipes(query)
                searchData.value = NetworkResponse(response).generalNetworkResponse()
            }
        }
    }

    fun callSearchApi(queries: Map<String, String>) = viewModelScope.launch {
        searchQueryChannel.send(queries)
    }

}