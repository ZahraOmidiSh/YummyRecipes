package com.zahra.yummyrecipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zahra.yummyrecipes.data.repository.FavoriteRepository
import com.zahra.yummyrecipes.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MealPlannerViewModel @Inject constructor(repository: MealRepository) : ViewModel() {
    val data = "Monday"
    val readPlannedMealData = repository.local.loadPlannedMeals(data).asLiveData()
}