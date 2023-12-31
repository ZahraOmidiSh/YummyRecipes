package com.zahra.yummyrecipes.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zahra.yummyrecipes.R.*
import com.zahra.yummyrecipes.data.database.entity.RecipeEntity
import com.zahra.yummyrecipes.data.repository.RecipeRepository
import com.zahra.yummyrecipes.models.recipe.ResponseRecipes
import com.zahra.yummyrecipes.utils.Constants.ADD_RECIPE_INFORMATION
import com.zahra.yummyrecipes.utils.Constants.API_KEY
import com.zahra.yummyrecipes.utils.Constants.ASCENDING
import com.zahra.yummyrecipes.utils.Constants.DESCENDING
import com.zahra.yummyrecipes.utils.Constants.DIET
import com.zahra.yummyrecipes.utils.Constants.HEALTHINESS
import com.zahra.yummyrecipes.utils.Constants.LIMITED_COUNT
import com.zahra.yummyrecipes.utils.Constants.MAIN_COURSE
import com.zahra.yummyrecipes.utils.Constants.NUMBER
import com.zahra.yummyrecipes.utils.Constants.POPULARITY
import com.zahra.yummyrecipes.utils.Constants.PRICE
import com.zahra.yummyrecipes.utils.Constants.SORT
import com.zahra.yummyrecipes.utils.Constants.SORT_DIRECTION
import com.zahra.yummyrecipes.utils.Constants.TIME
import com.zahra.yummyrecipes.utils.Constants.TRUE
import com.zahra.yummyrecipes.utils.Constants.TYPE
import com.zahra.yummyrecipes.utils.Constants.VEGAN
import com.zahra.yummyrecipes.utils.Constants.setAPIKEY
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository,
) : ViewModel() {
    //Greeting
    val Greeting = MutableLiveData<String>()
    private fun getGreeting(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 7..12 -> "Good Morning"
            in 12..17 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
    }

    private fun getEmojiByUnicode(): String {
        return String(Character.toChars(0x1f44b))
    }

    @SuppressLint("SetTextI18n")
    fun showGreeting() {
        Greeting.postValue("${getGreeting()} ${getEmojiByUnicode()}")
    }

    //Slogan
    val slogan = MutableLiveData<String>()

    @SuppressLint("SuspiciousIndentation")
    fun getSlogan() = viewModelScope.launch(Dispatchers.IO) {
        val randomNumber = Random.nextInt(0, 49)
        slogan.postValue(sloganList[randomNumber])
    }

    private val sloganList = listOf(
        "We are what we eat",
        "Walk right up to good food",
        "Dine and Dash",
        "Food: why not?",
        "Food: just what your body needs",
        "Food: all the cool kids are eating it!",
        "Food: what the doctor ordered",
        "Food: a prescription for good health",
        "Food: good for what ails you",
        "Food: integral part of a healthy day",
        "Food: it does a body good",
        "Food: eat & be happy",
        "Real Food. Real Local.",
        "Eat Less, Be at your Best",
        "Food, eat it!",
        "Food is what makes the world go around",
        "Get Your Veg On",
        "Hunger hurts. Please help us relieve the pain",
        "If you have a little extra, someone needs it",
        "Beef and reef",
        "It’s Good Mood Food",
        "Eat well feel well",
        "Healthy food taste better",
        "Eat drink and be merry",
        "Feed the Need",
        "Bread is your best food, eat more of it",
        "food with taste for copy paste",
        "Grow Smart. Eat Smart. Be Smart.",
        "Peace. Love. Veggies.",
        "Your taste – our passion!",
        "Dreaming of food",
        "Soup so big, it eats like a meal",
        "Be at Peace with your food",
        "Just taste it",
        "So Close You Can Taste It",
        "Love food so good",
        " Think Outside the Bun",
        "Taste the difference",
        "Quality is our Recipe",
        "Bananas are good all day long",
        "Never eat more than you can lift",
        "Good food ends with good talk",
        "Hunger is a good cook",
        "Have a break. Have a meal",
        "Call a friend, call meal",
        "Meal It's a kind of magic",
        "Food - It does a body good",
        "Hearty meal, better results",
        "Food always the right choice",
        "Taste is a never-ending story"
    )

    //---Suggested---//
    //Queries
    private fun getMealType(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 6..8 -> "breakfast"
            in 8..10 -> "breakfast,bread,appetizer"
            in 10..13 -> "main course,salad,appetizer,marinade"
            in 13..17 -> "soup,sauce,fingerfood,snack,drink,dessert"
            in 17..23 -> "main course,salad,side dish"
            else -> ""
        }
    }

    fun suggestedQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[API_KEY] = setAPIKEY()
        queries[TYPE] = getMealType()
        queries[NUMBER] = LIMITED_COUNT.toString()
        queries[ADD_RECIPE_INFORMATION] = TRUE
        queries[SORT] = POPULARITY
        queries[SORT_DIRECTION] = DESCENDING
        return queries
    }

    //Api
    val suggestedData = MutableLiveData<NetworkRequest<ResponseRecipes>>()
    fun callSuggestedApi(queries: Map<String, String>) = viewModelScope.launch {
        suggestedData.value = NetworkRequest.Loading()
        val response = repository.remote.getRecipe(queries)
        suggestedData.value = NetworkResponse(response).generalNetworkResponse()
        //Cache
        val cache = suggestedData.value?.data
        if (cache != null) {
            offlineSuggested(cache)
        }
    }

    //Local
    private fun saveSuggested(entity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.saveRecipes(entity)
    }

    val readSuggestedFromDb = repository.local.loadRecipes().asLiveData()
    private fun offlineSuggested(response: ResponseRecipes) {
        val entity = RecipeEntity(0, response)
        saveSuggested(entity)
    }


    //---Economical---//
    //Queries
    fun economicalQueries(number: Int): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[API_KEY] = setAPIKEY()
        queries[NUMBER] = number.toString()
        queries[ADD_RECIPE_INFORMATION] = TRUE
        queries[TYPE] = MAIN_COURSE
        queries[SORT] = PRICE
        queries["offset"] = "3"
        queries[SORT_DIRECTION] = ASCENDING
        return queries
    }

    //Api
    val economicalData = MutableLiveData<NetworkRequest<ResponseRecipes>>()
    fun callEconomicalApi(queries: Map<String, String>) = viewModelScope.launch {
        economicalData.value = NetworkRequest.Loading()
        val response = repository.remote.getRecipe(queries)
        economicalData.value = NetworkResponse(response).generalNetworkResponse()
        //Cache
        val cache = economicalData.value?.data
        if (cache != null) {
            offlineEconomical(cache)
        }

    }

    //local
    private fun saveEconomical(entity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.saveRecipes(entity)
    }

    val readEconomicalFromDb = repository.local.loadRecipes().asLiveData()
    private fun offlineEconomical(response: ResponseRecipes) {
        val entity = RecipeEntity(1, response)
        saveEconomical(entity)
    }

    //---Quick---//
    //Queries
    fun quickQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[API_KEY] = setAPIKEY()
        queries[NUMBER] = LIMITED_COUNT.toString()
        queries[ADD_RECIPE_INFORMATION] = TRUE
        queries[TYPE] = MAIN_COURSE
        queries[SORT] = TIME
        queries[SORT_DIRECTION] = ASCENDING
        return queries
    }

    //Api
    val quickData = MutableLiveData<NetworkRequest<ResponseRecipes>>()
    fun callQuickApi(queries: Map<String, String>) = viewModelScope.launch {
        quickData.value = NetworkRequest.Loading()
        val response = repository.remote.getRecipe(queries)
        quickData.value = NetworkResponse(response).generalNetworkResponse()

        //Cache
        val cache = quickData.value?.data
        if (cache != null) {
            offlineQuick(cache)
        }

    }

    //local
    private fun saveQuick(entity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.saveRecipes(entity)
    }

    val readQuickFromDb = repository.local.loadRecipes().asLiveData()
    private fun offlineQuick(response: ResponseRecipes) {
        val entity = RecipeEntity(2, response)
        saveQuick(entity)
    }


    //---Vegan---//
    //Queries
    fun veganQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[API_KEY] = setAPIKEY()
        queries[NUMBER] = LIMITED_COUNT.toString()
        queries[ADD_RECIPE_INFORMATION] = TRUE
        queries[DIET] = VEGAN
        queries[SORT] = POPULARITY
        queries[SORT_DIRECTION] = DESCENDING
        return queries
    }

    //Api
    val veganData = MutableLiveData<NetworkRequest<ResponseRecipes>>()
    fun callVeganApi(queries: Map<String, String>) = viewModelScope.launch {
        veganData.value = NetworkRequest.Loading()
        val response = repository.remote.getRecipe(queries)
        veganData.value = NetworkResponse(response).generalNetworkResponse()

        //Cache
        val cache = veganData.value?.data
        if (cache != null) {
            offlineVegan(cache)
        }

    }

    //local
    private fun saveVegan(entity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.saveRecipes(entity)
    }

    val readVeganFromDb = repository.local.loadRecipes().asLiveData()
    private fun offlineVegan(response: ResponseRecipes) {
        val entity = RecipeEntity(3, response)
        saveVegan(entity)
    }


    //---Healthy---//
    //Queries
    fun healthyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[API_KEY] = setAPIKEY()
        queries[NUMBER] = LIMITED_COUNT.toString()
        queries[ADD_RECIPE_INFORMATION] = TRUE
        queries[SORT] = HEALTHINESS
        return queries
    }

    //Api
    val healthyData = MutableLiveData<NetworkRequest<ResponseRecipes>>()
    fun callHealthyApi(queries: Map<String, String>) = viewModelScope.launch {
        healthyData.value = NetworkRequest.Loading()
        val response = repository.remote.getRecipe(queries)
        healthyData.value = NetworkResponse(response).generalNetworkResponse()

        //Cache
        val cache = healthyData.value?.data
        if (cache != null) {
            offlineHealthy(cache)
        }

    }

    //local
    private fun saveHealthy(entity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.saveRecipes(entity)
    }

    val readHealthyFromDb = repository.local.loadRecipes().asLiveData()
    private fun offlineHealthy(response: ResponseRecipes) {
        val entity = RecipeEntity(4, response)
        saveHealthy(entity)
    }
}