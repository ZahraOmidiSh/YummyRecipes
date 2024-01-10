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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.zahra.yummyrecipes.adapter.FavoriteAdapter
import com.zahra.yummyrecipes.adapter.MealPlannerAdapter
import com.zahra.yummyrecipes.data.database.entity.MealPlannerEntity
import com.zahra.yummyrecipes.databinding.FragmentMealPlannerBinding
import com.zahra.yummyrecipes.ui.recipe.RecipeFragmentDirections
import com.zahra.yummyrecipes.utils.Constants
import com.zahra.yummyrecipes.utils.Constants.setAPIKEY
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.isVisible
import com.zahra.yummyrecipes.utils.setupRecyclerview
import com.zahra.yummyrecipes.utils.showSnackBar
import com.zahra.yummyrecipes.viewmodel.MealPlannerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    var recipeId=0


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
                loadMealDataFromApi()
            } else {
                showAddHereButtons(false)
            }



            addToSunday.setOnClickListener {

                showAddHereButtons(false)
            }

            showWeekDates()
            //forward click listener
            forward.setOnClickListener {
                viewModel.moveOneWeek(7)
                showWeekDates()
            }
            //backward click listener
            backward.setOnClickListener {
                viewModel.moveOneWeek(-7)
                showWeekDates()
            }
            //Go To Current Week
            weekTxt.setOnClickListener {
                viewModel.goToCurrentWeek()
                showWeekDates()
            }
//            loadMealsForEachDay()

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
                            //make an entity with this data
//                            val entity = MealPlannerEntity(recipeId,data)
//                            viewModel.saveMeal(entity)
//                            loadMealsForEachDay()
                            Log.e("data", data.title + data.id )
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
    private fun loadMealsForEachDay(){
        //Sunday
        viewModel.data=viewModel.dateStringList[0].toInt()

        viewModel.readPlannedMealData.observe(viewLifecycleOwner){
            mealsAdapter.setData(it)
//            initMealsRecycler()

        }
    }

    private fun initMealsRecycler() {
        binding.sundayMealsList.setupRecyclerview(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            mealsAdapter
        )

        //Click
        mealsAdapter.setonItemClickListener {
            val action = MealPlannerFragmentDirections.actionToDetail(it)
            findNavController().navigate(action)
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