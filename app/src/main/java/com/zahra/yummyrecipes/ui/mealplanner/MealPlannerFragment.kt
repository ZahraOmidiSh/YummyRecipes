package com.zahra.yummyrecipes.ui.mealplanner

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zahra.yummyrecipes.adapter.MealPlannerAdapter
import com.zahra.yummyrecipes.data.database.entity.MealPlannerEntity
import com.zahra.yummyrecipes.databinding.FragmentMealPlannerBinding
import com.zahra.yummyrecipes.ui.recipe.RecipeFragmentDirections
import com.zahra.yummyrecipes.utils.Constants.setAPIKEY
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.setupRecyclerview
import com.zahra.yummyrecipes.utils.showSnackBar
import com.zahra.yummyrecipes.viewmodel.MealPlannerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MealPlannerFragment : Fragment() {
    //Binding
    private var _binding: FragmentMealPlannerBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sundayMealsAdapter: MealPlannerAdapter

    @Inject
    lateinit var mondayMealsAdapter: MealPlannerAdapter

    @Inject
    lateinit var tuesdayMealsAdapter: MealPlannerAdapter

    @Inject
    lateinit var wednesdayMealsAdapter: MealPlannerAdapter

    @Inject
    lateinit var thursdayMealsAdapter: MealPlannerAdapter

    @Inject
    lateinit var fridayMealsAdapter: MealPlannerAdapter

    @Inject
    lateinit var saturdayMealsAdapter: MealPlannerAdapter

    //Other
    private val viewModel: MealPlannerViewModel by viewModels()
    var recipeId = 0


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
            viewModel.updateDatesOfWeekDays()
            loadMealsForEveryDay()

            addToSunday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.dateStringList[0])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("test","yes" )
                } else {
                    loadMealDataFromApi(viewModel.dateStringList[0], "sunday")
                    Log.e("test","no")
                }

            }
            addToMonday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.dateStringList[1])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    loadMealDataFromApi(viewModel.dateStringList[1], "monday")
                }
            }
            addToTuesday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.dateStringList[2])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    loadMealDataFromApi(viewModel.dateStringList[2], "tuesday")
                }
            }
            addToWednesday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.dateStringList[3])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    loadMealDataFromApi(viewModel.dateStringList[3], "wednesday")
                }
            }
            addToThursday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.dateStringList[4])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    loadMealDataFromApi(viewModel.dateStringList[4], "thursday")
                }
            }
            addToFriday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.dateStringList[5])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    loadMealDataFromApi(viewModel.dateStringList[5], "friday")
                }
            }
            addToSaturday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.dateStringList[6])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    loadMealDataFromApi(viewModel.dateStringList[6], "saturday")
                }
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

            if (day == "sunday") {
                sundayMealsAdapter.setData(list)
                sundayMealsList.setupRecyclerview(
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                    sundayMealsAdapter
                )
                //click
                sundayMealsAdapter.setonItemClickListener {
                    val action = RecipeFragmentDirections.actionToDetail(it)
                    findNavController().navigate(action)
                }

                sundayMealsAdapter.setonItemClickListenerForDelete {
                    viewModel.deleteMeal(it)
                }

            }
            if (day == "monday") {
                mondayMealsAdapter.setData(list)
                mondayMealsList.setupRecyclerview(
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                    mondayMealsAdapter
                )
                //click
                mondayMealsAdapter.setonItemClickListener {
                    val action = RecipeFragmentDirections.actionToDetail(it)
                    findNavController().navigate(action)
                }
                mondayMealsAdapter.setonItemClickListenerForDelete {
                    viewModel.deleteMeal(it)
                }
            }
            if (day == "tuesday") {
                tuesdayMealsAdapter.setData(list)
                tuesdayMealsList.setupRecyclerview(
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                    tuesdayMealsAdapter
                )
                //click
                tuesdayMealsAdapter.setonItemClickListener {
                    val action = RecipeFragmentDirections.actionToDetail(it)
                    findNavController().navigate(action)
                }
                tuesdayMealsAdapter.setonItemClickListenerForDelete {
                    viewModel.deleteMeal(it)
                }
            }
            if (day == "wednesday") {
                wednesdayMealsAdapter.setData(list)
                wednesdayMealsList.setupRecyclerview(
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                    wednesdayMealsAdapter
                )
                //click
                wednesdayMealsAdapter.setonItemClickListener {
                    val action = RecipeFragmentDirections.actionToDetail(it)
                    findNavController().navigate(action)
                }
                wednesdayMealsAdapter.setonItemClickListenerForDelete {
                    viewModel.deleteMeal(it)
                }
            }
            if (day == "thursday") {
                thursdayMealsAdapter.setData(list)
                thursdayMealsList.setupRecyclerview(
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                    thursdayMealsAdapter
                )
                //click
                thursdayMealsAdapter.setonItemClickListener {
                    val action = RecipeFragmentDirections.actionToDetail(it)
                    findNavController().navigate(action)
                }
                thursdayMealsAdapter.setonItemClickListenerForDelete {
                    viewModel.deleteMeal(it)
                }
            }
            if (day == "friday") {
                fridayMealsAdapter.setData(list)
                fridayMealsList.setupRecyclerview(
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                    fridayMealsAdapter
                )
                //click
                fridayMealsAdapter.setonItemClickListener {
                    val action = RecipeFragmentDirections.actionToDetail(it)
                    findNavController().navigate(action)
                }
                fridayMealsAdapter.setonItemClickListenerForDelete {
                    viewModel.deleteMeal(it)
                }
            }
            if (day == "saturday") {
                saturdayMealsAdapter.setData(list)
                saturdayMealsList.setupRecyclerview(
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                    saturdayMealsAdapter
                )
                //click
                saturdayMealsAdapter.setonItemClickListener {
                    val action = RecipeFragmentDirections.actionToDetail(it)
                    findNavController().navigate(action)
                }
                saturdayMealsAdapter.setonItemClickListenerForDelete {
                    viewModel.deleteMeal(it)
                }
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