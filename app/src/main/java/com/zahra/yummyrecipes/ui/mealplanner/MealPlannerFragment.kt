package com.zahra.yummyrecipes.ui.mealplanner

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
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
import com.zahra.yummyrecipes.viewmodel.SearchViewModel
import com.zahra.yummyrecipes.viewmodel.ShowAddViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import java.util.Calendar
import java.util.Date
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
    private lateinit var showAddViewModel: ShowAddViewModel
    private val viewModel: MealPlannerViewModel by viewModels()
    var recipeId = 0
    private var sundayJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showAddViewModel = ViewModelProvider(requireActivity())[ShowAddViewModel::class.java]
    }

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
            showAddViewModel.showAddFlag.observe(requireActivity()) { showAddFlag ->
                if (showAddFlag == 1) {
                    loadMealDataFromApi()
                    showAddHereButtons(true)
                } else {
                    showAddHereButtons(false)
                }
            }

            //Show Current Week
            val today = Date()
            viewModel.setDatesOfWeek(today)
            updateDates()

            //forward click listener
            forward.setOnClickListener {
                viewModel.moveWeek(7)
            }

            //backward click listener
            backward.setOnClickListener {
                viewModel.moveWeek(-7)
            }

            //current week
            weekTxt.setOnClickListener {
                viewModel.goToCurrentWeek()
            }


            //clickListeners
            addToSunday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.datesOfWeek.value!![0])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    saveMeal(1)
                }

            }
            addToMonday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.datesOfWeek.value!![1])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    saveMeal(2)
                }
            }
            addToTuesday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.datesOfWeek.value!![2])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    saveMeal(3)                }
            }
            addToWednesday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.datesOfWeek.value!![3])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    saveMeal(4)                }
            }
            addToThursday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.datesOfWeek.value!![4])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    saveMeal(5)                }
            }
            addToFriday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.datesOfWeek.value!![5])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    saveMeal(6)                }
            }
            addToSaturday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.datesOfWeek.value!![6])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    saveMeal(7)                }
            }

        }
    }


    private fun loadMealDataFromApi() {
        viewModel.callMealApi(recipeId, setAPIKEY())
        binding.apply {
            viewModel.mealData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                    }

                    is NetworkRequest.Success -> {
                        response.data?.let { data ->
                            viewModel.data.postValue(data)
                        }
                    }

                    is NetworkRequest.Error -> {
                        binding.root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }

    private fun saveMeal(weekday: Int) {
        viewModel.data.value?.let {
            when (weekday) {
                1 -> viewModel.saveMeal(it, viewModel.dateStringList[0])
                2 -> viewModel.saveMeal(it, viewModel.dateStringList[1])
                3 -> viewModel.saveMeal(it, viewModel.dateStringList[2])
                4 -> viewModel.saveMeal(it, viewModel.dateStringList[3])
                5 -> viewModel.saveMeal(it, viewModel.dateStringList[4])
                6 -> viewModel.saveMeal(it, viewModel.dateStringList[5])
                else -> viewModel.saveMeal(it, viewModel.dateStringList[6])
            }
        }
    }

    private fun loadMealsForSunday(){
        sundayJob = viewModel.readMealsOfEachDay(0)
        viewModel.mealsForEachDayList.observe(viewLifecycleOwner){
            initMealsRecycler(it, 0)
            sundayJob?.cancel()
        }

    }

    private fun updateDates() {
        viewModel.datesOfWeek.observe(viewLifecycleOwner) {
            viewModel.updateDateList(it)
            viewModel.updateDateStringList(it)
            viewModel.setWeekTitle()
            binding.weekTxt.text = viewModel.weekText.value
            showWeekDates()
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
            makeTodayBold()
        }
    }

    private fun makeTodayBold() {
        binding.apply {
            val today = viewModel.formatDate(Date())
            isItToday(sunday, sundayDate, today, sundayBg)
            isItToday(monday, mondayDate, today, mondayBg)
            isItToday(tuesday, tuesdayDate, today, tuesdayBg)
            isItToday(wednesday, wednesdayDate, today, wednesdayBg)
            isItToday(thursday, thursdayDate, today, thursdayBg)
            isItToday(friday, fridayDate, today, fridayBg)
            isItToday(saturday, saturdayDate, today, saturdayBg)

        }
    }

    private fun isItToday(
        dayOfWeek: TextView,
        dateOfWeek: TextView,
        today: String,
        bg: View
    ) {
        binding.apply {
            if (dateOfWeek.text == today) {
                dayOfWeek.textSize = 20F
                bg.isVisible = true
            } else {
                dayOfWeek.textSize = 16F
                bg.isVisible = false
            }
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