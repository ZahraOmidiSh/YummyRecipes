package com.zahra.yummyrecipes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.yummyrecipes.data.database.entity.MealPlannerEntity
import com.zahra.yummyrecipes.data.repository.MealRepository
import com.zahra.yummyrecipes.models.detail.ResponseDetail
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
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

    var isSaved = MutableLiveData<Boolean>()
    fun setIsSaved(saved: Boolean) {
        isSaved.postValue(saved)
    }

    //save
    fun saveMeal(data: ResponseDetail, date: String) = viewModelScope.launch {
        val newId = (date + data.id).toLong()
        val entity = MealPlannerEntity(newId, data)
        repository.local.savePlannedMeal(entity)
        setIsSaved(true)
    }

    fun deleteMeal(entity: MealPlannerEntity) = viewModelScope.launch {
        repository.local.deletePlannedMeal(entity)
    }

    //livedata for meals for dayList
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

    fun readMealsOfEachDay(day: Int) = viewModelScope.launch(IO) {
        repository.local.loadPlannedMeals(dateStringList[day]).collect { mealsList ->
            when (day) {
                0 -> _mealsForSundayList.postValue(mealsList)
                1 -> _mealsForMondayList.postValue(mealsList)
                2 -> _mealsForTuesdayList.postValue(mealsList)
                3 -> _mealsForWednesdayList.postValue(mealsList)
                4 -> _mealsForThursdayList.postValue(mealsList)
                5 -> _mealsForFridayList.postValue(mealsList)
                6 -> _mealsForSaturdayList.postValue(mealsList)
            }
        }
    }

    //format dates
    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())
        return dateFormat.format(date)
    }

    private fun formatDateWithMonthDay(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return dateFormat.format(date)
    }

    //Dates Of Week
    var datesOfWeek = MutableLiveData<List<Date>>()
    fun setDatesOfWeek(day: Date) {
        //make a list od dates
        val dates = mutableListOf<Date>()
        //make an instance of Calendar
        val calendar = Calendar.getInstance()
        calendar.time = day
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        //set first day of week
        calendar.firstDayOfWeek = Calendar.SUNDAY

        for (i in 1..7) {
            calendar.set(Calendar.DAY_OF_WEEK, i)
            dates.add(calendar.time)
        }

        datesOfWeek.postValue(dates)
    }

    //datesList in string form
    val dateList = mutableListOf<String>()
    fun updateDateList(datesOfWeek: List<Date>) {
        dateList.clear()
        datesOfWeek.forEach {
            dateList.add(formatDate(it))
        }
    }

    val dateStringList = mutableListOf<String>()
    fun updateDateStringList(datesOfWeek: List<Date>) {
        dateStringList.clear()
        datesOfWeek.forEach {
            dateStringList.add(formatDateWithMonthDay(it))
        }
    }

    //move week
    var currentDate = Date()
    fun moveWeek(direction: Int) {
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.DAY_OF_YEAR, direction)
        currentDate = calendar.time
        setDatesOfWeek(currentDate)
    }

    //set week title
    val today = Date()
    var weekText = MutableLiveData<String>()
    fun setWeekTitle() {
        val differenceInMillis = currentDate.time - today.time
        when ((differenceInMillis / (24 * 60 * 60 * 1000)).toInt()) {
            0 -> weekText.value = "THIS WEEK"
            -7 -> weekText.value = "LAST WEEK"
            7 -> weekText.value = "NEXT WEEK"
            else -> weekText.value = "${dateList[0]} - ${dateList[6]}"
        }
    }

    fun goToCurrentWeek() {
        val calendar = Calendar.getInstance()
        calendar.time = today
        currentDate = calendar.time
        setDatesOfWeek(currentDate)
    }

    fun isTheDatePassed(date: Date): Boolean {
        return formatDateWithMonthDay(date) < formatDateWithMonthDay(today)
    }
}