package com.zahra.yummyrecipes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zahra.yummyrecipes.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MealPlannerViewModel @Inject constructor(repository: MealRepository) : ViewModel() {
    val data = "Monday"
    val readPlannedMealData = repository.local.loadPlannedMeals(data).asLiveData()

    private val calendar: Calendar = Calendar.getInstance()

    //setWeek calendar
    private fun makeAnInstanceOfCalendar() {
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
    }

    var startDateOfWeek = calendar.time

    private var endDateOfWeek = calendar.apply { add(Calendar.DAY_OF_WEEK, 6) }.time
    private val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
    var sunday = ""
    var monday = ""
    var tuesday = ""
    var wednesday = ""
    var thursday = ""
    var friday = ""
    var saturday = ""

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

    fun updateDatesOfWeekDays() {
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