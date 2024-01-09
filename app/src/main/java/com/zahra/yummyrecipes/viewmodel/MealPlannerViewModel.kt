package com.zahra.yummyrecipes.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zahra.yummyrecipes.data.repository.MealRepository
import com.zahra.yummyrecipes.utils.Constants
import com.zahra.yummyrecipes.viewmodel.MealPlannerViewModel.StoreKeys.RECIPE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MealPlannerViewModel @Inject constructor(
    repository: MealRepository,
//    private val sharedPreferences: SharedPreferences
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    val data = 20230806
    val readPlannedMealData = repository.local.loadPlannedMeals(data).asLiveData()


    object StoreKeys {
        val RECIPE_ID = intPreferencesKey("recipeId")
    }

    //saving recipeID
    fun saveIntToDataStore(recipeId: Int) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[StoreKeys.RECIPE_ID] = recipeId

            }
        }
    }
//    fun saveToSharedPreferences(recipeId: Int) {
//        sharedPreferences.edit().putInt("recipeId", recipeId).apply()
//    }

    //getting a value from shared preferences

    val readIntFromDataStore: Flow<Int> = dataStore.data.map { preferences ->
        preferences[StoreKeys.RECIPE_ID] ?: 0
    }
//    fun getFromSharedPreferences(): Int {
//        return sharedPreferences.getInt("recipeId", 0)
//    }


    private val calendar: Calendar = Calendar.getInstance()

    //setWeek calendar
    private fun makeAnInstanceOfCalendar() {
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
    }

    val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())


    private var startDateOfWeek: Date = calendar.time
    private var currentWeekStartDate: Date = calendar.time

    private var endDateOfWeek = calendar.apply { add(Calendar.DAY_OF_WEEK, 6) }.time
    private val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
    var sunday = ""
    var monday = ""
    var tuesday = ""
    var wednesday = ""
    var thursday = ""
    var friday = ""
    var saturday = ""


    fun setWeekTitle(): String {
        val startDate =
            "${formatWithSuffix(startDateOfWeek)} ${monthFormat.format(startDateOfWeek)}"
        val endDate = "${formatWithSuffix(endDateOfWeek)} ${monthFormat.format(endDateOfWeek)}"
        val differenceInMilliseconds = currentWeekStartDate.time - startDateOfWeek.time
        val differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMilliseconds)
        var weekText = ""

        if (differenceInDays.toInt() == 0) {
            weekText = "THIS WEEK"
        } else if (differenceInDays.toInt() == 7) {
            weekText = "LAST WEEK"
        } else if (differenceInDays > 7) {
            weekText = "$startDate - $endDate"
        } else if (differenceInDays.toInt() == -7) {
            weekText = "NEXT WEEK"
        } else if (differenceInDays < -7) {
            weekText = "$startDate - $endDate"
        }
        return weekText
    }

    fun forwardWeek() {
        calendar.time = startDateOfWeek
        calendar.add(Calendar.DAY_OF_MONTH, 7)
        startDateOfWeek = calendar.time
        endDateOfWeek = calendar.apply { add(Calendar.DAY_OF_WEEK, 6) }.time
    }

    fun backwardWeek() {
        calendar.time = startDateOfWeek
        calendar.add(Calendar.DAY_OF_MONTH, -7)
        startDateOfWeek = calendar.time
        endDateOfWeek = calendar.apply { add(Calendar.DAY_OF_WEEK, 6) }.time
    }

    fun goToCurrentWeek() {
        calendar.time = currentWeekStartDate
        startDateOfWeek = calendar.time
        endDateOfWeek = calendar.apply { add(Calendar.DAY_OF_WEEK, 6) }.time
        updateDatesOfWeekDays()
    }

    fun updateDatesOfWeekDays() {
        Log.e("startDateOfWeek", dateFormat.format(startDateOfWeek))
        var dateList = getDatesBetween(startDateOfWeek, endDateOfWeek)
        sunday = "${formatWithSuffix(startDateOfWeek)} ${monthFormat.format(startDateOfWeek)}"
        monday = "${formatWithSuffix(dateList[1])} ${monthFormat.format(dateList[1])}"
        tuesday = "${formatWithSuffix(dateList[2])} ${monthFormat.format(dateList[2])}"
        wednesday = "${formatWithSuffix(dateList[3])} ${monthFormat.format(dateList[3])}"
        thursday = "${formatWithSuffix(dateList[4])} ${monthFormat.format(dateList[4])}"
        friday = "${formatWithSuffix(dateList[5])} ${monthFormat.format(dateList[5])}"
        saturday = "${formatWithSuffix(endDateOfWeek)} ${monthFormat.format(endDateOfWeek)}"
    }

    private fun formatWithSuffix(date: Date): String {
        val dayFormatter = SimpleDateFormat("d", Locale.getDefault())
        val day = dayFormatter.format(date).toInt()

        return when {
            day in 11..13 -> "${day}th"
            day % 10 == 1 -> "${day}st"
            day % 10 == 2 -> "${day}nd"
            day % 10 == 3 -> "${day}rd"
            else -> "${day}th"
        }
    }

    private fun getDatesBetween(startDate: Date, endDate: Date): List<Date> {
        val dates = mutableListOf<Date>()
        makeAnInstanceOfCalendar()
        calendar.time = startDate

        while (calendar.time.before(endDate) || calendar.time == endDate) {
            dates.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return dates
    }


}