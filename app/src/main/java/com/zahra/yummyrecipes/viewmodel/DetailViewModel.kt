package com.zahra.yummyrecipes.viewmodel

import androidx.lifecycle.ViewModel
import com.zahra.yummyrecipes.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository:RecipeRepository):ViewModel() {

}