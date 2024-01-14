package com.zahra.yummyrecipes.ui.mealplanner

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.zahra.yummyrecipes.adapter.MealPlannerAdapter
import com.zahra.yummyrecipes.data.database.entity.MealPlannerEntity
import com.zahra.yummyrecipes.databinding.FragmentMealPlannerBinding
import com.zahra.yummyrecipes.utils.Constants.setAPIKEY
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.setupRecyclerview
import com.zahra.yummyrecipes.utils.showSnackBar
import com.zahra.yummyrecipes.viewmodel.MealPlannerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@AndroidEntryPoint
class MealPlannerFragment : Fragment() {
    //Binding
    private var _binding: FragmentMealPlannerBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var mealsAdapter: MealPlannerAdapter

    //Other
    private val viewModel: MealPlannerViewModel by viewModels()
    var recipeId = 0
//    lateinit var entity: MealPlannerEntity


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
            recipeId = arguments?.getInt("recipeId", 0)!!

            if (recipeId > 0) {
                showAddHereButtons(true)
            } else {
                showAddHereButtons(false)
            }

            loadMealsForEveryDay()

            addToSunday.setOnClickListener {
                loadMealDataFromApi(viewModel.dateStringList[0], "sunday")
            }
            addToMonday.setOnClickListener {
                loadMealDataFromApi(viewModel.dateStringList[1], "monday")
            }
            addToTuesday.setOnClickListener {
                loadMealDataFromApi(viewModel.dateStringList[2], "tuesday")
            }
            addToWednesday.setOnClickListener {
                loadMealDataFromApi(viewModel.dateStringList[3], "wednesday")
            }
            addToThursday.setOnClickListener {
                loadMealDataFromApi(viewModel.dateStringList[4], "thursday")
            }
            addToFriday.setOnClickListener {
                loadMealDataFromApi(viewModel.dateStringList[5], "friday")
            }
            addToSaturday.setOnClickListener {
                loadMealDataFromApi(viewModel.dateStringList[6], "saturday")
            }

            showWeekDates()

            //forward click listener
            forward.setOnClickListener {
                viewModel.moveOneWeek(7)
                showWeekDates()
                loadMealsForEveryDay()
            }
            //backward click listener
            backward.setOnClickListener {
                viewModel.moveOneWeek(-7)
                showWeekDates()
                loadMealsForEveryDay()
            }
            //Go To Current Week
            weekTxt.setOnClickListener {
                viewModel.goToCurrentWeek()
                showWeekDates()
                loadMealsForEveryDay()
            }

        }
    }

    private fun loadMealDataFromApi(date: String, day: String) {
        viewModel.callMealApi(recipeId, setAPIKEY())
        binding.apply {
            viewModel.mealData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                    }

                    is NetworkRequest.Success -> {
                        response.data?.let { data ->
                            viewModel.saveMeal(data, date)
                            loadMealsForEachDay(day)
                            showAddHereButtons(false)
                        }
                    }

                    is NetworkRequest.Error -> {
                        binding.root.showSnackBar(response.message!!)
                    }

                }

            }
        }
    }


    //Load Meals for each day
    private fun loadMealsForEachDay(day: String) {
        viewModel.readMealsOfEachDay(day).observe(viewLifecycleOwner) {
            initMealsRecycler(it, day)
        }
    }

    private fun loadMealsForEveryDay() {
        loadMealsForEachDay("sunday")
        loadMealsForEachDay("monday")
        loadMealsForEachDay("tuesday")
        loadMealsForEachDay("wednesday")
        loadMealsForEachDay("thursday")
        loadMealsForEachDay("friday")
        loadMealsForEachDay("saturday")
    }


    private fun initMealsRecycler(list: List<MealPlannerEntity>, day: String) {
        binding.apply {
            mealsAdapter.setData(list)
            if (day == "sunday") {
                sundayMealsList.setupRecyclerview(
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                    mealsAdapter
                )
            }
            if (day == "monday") {
                mondayMealsList.setupRecyclerview(
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                    mealsAdapter
                )
            }
            if (day == "tuesday") {
                tuesdayMealsList.setupRecyclerview(
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                    mealsAdapter
                )
            }
            if (day == "wednesday") {
                wednesdayMealsList.setupRecyclerview(
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                    mealsAdapter
                )
            }
            if (day == "thursday") {
                thursdayMealsList.setupRecyclerview(
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                    mealsAdapter
                )
            }
            if (day == "friday") {
                fridayMealsList.setupRecyclerview(
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                    mealsAdapter
                )
            }
            if (day == "saturday") {
                saturdayMealsList.setupRecyclerview(
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                    mealsAdapter
                )
            }


        }
    }

    private fun showWeekDates() {
        binding.apply {
            sundayDate.text = viewModel.dateList[0]
            mondayDate.text = viewModel.dateList[1]
            tuesdayDate.text = viewModel.dateList[2]
            wednesdayDate.text = viewModel.dateList[3]
            thursdayDate.text = viewModel.dateList[4]
            fridayDate.text = viewModel.dateList[5]
            saturdayDate.text = viewModel.dateList[6]
            weekTxt.text = viewModel.weekText
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
            chooseDayTxt.isVisible = visibility

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}