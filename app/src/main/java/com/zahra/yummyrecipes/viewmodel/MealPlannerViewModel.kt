package com.zahra.yummyrecipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zahra.yummyrecipes.data.repository.FavoriteRepository
import com.zahra.yummyrecipes.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MealPlannerViewModel @Inject constructor(repository: MealRepository) : ViewModel() {
    val data = "Monday"
    val readPlannedMealData = repository.local.loadPlannedMeals(data).asLiveData()


    //setWeek calendar
    fun setCalendar(){
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK,calendar.firstDayOfWeek)
    }



}