package com.zahra.yummyrecipes.ui.search

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.adapter.AdvancedSearchAdapter
import com.zahra.yummyrecipes.adapter.SearchAdapter
import com.zahra.yummyrecipes.databinding.FragmentSearchBinding
import com.zahra.yummyrecipes.utils.NetworkChecker
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.setupRecyclerview
import com.zahra.yummyrecipes.utils.showSnackBar
import com.zahra.yummyrecipes.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    //Binding
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var advancedSearchAdapter: AdvancedSearchAdapter

    @Inject
    lateinit var searchAdapter: SearchAdapter

    @Inject
    lateinit var networkChecker: NetworkChecker

    //Others
    private var isThemeChanged: Boolean = false
    private lateinit var viewModel: SearchViewModel

    //    private var isNetworkAvailable by Delegates.notNull<Boolean>()
    private var isNetworkAvailable: Boolean? = null
    private var searchString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the theme change state
        outState.putBoolean("themeChanged", true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {


            if (viewModel.searchString.value == null) {
                viewModel.searchString.value = ""
            }
            searchEdt.text.clear()
            searchEdt.text.append(viewModel.searchString.value)

//            if (viewModel.searchString.value != "" && viewModel.isCloseButtonPressed.value == false) {
//                viewModel.callSearchApi(viewModel.searchQueries(viewModel.searchString.value.toString()))
//                loadRecentData()
//            }
            if (viewModel.searchString.value == "" && viewModel.isSearchWithDietsOrAllergies.value == false
                && viewModel.isSearchWithIngredient.value == false && viewModel.isSearchWithFilters.value == false) {
                simpleSearchLay.isVisible = false
                advancedSearchScroll.isVisible = true
                closeImg.isVisible = false
            }else{
                simpleSearchLay.isVisible = true
                advancedSearchScroll.isVisible = false
                closeImg.isVisible = true
            }

            viewModel.searchString.observe(viewLifecycleOwner) {
                searchString = it
            }
            viewModel.isCloseButtonPressed.value = false
            isNetworkAvailable = networkChecker.checkNetworkAvailability().value
            if (savedInstanceState != null) {
                // Check if the activity is being recreated due to a theme change
                isThemeChanged = savedInstanceState.getBoolean("themeChanged", false)
            }
            if (!isThemeChanged) {
                // This line will only execute if the activity is not recreated due to a theme change
                viewModel.expandedIngredientsList.value!!.forEach {
                    if (it.isSelected) {
                        it.isSelected = false
                    }
                }
            }
            //load data
            viewModel.expandedIngredientsList.observe(
                viewLifecycleOwner
            ) { expandedIngredients ->
                advancedSearchAdapter.setData(expandedIngredients)
            }

            //Close listener
            closeImg.setOnClickListener {
                closeSearch()
            }

            //view all ingredient button listener
            viewAllListener()

            // Set up RecyclerView
            setupRecyclerView(ingredientsList)

            //Click on items
            clickOnIngredientListListener()

            //Check Internet
            checkInternet()

            //box Search
            textChangeListener()

            //ingredient search
            ingredientSearch()

            //diets and allergies search
            dietsAndAllergiesSearch()

            //filterSearch
            filterSearch()

            //Filters Buttons Listener
            ingredientsButton.setOnClickListener {
                val direction = SearchFragmentDirections.actionToSearchAllIngredients()
                findNavController().navigate(direction)
            }
            dietsButton.setOnClickListener {
                val direction = SearchFragmentDirections.actionToDiets()
                findNavController().navigate(direction)
            }
            filtersButton.setOnClickListener {
                val direction = SearchFragmentDirections.actionToFilters()
                findNavController().navigate(direction)
            }

            //Search by Meal Listener
            breakfastMeal.setOnClickListener {
                mealClickListener("breakfast")
            }
            maincourseMeal.setOnClickListener {
                mealClickListener("main course")
            }
            dessertMeal.setOnClickListener {
                mealClickListener("dessert")
            }
            snackMeal.setOnClickListener {
                mealClickListener("snack")
            }

            //Most popular categories
            //lowCalorie
            lowCalorie.setOnClickListener {
                viewModel.isCloseButtonPressed.value = false
                viewModel._selectedCalorieData.value = listOf("0-200")
                viewModel.isSearchWithCalorie.value = true
                viewModel.isSearchWithFilters.value = true
                filterSearch()
            }

            //ovenBaked
            ovenBaked.setOnClickListener {
                viewModel.isCloseButtonPressed.value = false
                viewModel._selectedToolsData.value = listOf("oven")
                viewModel.isSearchWithTools.value = true
                viewModel.isSearchWithFilters.value = true
                filterSearch()
            }

            //Asian
            asian.setOnClickListener {
                viewModel.isCloseButtonPressed.value = false
                viewModel._selectedRegionData.value = listOf("Asian")
                viewModel.isSearchWithRegion.value = true
                viewModel.isSearchWithFilters.value = true
                filterSearch()
            }

            //Mediterranean
            mediterranean.setOnClickListener {
                viewModel.isCloseButtonPressed.value = false
                viewModel._selectedRegionData.value = listOf("Mediterranean")
                viewModel.isSearchWithRegion.value = true
                viewModel.isSearchWithFilters.value = true
                filterSearch()
            }

            //Vegetarian
            vegetarian.setOnClickListener {
                viewModel.isCloseButtonPressed.value = false
                viewModel._selectedDietsData.value = listOf("Vegetarian")
                viewModel.isSearchWithDiets.value = true
                viewModel.isSearchWithDietsOrAllergies.value = true
                dietsAndAllergiesSearch()
            }

            //QuickAndEasy
            quickAndEasy.setOnClickListener {
                viewModel.isCloseButtonPressed.value = false
                viewModel._selectedTimeData.value = listOf("15")
                viewModel.isSearchWithTime.value = true
                viewModel.isSearchWithFilters.value = true
                filterSearch()
            }
        }
    }

    private fun mealClickListener(meal: String) {
        viewModel.isCloseButtonPressed.value = false
        viewModel._selectedMealsData.value = listOf(meal)
        viewModel.isSearchWithMeals.value = true
        viewModel.isSearchWithFilters.value = true
        filterSearch()
    }

    private fun closeSearch() {
        binding.apply {
            viewModel.isCloseButtonPressed.value = true
            viewModel.searchString.value = ""
            searchEdt.text.clear()
            searchString = ""
            viewModel.expandedIngredientsList.value!!.forEach {
                if (it.isSelected) {
                    it.isSelected = false
                }
            }
            viewModel.updateSelectedIngredientsName()
            viewModel._selectedDietsData.value = emptyList()
            viewModel._selectedAllergiesData.value = emptyList()
            viewModel._selectedMealsData.value = emptyList()
            viewModel._selectedRegionData.value = emptyList()
            viewModel._selectedTimeData.value = emptyList()
            viewModel._selectedCalorieData.value = emptyList()
            viewModel._selectedToolsData.value = emptyList()
            viewModel.isSearchWithIngredient.value = false
            viewModel.isSearchWithMeals.value = false
            viewModel.isSearchWithRegion.value = false
            viewModel.isSearchWithTime.value = false
            viewModel.isSearchWithCalorie.value = false
            viewModel.isSearchWithTools.value = false
            viewModel.isSearchWithDiets.value = false
            viewModel.isSearchWithAllergies.value = false
            viewModel.isSearchWithDietsOrAllergies.value = false
            viewModel.isSearchWithFilters.value = false
            simpleSearchLay.isVisible = false
            advancedSearchScroll.isVisible = true
            closeImg.isVisible = false
        }
    }

    private fun textChangeListener() {
        binding.searchEdt.addTextChangedListener {
            binding.closeImg.isVisible = true
            if (isNetworkAvailable == true) {
                viewModel.isCloseButtonPressed.value = false
                viewModel.searchString.value = it.toString()
                viewModel.callSearchApi(viewModel.searchQueries(viewModel.searchString.value.toString()))
                loadRecentData()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun ingredientSearch() {
        binding.apply {
            viewModel.isSearchWithIngredient.observe(viewLifecycleOwner) {
                if (viewModel.isCloseButtonPressed.value == false) {
                    if (it) {
                        closeImg.isVisible = true
                        viewModel.callSearchApi(viewModel.searchQueries(searchString))
                        loadRecentData()
                        ingredientsButton.text =
                            "INGREDIENTS " + "(" + viewModel.selectedIngredientsNameData.value?.size.toString() + ")"
                        setOneButtonTextColor(ingredientsButton, R.color.white)
                        if (isDarkTheme()) {
                            setOneButtonBackgroundTint(ingredientsButton, R.color.congo_pink)
                        } else {
                            setOneButtonBackgroundTint(ingredientsButton, R.color.big_foot_feet)
                        }
                    } else {
                        setOneFilterButtonsToDefault(ingredientsButton)
                        ingredientsButton.text = "INGREDIENTS"
                        if (viewModel.searchString.value?.isNotEmpty() == true) {
                            viewModel.callSearchApi(viewModel.searchQueries(searchString))
                            loadRecentData()
                        } else {
                            if (viewModel.isSearchWithDietsOrAllergies.value == true || viewModel.isSearchWithFilters.value == true) {
                                viewModel.callSearchApi(viewModel.searchQueries(""))
                                loadRecentData()
                            }
//                            else if (viewModel.isSearchWithDietsOrAllergies.value == false && viewModel.isSearchWithFilters.value == false) {
//                                simpleSearchLay.isVisible = false
//                                advancedSearchScroll.isVisible = true
//                                closeImg.isVisible = false
//                            }
                        }
                    }
                } else {
                    setAllFilterButtonsToDefault()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun dietsAndAllergiesSearch() {
        binding.apply {
            viewModel.isSearchWithDietsOrAllergies.observe(viewLifecycleOwner) {
                if (viewModel.isCloseButtonPressed.value == false) {
                    if (it) {
                        closeImg.isVisible = true
                        simpleSearchLay.isVisible = true
                        advancedSearchScroll.isVisible = false
                        viewModel.callSearchApi(viewModel.searchQueries(searchString))
                        loadRecentData()
                        var sizeDiets = 0
                        var sizeAllergies = 0
                        if (viewModel.selectedDietsData.value?.isNotEmpty() == true) {
                            sizeDiets = viewModel.selectedDietsData.value?.size!!
                        }
                        if (viewModel._selectedAllergiesData.value?.isNotEmpty() == true) {
                            sizeAllergies = viewModel.selectedAllergiesData.value?.size!!
                        }
                        val size = sizeDiets + sizeAllergies
                        dietsButton.text = "DIETS ($size)"
                        setOneButtonTextColor(dietsButton, R.color.white)
                        if (isDarkTheme()) {
                            setOneButtonBackgroundTint(dietsButton, R.color.congo_pink)
                        } else {
                            setOneButtonBackgroundTint(dietsButton, R.color.big_foot_feet)
                        }

                    } else {
                        setOneFilterButtonsToDefault(dietsButton)
                        dietsButton.text = "DIETS"
                        if (viewModel.searchString.value?.isNotEmpty() == true) {
                            viewModel.callSearchApi(viewModel.searchQueries(searchString))
                            loadRecentData()
                        } else {
                            if (viewModel.isSearchWithIngredient.value == true || viewModel.isSearchWithFilters.value == true) {
                                viewModel.callSearchApi(viewModel.searchQueries(searchString))
                                loadRecentData()
                            }
//                            else if (viewModel.isSearchWithIngredient.value == false && viewModel.isSearchWithFilters.value == false) {
//                                simpleSearchLay.isVisible = false
//                                advancedSearchScroll.isVisible = true
//                                closeImg.isVisible = false
//                            }
                        }
                    }
                } else {
                    setAllFilterButtonsToDefault()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun filterSearch() {
        binding.apply {
            viewModel.isSearchWithFilters.observe(viewLifecycleOwner) {
                if (viewModel.isCloseButtonPressed.value == false) {
                    if (it) {
                        closeImg.isVisible = true
                        simpleSearchLay.isVisible = true
                        advancedSearchScroll.isVisible = false
                        viewModel.callSearchApi(viewModel.searchQueries(searchString))
                        loadRecentData()
                        var sizeMeal = 0
                        var sizeTime = 0
                        var sizeCalorie = 0
                        var sizeRegion = 0
                        var sizeTools = 0

                        if (viewModel.selectedMealsData.value?.isNotEmpty() == true) {
                            sizeMeal = viewModel.selectedMealsData.value?.size!!
                        }
                        if (viewModel.selectedToolsData.value?.isNotEmpty() == true) {
                            sizeTools = viewModel.selectedToolsData.value?.size!!
                        }
                        if (viewModel.selectedRegionData.value?.isNotEmpty() == true) {
                            sizeRegion = viewModel.selectedRegionData.value?.size!!
                        }
                        if (viewModel.selectedTimeData.value?.isNotEmpty() == true) {
                            sizeTime = viewModel.selectedTimeData.value?.size!!
                        }
                        if (viewModel.selectedCalorieData.value?.isNotEmpty() == true) {
                            sizeCalorie = viewModel.selectedCalorieData.value?.size!!
                        }
                        val size = sizeMeal + sizeTime + sizeRegion + sizeCalorie + sizeTools

                        filtersButton.text = "FILTERS ($size)"
                        setOneButtonTextColor(filtersButton, R.color.white)
                        if (isDarkTheme()) {
                            setOneButtonBackgroundTint(filtersButton, R.color.congo_pink)
                        } else {
                            setOneButtonBackgroundTint(filtersButton, R.color.big_foot_feet)
                        }
                    } else {
                        setOneFilterButtonsToDefault(filtersButton)
                        filtersButton.text = "FILTERS"
                        if (viewModel.searchString.value?.isNotEmpty() == true) {
                            viewModel.callSearchApi(viewModel.searchQueries(searchString))
                            loadRecentData()
                        } else {
                            if (viewModel.isSearchWithIngredient.value == true || viewModel.isSearchWithDietsOrAllergies.value == true) {
                                viewModel.callSearchApi(viewModel.searchQueries(searchString))
                                loadRecentData()
                            }
//                            else if (viewModel.isSearchWithIngredient.value == false && viewModel.isSearchWithDietsOrAllergies.value == false) {
//                                simpleSearchLay.isVisible = false
//                                advancedSearchScroll.isVisible = true
//                                closeImg.isVisible = false
//                            }
                        }
                    }
                } else {
                    setAllFilterButtonsToDefault()
                }
            }
        }
    }

    private fun checkInternet() {
        lifecycleScope.launch {
            withStarted { }
            networkChecker.checkNetworkAvailability().collect { state ->
                initInternetLayout(state)
                isNetworkAvailable = state
            }
        }
    }

    private fun clickOnIngredientListListener() {
        advancedSearchAdapter.setonItemClickListener { ingredientName ->
            viewModel.expandedIngredientsList.value?.forEach {
                it.isSelected = false
            }
            viewModel.updateExpandedIngredientByName(ingredientName, true)
            val action = SearchFragmentDirections.actionToSearchAllIngredients()
            viewModel.updateSelectedIngredientsName()
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView(ingredientsList: RecyclerView) {
        ingredientsList.setupRecyclerview(
            LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            ), advancedSearchAdapter
        )
    }

    private fun viewAllListener() {
        binding.viewAllSearchByIngredients.setOnClickListener {
            viewModel.expandedIngredientsList.value?.forEach {
                it.isSelected = false
            }
            viewModel.updateSelectedIngredientsName()
            viewModel.isSearchWithIngredient.value = false
            val direction = SearchFragmentDirections.actionToSearchAllIngredients()
            findNavController().navigate(direction)
        }
    }

    private fun loadRecentData() {
        binding.apply {
            viewModel.searchData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        advancedSearchScroll.isVisible = false
                        simpleSearchLay.isVisible = true
                        simpleSearchList.showShimmer()
                    }

                    is NetworkRequest.Success -> {
                        advancedSearchScroll.isVisible = false
                        simpleSearchLay.isVisible = true
                        simpleSearchList.hideShimmer()
                        response.data.let { data ->
                            if (data?.results!!.isNotEmpty()) {
                                searchAdapter.setData(data.results)
                                initSearchListRecycler()
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        advancedSearchScroll.isVisible = false
                        simpleSearchLay.isVisible = false
                        simpleSearchList.hideShimmer()
                        binding.root.showSnackBar(response.message!!)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun initSearchListRecycler() {
        binding.simpleSearchList.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = searchAdapter
        }
        //Click
        searchAdapter.setonItemClickListener {
            goToDetailPage(it)
        }
    }

    private fun goToDetailPage(id: Int) {
        val action = SearchFragmentDirections.actionToDetail(id)
        findNavController().navigate(action)
    }

    private fun initInternetLayout(isConnected: Boolean) {
        binding.internetLay.isVisible = isConnected.not()
    }

    private fun setOneButtonBackgroundTint(button: Button, color: Int) {
        button.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), color)
        )
    }

    @SuppressLint("SetTextI18n")
    private fun setAllFilterButtonsToDefault() {
        binding.apply {
            ingredientsButton.text = "INGREDIENTS"
            dietsButton.text = "DIETS"
            filtersButton.text = "Filters"
            if (isDarkTheme()) {
                setOneButtonBackgroundTint(ingredientsButton, R.color.eerie_black)
                setOneButtonBackgroundTint(dietsButton, R.color.eerie_black)
                setOneButtonBackgroundTint(filtersButton, R.color.eerie_black)
                setOneButtonTextColor(ingredientsButton, R.color.white)
                setOneButtonTextColor(dietsButton, R.color.white)
                setOneButtonTextColor(filtersButton, R.color.white)
            } else {
                setOneButtonBackgroundTint(ingredientsButton, R.color.whiteSmoke)
                setOneButtonBackgroundTint(dietsButton, R.color.whiteSmoke)
                setOneButtonBackgroundTint(filtersButton, R.color.whiteSmoke)
                setOneButtonTextColor(ingredientsButton, R.color.rose_ebony)
                setOneButtonTextColor(dietsButton, R.color.rose_ebony)
                setOneButtonTextColor(filtersButton, R.color.rose_ebony)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setOneFilterButtonsToDefault(button: Button) {
        binding.apply {
            if (isDarkTheme()) {
                setOneButtonBackgroundTint(button, R.color.eerie_black)
                setOneButtonTextColor(button, R.color.white)
            } else {
                setOneButtonBackgroundTint(button, R.color.whiteSmoke)
                setOneButtonTextColor(button, R.color.rose_ebony)
            }
        }
    }

    private fun setOneButtonTextColor(button: Button, color: Int) {
        button.setTextColor(
            ContextCompat.getColor(
                requireContext(), color
            )
        )
    }

    private fun isDarkTheme(): Boolean {
        return requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}