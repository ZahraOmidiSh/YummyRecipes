package com.zahra.yummyrecipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zahra.yummyrecipes.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(repository: RecipeRepository):ViewModel() {
    val readFavoriteData = repository.local.loadFavorite().asLiveData()
}