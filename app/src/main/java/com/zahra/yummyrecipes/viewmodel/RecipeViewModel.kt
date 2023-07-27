package com.zahra.yummyrecipes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.yummyrecipes.data.repository.RecipeRepository
import com.zahra.yummyrecipes.models.recipe.ResponseQuotes
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


    fun getSlogan()=viewModelScope.launch (Dispatchers.IO){
    val randomNumber=Random.nextInt(0, 3)
        slogan.postValue(SloganList[randomNumber])
    }

  val SloganList= listOf(
      "We are what we eat",
      "Walk right up to good food",
      "Dine and Dash",
      "Food: why not?"
  )
}