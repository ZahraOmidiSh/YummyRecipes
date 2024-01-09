package com.zahra.yummyrecipes.ui.mealplanner

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.navigation.fragment.navArgs
import com.zahra.yummyrecipes.databinding.FragmentMealPlannerBinding
import com.zahra.yummyrecipes.viewmodel.MealPlannerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        val recipeId = arguments?.getInt("recipeId", 0)
        Log.e("recipeId", recipeId.toString())

        //InitViews
        binding.apply {
            //forward click listener
            forward.setOnClickListener {
                viewModel.forwardWeek()
                viewModel.updateDatesOfWeekDays()
                showWeekDates()
            }
            //backward click listener
            backward.setOnClickListener {
                viewModel.backwardWeek()
                viewModel.updateDatesOfWeekDays()
                showWeekDates()
            }
            //Go To Current Week
            weekTxt.setOnClickListener {
                viewModel.goToCurrentWeek()
//                viewModel.updateDatesOfWeekDays()
                showWeekDates()
            }
            //Show current week initially
            viewModel.updateDatesOfWeekDays()
            showWeekDates()
        }
    }

    private fun showWeekDates() {
        binding.apply {
            sundayDate.text = viewModel.sunday
            mondayDate.text = viewModel.monday
            tuesdayDate.text = viewModel.tuesday
            wednesdayDate.text = viewModel.wednesday
            thursdayDate.text = viewModel.thursday
            fridayDate.text = viewModel.friday
            saturdayDate.text = viewModel.saturday
            weekTxt.text = viewModel.setWeekTitle()
        }

    }

    private fun showAddHereButtons(visibility: Boolean) {
        binding.apply {
            addToSunday.isVisible = visibility
            addToMonday.isVisible = visibility
            addToTuesday.isVisible = visibility
            addToWednesday.isVisible = visibility
            addToThursday.isVisible = visibility
            addToFriday.isVisible = visibility
            addToSaturday.isVisible = visibility

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}