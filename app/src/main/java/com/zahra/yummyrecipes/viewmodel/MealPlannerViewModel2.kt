package com.zahra.yummyrecipes.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zahra.yummyrecipes.data.database.entity.MealPlannerEntity
import com.zahra.yummyrecipes.data.repository.MealRepository
import com.zahra.yummyrecipes.models.detail.ResponseDetail
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MealPlannerViewModel2 @Inject constructor(
    private val repository: MealRepository,
) : ViewModel() {
    //Call meal api
    val mealData = MutableLiveData<NetworkRequest<ResponseDetail>>()

    private var _mealsForSundayList = MutableLiveData<List<MealPlannerEntity>>()
    val mealsForSundayList: LiveData<List<MealPlannerEntity>> get() = _mealsForSundayList

    private var _mealsForMondayList = MutableLiveData<List<MealPlannerEntity>>()
    val mealsForMondayList: LiveData<List<MealPlannerEntity>> get() = _mealsForMondayList

    private var _mealsForTuesdayList = MutableLiveData<List<MealPlannerEntity>>()
    val mealsForTuesdayList: LiveData<List<MealPlannerEntity>> get() = _mealsForTuesdayList

    private var _mealsForWednesdayList = MutableLiveData<List<MealPlannerEntity>>()
    val mealsForWednesdayList: LiveData<List<MealPlannerEntity>> get() = _mealsForWednesdayList

    private var _mealsForThursdayList = MutableLiveData<List<MealPlannerEntity>>()
    val mealsForThursdayList: LiveData<List<MealPlannerEntity>> get() = _mealsForThursdayList

    private var _mealsForFridayList = MutableLiveData<List<MealPlannerEntity>>()
    val mealsForFridayList: LiveData<List<MealPlannerEntity>> get() = _mealsForFridayList

    private var _mealsForSaturdayList = MutableLiveData<List<MealPlannerEntity>>()
    val mealsForSaturdayList: LiveData<List<MealPlannerEntity>> get() = _mealsForSaturdayList


    fun callMealApi(id: Int, apikey: String) = viewModelScope.launch {
        mealData.value = NetworkRequest.Loading()
        val response = repository.remote.getDetail(id, apikey, true)
        mealData.value = NetworkResponse(response).generalNetworkResponse()
    }

    var theEntity = MutableLiveData<MealPlannerEntity>()

    //save
    fun saveMeal(data: ResponseDetail, date: String) = viewModelScope.launch {
        val newId = (date + data.id).toLong()
        val entity = MealPlannerEntity(newId, data)
        theEntity.value = entity
        repository.local.savePlannedMeal(entity)
    }

    fun deleteMeal(entity: MealPlannerEntity) = viewModelScope.launch {
        repository.local.deletePlannedMeal(entity)
    }


    fun readMealsOfEachDay(day: String) = viewModelScope.launch(IO) {
        when (day) {
            "sunday" -> {
                repository.local.loadPlannedMeals(dateStringList[0]).collect { mealsList ->
                    _mealsForSundayList.postValue(mealsList)
                }
            }

            "monday" -> {
                repository.local.loadPlannedMeals(dateStringList[1]).collect { mealsList ->
                    _mealsForMondayList.postValue(mealsList)
                }
            }

            "tuesday" -> {
                repository.local.loadPlannedMeals(dateStringList[2]).collect { mealsList ->
                    _mealsForTuesdayList.postValue(mealsList)
                }
            }

            "wednesday" -> {
                repository.local.loadPlannedMeals(dateStringList[3]).collect { mealsList ->
                    _mealsForWednesdayList.postValue(mealsList)
                }
            }

            "thursday" -> {
                repository.local.loadPlannedMeals(dateStringList[4]).collect { mealsList ->
                    _mealsForThursdayList.postValue(mealsList)
                }
            }

            "friday" -> {
                repository.local.loadPlannedMeals(dateStringList[5]).collect { mealsList ->
                    _mealsForFridayList.postValue(mealsList)
                }
            }

            "saturday" -> {
                repository.local.loadPlannedMeals(dateStringList[6]).collect { mealsList ->
                    _mealsForSaturdayList.postValue(mealsList)
                }
            }
        }
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
        theDay = Date()
        // Find the current Sunday
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        updateDatesOfWeekDays()
    }

    private fun updateDatesOfWeekDays() {
        dateList.clear()
        dateStringList.clear()
        // Add dates for the current week to the list
        calendar.time = getStartOfWeekDate(theDay)
        for (i in 0 until 7) {
            dateList.add(formatDate(calendar.time))
            dateStringList.add(formatDateWithMonthDay(calendar.time))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        setWeekTitle(Date(), theDay)
    }

    fun formatDate(date: Date): String {
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

    private fun getStartOfWeekDate(inputDate: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = inputDate
        calendar.firstDayOfWeek = Calendar.SUNDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        // Optional: If you want to set the time to midnight (00:00:00)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.time
    }

    fun moveOneWeek(direction: Int) {
        calendar.time = theDay
        calendar.add(Calendar.DAY_OF_YEAR, direction)
        calendar.firstDayOfWeek = Calendar.SUNDAY
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