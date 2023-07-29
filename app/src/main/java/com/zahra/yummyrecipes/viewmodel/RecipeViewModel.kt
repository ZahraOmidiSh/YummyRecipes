package com.zahra.yummyrecipes.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.yummyrecipes.data.repository.RecipeRepository
import com.zahra.yummyrecipes.models.recipe.ResponseRecipes
import com.zahra.yummyrecipes.utils.Constants.ADD_RECIPE_INFORMATION
import com.zahra.yummyrecipes.utils.Constants.API_KEY
import com.zahra.yummyrecipes.utils.Constants.MY_API_KEY
import com.zahra.yummyrecipes.utils.Constants.NUMBER
import com.zahra.yummyrecipes.utils.Constants.TYPE
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository,
) : ViewModel() {

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
    fun suggestedQueries():HashMap<String,String>{
        val queries:HashMap<String,String> = HashMap()
        queries[API_KEY] = MY_API_KEY
        queries[TYPE] = MY_API_KEY
        queries[NUMBER] = MY_API_KEY
        queries[ADD_RECIPE_INFORMATION] = MY_API_KEY
        return queries
    }

    //Api
    val suggestedData = MutableLiveData<NetworkRequest<ResponseRecipes>>()
    fun callSuggestedApi(queries:Map<String,String>) = viewModelScope.launch {
        suggestedData.value=NetworkRequest.Loading()
        val response =repository.remote.getRecipe(queries)
        suggestedData.value=NetworkResponse(response).generalNetworkResponse()

    }
}