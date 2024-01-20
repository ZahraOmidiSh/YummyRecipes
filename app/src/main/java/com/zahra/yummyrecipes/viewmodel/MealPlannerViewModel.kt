package com.zahra.yummyrecipes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zahra.yummyrecipes.data.database.entity.MealPlannerEntity
import com.zahra.yummyrecipes.data.repository.MealRepository
import com.zahra.yummyrecipes.models.detail.ResponseDetail
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MealPlannerViewModel @Inject constructor(
    private val repository: MealRepository,
) : ViewModel() {
    //Call meal api
    val mealData = MutableLiveData<NetworkRequest<ResponseDetail>>()
    fun callMealApi(id: Int, apikey: String) = viewModelScope.launch {
        mealData.value = NetworkRequest.Loading()
        val response = repository.remote.getDetail(id, apikey, true)
        mealData.value = NetworkResponse(response).generalNetworkResponse()
    }

    //save
    fun saveMeal(data: ResponseDetail, date: String) = viewModelScope.launch {
        val newId = (date + data.id).toLong()
        val entity = MealPlannerEntity(newId, data)
        repository.local.savePlannedMeal(entity)
    }

    fun deleteMeal(entity: MealPlannerEntity) = viewModelScope.launch {
        repository.local.deletePlannedMeal(entity)
    }

    fun readMealsOfEachDay(day: String): LiveData<List<MealPlannerEntity>> {
        var mealsForDayList = repository.local.loadPlannedMeals(dateStringList[0]).asLiveData()
        if (day == "sunday") {
            mealsForDayList = repository.local.loadPlannedMeals(dateStringList[0]).asLiveData()
        }
        if (day == "monday") {
            mealsForDayList = repository.local.loadPlannedMeals(dateStringList[1]).asLiveData()
        }
        if (day == "tuesday") {
            mealsForDayList = repository.local.loadPlannedMeals(dateStringList[2]).asLiveData()
        }
        if (day == "wednesday") {
            mealsForDayList = repository.local.loadPlannedMeals(dateStringList[3]).asLiveData()
        }
        if (day == "thursday") {
            mealsForDayList = repository.local.loadPlannedMeals(dateStringList[4]).asLiveData()
        }
        if (day == "friday") {
            mealsForDayList = repository.local.loadPlannedMeals(dateStringList[5]).asLiveData()
        }
        if (day == "saturday") {
            mealsForDayList = repository.local.loadPlannedMeals(dateStringList[6]).asLiveData()
        }
        return mealsForDayList
    }

    var recipeId = MutableLiveData<Int>()


    //Get The current date
    private var theDay = Date()

    //Create a Calendar instance
    private val calendar = Calendar.getInstance()
    val dateList = mutableListOf<String>()
    val dateStringList = mutableListOf<String>()

    init {
        // Set the calendar to the current date
        calendar.time = Date()
        // Find the current Sunday
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        updateDatesOfWeekDays()
    }

    private fun updateDatesOfWeekDays() {
        dateList.clear()
        dateStringList.clear()
        // Add dates for the current week to the list
        for (i in 0 until 7) {
            dateList.add(formatDate(calendar.time))
            dateStringList.add(formatDateWithMonthDay(calendar.time))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        setWeekTitle(Date(), theDay)
    }

    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())
        return dateFormat.format(date)
    }

    private fun formatDateWithMonthDay(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return dateFormat.format(date)
    }

    var weekText = "THIS WEEK"
    private fun setWeekTitle(today: Date, currentDay: Date) {
        val todayStartOfWeek = getStartOfWeek(today).toInt()
        val currentStartOfWeek = getStartOfWeek(currentDay).toInt()
        val differenceInDays = currentStartOfWeek - todayStartOfWeek

        weekText = when (differenceInDays) {
            0 -> "THIS WEEK"
            -7 -> "LAST WEEK"
            7 -> "NEXT WEEK"
            in -8878..-8874 -> "LAST WEEK"
            else -> "${dateList[0]} - ${dateList[6]}"
        }

    }

    private fun getStartOfWeek(inputDate: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = inputDate
        calendar.firstDayOfWeek = Calendar.SUNDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        // Optional: If you want to set the time to midnight (00:00:00)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun moveOneWeek(direction: Int) {
        calendar.time = theDay
        calendar.add(Calendar.DAY_OF_YEAR, direction)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        theDay = calendar.time
        updateDatesOfWeekDays()
    }

    fun goToCurrentWeek() {
        calendar.time = Date()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        theDay = calendar.time
        updateDatesOfWeekDays()
    }

    fun isTheDatePassed(date: String): Boolean {
         val today = Calendar.getInstance()
        return date < formatDateWithMonthDay(today.time)
    }
}