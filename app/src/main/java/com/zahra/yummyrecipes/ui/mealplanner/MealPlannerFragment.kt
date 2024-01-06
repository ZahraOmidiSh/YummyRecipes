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
            //find the current week
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            val dateFormat = SimpleDateFormat("d MMM", Locale.getDefault())
            val startDate = calendar.time
            val endDate = calendar.apply { add(Calendar.DAY_OF_WEEK, 6) }.time
            val dateList = getDatesBetween(startDate, endDate)

            //Display the dates in TextViews
            sundayDate.text = dateFormat.format(startDate)
            mondayDate.text = dateFormat.format(dateList[1])
            tuesdayDate.text = dateFormat.format(dateList[2])
            wednesdayDate.text = dateFormat.format(dateList[3])
            thursdayDate.text = dateFormat.format(dateList[4])
            fridayDate.text = dateFormat.format(dateList[5])
            saturdayDate.text = dateFormat.format(endDate)


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