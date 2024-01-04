package com.zahra.yummyrecipes.ui.mealplanner

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zahra.yummyrecipes.databinding.FragmentMealPlannerBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
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
            calendar.set(Calendar.DAY_OF_WEEK,calendar.firstDayOfWeek)
            val dateFormat = SimpleDateFormat("MM-dd", Locale.getDefault())
            val startDate = dateFormat.format(calendar.time)

            calendar.add(Calendar.DAY_OF_WEEK,6)

            val endDate = dateFormat.format(calendar.time)

            //Display the dates in TextViews
            monday.text="monday:$startDate"
            sunday.text="monday:$endDate"


        }
    }

    private fun getDatesBetween(startDate:Calendar,endDate:Calendar):List<Calendar>{
        val dates = mutableListOf<Calendar>()
        val calendar =startDate.clone() as Calendar

        while (calendar.before(endDate) || calendar == endDate){
            dates.add(calendar.clone() as Calendar)
            calendar.add(Calendar.DAY_OF_WEEK , 1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}