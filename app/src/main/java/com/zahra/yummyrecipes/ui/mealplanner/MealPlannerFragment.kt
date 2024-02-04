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
import com.zahra.yummyrecipes.viewmodel.ShowAddViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
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
    private var mondayJob: Job? = null
    private var tuesdayJob: Job? = null
    private var wednesdayJob: Job? = null
    private var thursdayJob: Job? = null
    private var fridayJob: Job? = null
    private var saturdayJob: Job? = null


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
            viewModel.setIsSaved(false)
            recipeId = arguments?.getInt("recipeId", 0)!!
            showAddViewModel.showAddFlag.observe(requireActivity()) { showAddFlag ->
                if (showAddFlag == 1) {
                    Log.e("problem", recipeId.toString() )
                    loadMealDataFromApi()
                    showAddHereButtons(true)
                } else {
                    showAddHereButtons(false)
                }
            }

            //Show Current Week
            val today = viewModel.currentDate
            viewModel.setDatesOfWeek(today)
            updateDates()
            loadMealsForEachDay()

            viewModel.isSaved.observe(viewLifecycleOwner){
                if(it){
                    loadMealsForEveryDay()
                }
            }

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
                    saveMeal(0)
//                    loadMealsForSunday()
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
                    saveMeal(1)
//                    loadMealsForMonday()
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
                    saveMeal(2)
//                    loadMealsForTuesday()
                }
            }
            addToWednesday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.datesOfWeek.value!![3])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    saveMeal(3)
//                    loadMealsForWednesday()
                }
            }
            addToThursday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.datesOfWeek.value!![4])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    saveMeal(4)
//                    loadMealsForThursday()
                }
            }
            addToFriday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.datesOfWeek.value!![5])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    saveMeal(5)
//                    loadMealsForFriday()
                }
            }
            addToSaturday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.datesOfWeek.value!![6])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    saveMeal(6)
