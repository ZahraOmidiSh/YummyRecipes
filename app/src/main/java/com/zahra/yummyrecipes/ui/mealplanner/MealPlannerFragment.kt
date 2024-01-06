package com.zahra.yummyrecipes.ui.mealplanner

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zahra.yummyrecipes.databinding.FragmentMealPlannerBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MealPlannerFragment : Fragment() {
    //Binding
    private var _binding: FragmentMealPlannerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealPlannerBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //forward click listener
            forward.setOnClickListener {
                showNextWeek()
            }
            //Show current week initially
            showCurrentWeek()
        }
    }

    private fun showCurrentWeek() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)

        showWeek(calendar)
    }

    private fun showNextWeek() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        calendar.add(Calendar.WEEK_OF_YEAR, 1)

        showWeek(calendar)
    }

    @SuppressLint("SetTextI18n")
    private fun showWeek(startOfWeek: Calendar) {
        binding.apply {
            val dateFormat = SimpleDateFormat("d", Locale.getDefault())
            val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())

            val startDate = startOfWeek.time
            val endDate = startOfWeek.apply { add(Calendar.DAY_OF_WEEK, 6) }.time

            val dateList = getDatesBetween(startDate, endDate)

            //Display the dates in TextViews
            sundayDate.text = "${formatWithSuffix(startDate)} ${monthFormat.format(startDate)}"
            mondayDate.text = "${formatWithSuffix(dateList[1])} ${monthFormat.format(startDate)}"
            tuesdayDate.text = "${formatWithSuffix(dateList[2])} ${monthFormat.format(startDate)}"
            wednesdayDate.text = "${formatWithSuffix(dateList[3])} ${monthFormat.format(startDate)}"
            thursdayDate.text = "${formatWithSuffix(dateList[4])} ${monthFormat.format(startDate)}"
            fridayDate.text = "${formatWithSuffix(dateList[5])} ${monthFormat.format(startDate)}"
            saturdayDate.text = "${formatWithSuffix(endDate)} ${monthFormat.format(startDate)}"
        }

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
        val calendar = Calendar.getInstance()
        calendar.time = startDate

        while (calendar.time.before(endDate) || calendar.time == endDate) {
            dates.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return dates
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}