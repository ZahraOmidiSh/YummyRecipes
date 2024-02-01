package com.zahra.yummyrecipes.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
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

    var data = MutableLiveData<ResponseDetail>()

    //save
    fun saveMeal(data: ResponseDetail, date: String) = viewModelScope.launch {
        val newId = (date + data.id).toLong()
        val entity = MealPlannerEntity(newId, data)
        repository.local.savePlannedMeal(entity)
    }

    fun deleteMeal(entity: MealPlannerEntity) = viewModelScope.launch {
//        theEntity.value=entity
        repository.local.deletePlannedMeal(entity)
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
            0 -> weekText.value ="THIS WEEK"
            -7 ->  weekText.value ="LAST WEEK"
            7 ->  weekText.value ="NEXT WEEK"
            else ->  weekText.value ="${dateList[0]} - ${dateList[6]}"
        }
    }

    var meals = emptyList<MealPlannerEntity>()

    var recipeId = MutableLiveData<Int>()

    fun goToCurrentWeek() {
        val calendar = Calendar.getInstance()
        calendar.time = today
        currentDate = calendar.time
        setDatesOfWeek(currentDate)
    }

    fun isTheDatePassed(date: Date): Boolean {
        return date < today
    }
}