//                    loadMealsForSaturday()
                }
            }

        }
    }

    private fun loadMealsForEachDay() {
        viewModel.mealsForSundayList.observe(viewLifecycleOwner) {
            initMealsRecycler(it, 0)
            sundayJob?.cancel()
        }
        viewModel.mealsForMondayList.observe(viewLifecycleOwner) {
            initMealsRecycler(it, 1)
            mondayJob?.cancel()
        }
        viewModel.mealsForTuesdayList.observe(viewLifecycleOwner) {
            initMealsRecycler(it, 2)
            tuesdayJob?.cancel()
        }
        viewModel.mealsForWednesdayList.observe(viewLifecycleOwner) {
            initMealsRecycler(it, 3)
            wednesdayJob?.cancel()
        }
        viewModel.mealsForThursdayList.observe(viewLifecycleOwner) {
            initMealsRecycler(it, 4)
            thursdayJob?.cancel()
        }
        viewModel.mealsForFridayList.observe(viewLifecycleOwner) {
            initMealsRecycler(it, 5)
            fridayJob?.cancel()

        }
        viewModel.mealsForSaturdayList.observe(viewLifecycleOwner) {
            initMealsRecycler(it, 6)
            saturdayJob?.cancel()
        }
    }

    private fun loadMealsForEveryDay() {
        //  load meals for each day
        sundayJob = viewModel.readMealsOfEachDay(0)
        mondayJob = viewModel.readMealsOfEachDay(1)
        tuesdayJob = viewModel.readMealsOfEachDay(2)
        wednesdayJob = viewModel.readMealsOfEachDay(3)
        thursdayJob = viewModel.readMealsOfEachDay(4)
        fridayJob = viewModel.readMealsOfEachDay(5)
        saturdayJob = viewModel.readMealsOfEachDay(6)
    }

    private fun loadMealDataFromApi() {
        viewModel.callMealApi(recipeId, setAPIKEY())
    }

    private fun saveMeal(weekday: Int) {
        viewModel.mealData.value?.let {mealData ->
            mealData.data?.let {
                when (weekday) {
                    0 -> viewModel.saveMeal(it, viewModel.dateStringList[0])
                    1 -> viewModel.saveMeal(it, viewModel.dateStringList[1])
                    2 -> viewModel.saveMeal(it, viewModel.dateStringList[2])
                    3 -> viewModel.saveMeal(it, viewModel.dateStringList[3])
                    4 -> viewModel.saveMeal(it, viewModel.dateStringList[4])
                    5 -> viewModel.saveMeal(it, viewModel.dateStringList[5])
                    else -> viewModel.saveMeal(it, viewModel.dateStringList[6])
                }
//                loadMealsForEveryDay()
                showAddViewModel.setShowAddFlag(0)
            }

        }
    }



    private fun initMealsRecycler(list: List<MealPlannerEntity>, day: Int) {
        binding.apply {
            when (day) {
                //sunday
                0 -> {
                    sundayMealsAdapter.setData(list)
                    sundayMealsList.setupRecyclerview(
                        LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        ),
                        sundayMealsAdapter
                    )
                    //click
                    sundayMealsAdapter.setonItemClickListener {
                        showAddViewModel.setShowAddFlag(0)
                        val action = RecipeFragmentDirections.actionToDetail(it)
                        findNavController().navigate(action)
                    }
                    sundayMealsAdapter.setonItemClickListenerForDelete {
                        viewModel.deleteMeal(it)
                        loadMealsForEveryDay()
                    }
                }

                //monday
                1 -> {
                    mondayMealsAdapter.setData(list)
                    mondayMealsList.setupRecyclerview(
                        LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        ),
                        mondayMealsAdapter
                    )
                    //click
                    mondayMealsAdapter.setonItemClickListener {
                        showAddViewModel.setShowAddFlag(0)
                        val action = RecipeFragmentDirections.actionToDetail(it)
                        findNavController().navigate(action)
                    }
                    mondayMealsAdapter.setonItemClickListenerForDelete {
                        viewModel.deleteMeal(it)
                        loadMealsForEveryDay()
                    }
                }

                //tuesday
                2 -> {
                    tuesdayMealsAdapter.setData(list)
                    tuesdayMealsList.setupRecyclerview(
                        LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        ),
                        tuesdayMealsAdapter
                    )
                    //click
                    tuesdayMealsAdapter.setonItemClickListener {
                        showAddViewModel.setShowAddFlag(0)
                        val action = RecipeFragmentDirections.actionToDetail(it)
                        findNavController().navigate(action)
                    }
                    tuesdayMealsAdapter.setonItemClickListenerForDelete {
                        viewModel.deleteMeal(it)
                        loadMealsForEveryDay()
                    }
                }

                //wednesday
                3 -> {
                    wednesdayMealsAdapter.setData(list)
                    wednesdayMealsList.setupRecyclerview(
                        LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        ),
                        wednesdayMealsAdapter
                    )
                    //click
                    wednesdayMealsAdapter.setonItemClickListener {
                        showAddViewModel.setShowAddFlag(0)
                        val action = RecipeFragmentDirections.actionToDetail(it)
                        findNavController().navigate(action)
                    }
                    wednesdayMealsAdapter.setonItemClickListenerForDelete {
                        viewModel.deleteMeal(it)
                        loadMealsForEveryDay()
                    }
                }

                //thursday
                4 -> {
                    thursdayMealsAdapter.setData(list)
                    thursdayMealsList.setupRecyclerview(
                        LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        ),
                        thursdayMealsAdapter
                    )
                    //click
                    thursdayMealsAdapter.setonItemClickListener {
                        showAddViewModel.setShowAddFlag(0)
                        val action = RecipeFragmentDirections.actionToDetail(it)
                        findNavController().navigate(action)
                    }
                    thursdayMealsAdapter.setonItemClickListenerForDelete {
                        viewModel.deleteMeal(it)
                        loadMealsForEveryDay()
                    }
                }

                //friday
                5 -> {
                    fridayMealsAdapter.setData(list)
                    fridayMealsList.setupRecyclerview(
                        LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        ),
                        fridayMealsAdapter
                    )
                    //click
                    fridayMealsAdapter.setonItemClickListener {
                        showAddViewModel.setShowAddFlag(0)
                        val action = RecipeFragmentDirections.actionToDetail(it)
                        findNavController().navigate(action)
                    }
                    fridayMealsAdapter.setonItemClickListenerForDelete {
                        viewModel.deleteMeal(it)
                        loadMealsForEveryDay()
                    }
                }

                //saturday
                6 -> {
                    saturdayMealsAdapter.setData(list)
                    saturdayMealsList.setupRecyclerview(
                        LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        ),
                        saturdayMealsAdapter
                    )
                    //click
                    saturdayMealsAdapter.setonItemClickListener {
                        showAddViewModel.setShowAddFlag(0)
                        val action = RecipeFragmentDirections.actionToDetail(it)
                        findNavController().navigate(action)
                    }
                    saturdayMealsAdapter.setonItemClickListenerForDelete {
                        viewModel.deleteMeal(it)
                        loadMealsForEveryDay()
                    }
                }
            }
        }
    }

    private fun updateDates() {
        viewModel.datesOfWeek.observe(viewLifecycleOwner) {
            viewModel.updateDateList(it)
            viewModel.updateDateStringList(it)
            viewModel.setWeekTitle()
            binding.weekTxt.text = viewModel.weekText.value
            showWeekDates()
            loadMealsForEveryDay()
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

    private fun isItToday(dayOfWeek: TextView, dateOfWeek: TextView, today: String, bg: View) {
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