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
                    showAddHereButtons(true)
                } else {
                    showAddHereButtons(false)
                }
            }
            //clickListeners
            /*
            addToSunday.setOnClickListener {
                if (viewModel.isTheDatePassed(viewModel.dateStringList[0])) {
                    Toast.makeText(
                        requireContext(),
                        "The date is already passed!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    loadMealDataFromApi(viewModel.dateStringList[0], "sunday")
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
*/

            val calendar = Calendar.getInstance()
            calendar.time = Date()

            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            Log.e("today2", calendar.time.toString())

            calendar.firstDayOfWeek = Calendar.SUNDAY
            Log.e("today3", calendar.firstDayOfWeek.toString())

            //find the first day of week
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            val firstDayOfWeek = calendar.time
            Log.e("today4", "firstDayOfWeek: $firstDayOfWeek")

            //find the other days
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            Log.e("today5", "otherDaysOfWeek: ${calendar.time}")

            calendar.add(Calendar.DAY_OF_YEAR, 1)
            Log.e("today6", "otherDaysOfWeek: ${calendar.time}")

            calendar.add(Calendar.DAY_OF_YEAR, 1)
            Log.e("today7", "otherDaysOfWeek: ${calendar.time}")

            calendar.add(Calendar.DAY_OF_YEAR, 1)
            Log.e("today8", "otherDaysOfWeek: ${calendar.time}")

            calendar.add(Calendar.DAY_OF_YEAR, 1)
            Log.e("today9", "otherDaysOfWeek: ${calendar.time}")

            calendar.add(Calendar.DAY_OF_YEAR, 1)
            Log.e("today10", "otherDaysOfWeek: ${calendar.time}")





//            val sunday = calendar.time
//
//
//            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
//            val monday = calendar.time
//            Log.e("today5", monday.toString())
//
//            calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY)
//            val tuesday = calendar.time
//            Log.e("today6", tuesday.toString())
//
//            calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
//            val wednesday = calendar.time
//            Log.e("today7", wednesday.toString())
//
//            calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY)
//            val thursday = calendar.time
//            Log.e("today8", thursday.toString())
//
//            calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
//            val friday = calendar.time
//            Log.e("today9", friday.toString())
//
//            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
//            val saturday = calendar.time
//            Log.e("today10", saturday.toString())
//
//            Log.e("today11", calendar.time.toString())


//            showWeekDates()
//            loadMealsForEveryDay()

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
                            if (day == "sunday") {
                                viewModel.theEntity.value?.let {
                                    sundayMealsAdapter.addMealPlannerEntity(it)
                                }
                            }
                            if (day == "monday") {
                                viewModel.theEntity.value?.let {
                                    mondayMealsAdapter.addMealPlannerEntity(it)
                                }
                            }
                            if (day == "tuesday") {
                                viewModel.theEntity.value?.let {
                                    tuesdayMealsAdapter.addMealPlannerEntity(it)
                                }
                            }
                            if (day == "wednesday") {
                                viewModel.theEntity.value?.let {
                                    wednesdayMealsAdapter.addMealPlannerEntity(it)
                                }
                            }
                            if (day == "thursday") {
                                viewModel.theEntity.value?.let {
                                    thursdayMealsAdapter.addMealPlannerEntity(it)
                                }
                            }
                            if (day == "friday") {
                                viewModel.theEntity.value?.let {
                                    fridayMealsAdapter.addMealPlannerEntity(it)
                                }
                            }
                            if (day == "saturday") {
                                viewModel.theEntity.value?.let {
                                    saturdayMealsAdapter.addMealPlannerEntity(it)
                                }
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        binding.root.showSnackBar(response.message!!)
                    }
                }
            }
        }
        loadMealsForEveryDay()
        showAddViewModel.setShowAddFlag(0)
    }


    //Load Meals for each day
    private fun loadMealsForEachDay(day: String) {
        viewModel.readMealsOfEachDay(day).observe(viewLifecycleOwner) {
            initMealsRecycler(it, day)
        }
    }

    private fun loadMealsForEveryDay() {
        //  load meals for each day
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
                    sundayMealsAdapter.removeMealPlannerEntity(it)
                    loadMealsForEveryDay()
                }

            }
            if (day == "monday") {
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
                    mondayMealsAdapter.removeMealPlannerEntity(it)
                    loadMealsForEveryDay()
                }
            }
            if (day == "tuesday") {
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
                    tuesdayMealsAdapter.removeMealPlannerEntity(it)
                    loadMealsForEveryDay()
                }
            }
            if (day == "wednesday") {
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
                    wednesdayMealsAdapter.removeMealPlannerEntity(it)
                    loadMealsForEveryDay()
                }
            }
            if (day == "thursday") {
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
                    thursdayMealsAdapter.removeMealPlannerEntity(it)
                    loadMealsForEveryDay()
                }
            }
            if (day == "friday") {
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
                    fridayMealsAdapter.removeMealPlannerEntity(it)
                    loadMealsForEveryDay()
                }
            }
            if (day == "saturday") {
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
                    saturdayMealsAdapter.removeMealPlannerEntity(it)
                    loadMealsForEveryDay()
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
            makeTodayBold()
            weekTxt.text = viewModel.weekText
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