package com.zahra.yummyrecipes.viewmodel

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zahra.yummyrecipes.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MealPlannerViewModel @Inject constructor(
    repository: MealRepository,
) : ViewModel() {
    val data = 20230806
    val readPlannedMealData = repository.local.loadPlannedMeals(data).asLiveData()

    //Get The current date
    private val today = Date()

    //Create a Calendar instance
    private val calendar = Calendar.getInstance()
    private val dateList = mutableListOf<String>()

    init {
        // Set the calendar to the current date
        calendar.time = today
        // Find the current Sunday
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    }

    // Function to get a list of dates for the current week (Sunday to Saturday)
    fun getDatesForCurrentWeek(){
        // Add dates for the current week to the list
        for (i in 0 until 7) {
            dateList.add(formatDateWithDayOfWeek(calendar.time))
            calendar.add(Calendar.DAY_OF_WEEK, 1)
        }
        updateDatesOfWeekDays()
    }

    // Function to format a date with the day of the week
    private fun formatDateWithDayOfWeek(date: Date): String {
        val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())
        return dateFormat.format(date)
    }

    var sunday =""
    var monday =""
    var tuesday =""
    var wednesday=""
    var thursday=""
    var friday =""
    var saturday=""



    //    fun setWeekTitle(): String {
//        val startDate =
//            "${formatWithSuffix(startDateOfWeek)} ${monthFormat.format(startDateOfWeek)}"
//        val endDate = "${formatWithSuffix(endDateOfWeek)} ${monthFormat.format(endDateOfWeek)}"
//        val differenceInMilliseconds = currentWeekStartDate.time - startDateOfWeek.time
//        val differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMilliseconds)
//        var weekText = ""
//
//        if (differenceInDays.toInt() == 0) {
//            weekText = "THIS WEEK"
//        } else if (differenceInDays.toInt() == 7) {
//            weekText = "LAST WEEK"
//        } else if (differenceInDays > 7) {
//            weekText = "$startDate - $endDate"
//        } else if (differenceInDays.toInt() == -7) {
//            weekText = "NEXT WEEK"
//        } else if (differenceInDays < -7) {
//            weekText = "$startDate - $endDate"
//        }
//        return weekText
//    }
//
//    fun forwardWeek() {
//        calendar.time = startDateOfWeek
//        calendar.add(Calendar.DAY_OF_MONTH, 7)
//        startDateOfWeek = calendar.time
//        endDateOfWeek = calendar.apply { add(Calendar.DAY_OF_WEEK, 6) }.time
//        updateDatesOfWeekDays()
//    }
//
//    fun backwardWeek() {
//        calendar.time = startDateOfWeek
//        calendar.add(Calendar.DAY_OF_MONTH, -7)
//        startDateOfWeek = calendar.time
//        endDateOfWeek = calendar.apply { add(Calendar.DAY_OF_WEEK, 6) }.time
//        updateDatesOfWeekDays()
//    }
//
//    fun goToCurrentWeek() {
//        calendar.time = currentWeekStartDate
//        startDateOfWeek = calendar.time
//        endDateOfWeek = calendar.apply { add(Calendar.DAY_OF_WEEK, 6) }.time
//        updateDatesOfWeekDays()
//    }
//
    fun updateDatesOfWeekDays() {
     sunday = dateList[0]
     monday = dateList[1]
     tuesday = dateList[2]
     wednesday = dateList[3]
     thursday = dateList[4]
     friday = dateList[5]
     saturday = dateList[6]

    }
//
//
//    private fun formatWithSuffix(date: Date): String {
//        val dayFormatter = SimpleDateFormat("d", Locale.getDefault())
//        val day = dayFormatter.format(date).toInt()
//
//        return when {
//            day in 11..13 -> "${day}th"
//            day % 10 == 1 -> "${day}st"
//            day % 10 == 2 -> "${day}nd"
//            day % 10 == 3 -> "${day}rd"
//            else -> "${day}th"
//        }
//    }
//
//    private fun getDatesBetween(startDate: Date, endDate: Date): List<Date> {
//        val dates = mutableListOf<Date>()
//        makeAnInstanceOfCalendar()
//        calendar.time = startDate
//
//        while (calendar.time.before(endDate) || calendar.time == endDate) {
//            dates.add(calendar.time)
//            calendar.add(Calendar.DAY_OF_MONTH, 1)
//        }
//
//        Log.e("dates", dates.toString())
//
//        return dates
//    }


}