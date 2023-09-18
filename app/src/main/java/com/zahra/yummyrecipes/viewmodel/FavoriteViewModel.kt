package com.zahra.yummyrecipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zahra.yummyrecipes.data.repository.FavoriteRepository
import com.zahra.yummyrecipes.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(repository: FavoriteRepository):ViewModel() {
    val readFavoriteData = repository.local.loadFavorite().asLiveData()
}