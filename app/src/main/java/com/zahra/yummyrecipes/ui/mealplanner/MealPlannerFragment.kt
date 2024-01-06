package com.zahra.yummyrecipes.ui.mealplanner

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zahra.yummyrecipes.databinding.FragmentMealPlannerBinding
import com.zahra.yummyrecipes.viewmodel.MealPlannerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealPlannerFragment : Fragment() {
    //Binding
    private var _binding: FragmentMealPlannerBinding? = null
    private val binding get() = _binding!!

    //Other
    private val viewModel: MealPlannerViewModel by viewModels()

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
//            forward.setOnClickListener {
//                showNextWeek()
//            }
            //Show current week initially
            sundayDate.text = viewModel.sunday
            mondayDate.text = viewModel.monday
            tuesdayDate.text = viewModel.tuesday
            wednesdayDate.text = viewModel.wednesday
            thursdayDate.text = viewModel.thursday
            fridayDate.text = viewModel.friday
            saturdayDate.text = viewModel.saturday
        }
    }

//    private fun showCurrentWeek() {
//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
//
//        showWeek(calendar)
//    }

//    private fun showNextWeek() {
//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
//        calendar.add(Calendar.WEEK_OF_YEAR, 1)
//
//
//        showWeek(calendar)
//    }

    @SuppressLint("SetTextI18n")
//    private fun showWeek(startOfWeek: Calendar) {
//        binding.apply {
//            val dateFormat = SimpleDateFormat("d", Locale.getDefault())
//            val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
//
//            val startDate = startOfWeek.time
//            val endDate = startOfWeek.apply { add(Calendar.DAY_OF_WEEK, 6) }.time
//
//            val dateList = getDatesBetween(startDate, endDate)
////            var previousMonth = -1  //Initialize to an invalid value
//
//            //Display the dates in TextViews
//            sundayDate.text = "${formatWithSuffix(startDate)} ${monthFormat.format(startDate)}"
//            mondayDate.text = "${formatWithSuffix(dateList[1])} ${monthFormat.format(dateList[1])}"
//            tuesdayDate.text = "${formatWithSuffix(dateList[2])} ${monthFormat.format(dateList[2])}"
//            wednesdayDate.text = "${formatWithSuffix(dateList[3])} ${monthFormat.format(dateList[3])}"
//            thursdayDate.text = "${formatWithSuffix(dateList[4])} ${monthFormat.format(dateList[4])}"
//            fridayDate.text = "${formatWithSuffix(dateList[5])} ${monthFormat.format(dateList[5])}"
//            saturdayDate.text = "${formatWithSuffix(endDate)} ${monthFormat.format(endDate)}"
//        }
//
//    }

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

//    private fun getDatesBetween(startDate: Date, endDate: Date): List<Date> {
//        val dates = mutableListOf<Date>()
//        val calendar = Calendar.getInstance()
//        calendar.time = startDate
//
//        while (calendar.time.before(endDate) || calendar.time == endDate) {
//            dates.add(calendar.time)
//            calendar.add(Calendar.DAY_OF_MONTH, 1)
//        }
//
//        return dates
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}