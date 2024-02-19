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
            searchEdt.setText(viewModel.searchString.value.toString())

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

            //total search
            totalSearch()

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
                viewModel.updateTotalSearchValue()
            }

            //ovenBaked
            ovenBaked.setOnClickListener {
                viewModel.isCloseButtonPressed.value = false
                viewModel._selectedToolsData.value = listOf("oven")
                viewModel.isSearchWithTools.value = true
                viewModel.isSearchWithFilters.value = true
                viewModel.updateTotalSearchValue()
            }

            //Asian
            asian.setOnClickListener {
                viewModel.isCloseButtonPressed.value = false
                viewModel._selectedRegionData.value = listOf("Asian")
                viewModel.isSearchWithRegion.value = true
                viewModel.isSearchWithFilters.value = true
                viewModel.updateTotalSearchValue()
            }

            //Mediterranean
            mediterranean.setOnClickListener {
                viewModel.isCloseButtonPressed.value = false
                viewModel._selectedRegionData.value = listOf("Mediterranean")
                viewModel.isSearchWithRegion.value = true
                viewModel.isSearchWithFilters.value = true
                viewModel.updateTotalSearchValue()
            }

            //Vegetarian
            vegetarian.setOnClickListener {
                viewModel.isCloseButtonPressed.value = false
                viewModel._selectedDietsData.value = listOf("Vegetarian")
                viewModel.isSearchWithDiets.value = true
                viewModel.isSearchWithDietsOrAllergies.value = true
                viewModel.updateTotalSearchValue()
            }

            //QuickAndEasy
            quickAndEasy.setOnClickListener {
                viewModel.isCloseButtonPressed.value = false
                viewModel._selectedTimeData.value = listOf("15")
                viewModel.isSearchWithTime.value = true
                viewModel.isSearchWithFilters.value = true
                viewModel.updateTotalSearchValue()
            }
        }
    }

    private fun mealClickListener(meal: String) {
        viewModel.isCloseButtonPressed.value = false
        viewModel._selectedMealsData.value = listOf(meal)
        viewModel.isSearchWithMeals.value = true
        viewModel.isSearchWithFilters.value = true
        viewModel.updateTotalSearchValue()
    }

    private fun closeSearch() {
        binding.apply {
            emptyTxt.isVisible = false
            viewModel.isCloseButtonPressed.value = true
            searchEdt.text.clear()
            viewModel.searchString.value = ""
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
            viewModel.updateTotalSearchValue()
            simpleSearchLay.isVisible = false
            advancedSearchScroll.isVisible = true
            closeImg.isVisible = false
            emptyTxt.isVisible = false
        }
    }

    private fun textChangeListener() {
        binding.searchEdt.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                binding.closeImg.isVisible = true
                viewModel.searchString.value = it.toString()
                if (isNetworkAvailable == true) {
                    viewModel.isCloseButtonPressed.value = false
                    viewModel.updateTotalSearchValue()
                }
            } else {
                viewModel.searchString.value = ""
                viewModel.updateTotalSearchValue()
            }
        }
    }

    private fun totalSearch() {
        binding.apply {
            viewModel.totalSearch.observe(viewLifecycleOwner) {
                if (it) {
                    emptyTxt.isVisible = false
                    simpleSearchLay.isVisible = true
                    advancedSearchScroll.isVisible = false
                    closeImg.isVisible = true
                    viewModel.callSearchApi(viewModel.searchQueries(viewModel.searchString.value.toString()))
                    loadRecentData()
                    setAllFilterButtonsSizeAndColor()
                } else {
                    emptyTxt.isVisible = false
                    setAllFilterButtonsToDefault()
                    simpleSearchLay.isVisible = false
                    advancedSearchScroll.isVisible = true
                    closeImg.isVisible = false
                }
            }
        }
    }

    private fun setAllFilterButtonsSizeAndColor() {
        setIngredientsSize()
        setDietsSize()
        setFiltersSize()
    }

    @SuppressLint("SetTextI18n")
    private fun setIngredientsSize() {
        binding.apply {
            var sizeIngredients = 0
            if (viewModel.selectedIngredientsNameData.value?.isNotEmpty() == true) {
                sizeIngredients = viewModel.selectedIngredientsNameData.value?.size!!
            }
            if (sizeIngredients == 0) {
                ingredientsButton.text = "INGREDIENTS"
                setButtonTextColorBasedOnTheme(ingredientsButton, R.color.white, R.color.rose_ebony)
                setButtonBackgroundBasedOnTheme(
                    ingredientsButton,
                    R.color.eerie_black,
                    R.color.whiteSmoke
                )
            } else {
                ingredientsButton.text = "INGREDIENTS ($sizeIngredients)"
                setButtonTextColorBasedOnTheme(ingredientsButton, R.color.white, R.color.white)
                setButtonBackgroundBasedOnTheme(
                    ingredientsButton,
                    R.color.congo_pink,
                    R.color.big_foot_feet
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDietsSize() {
        binding.apply {
            var sizeDiets = 0
            var sizeAllergies = 0
            if (viewModel.selectedDietsData.value?.isNotEmpty() == true) {
                sizeDiets = viewModel.selectedDietsData.value?.size!!
            }
            if (viewModel._selectedAllergiesData.value?.isNotEmpty() == true) {
                sizeAllergies = viewModel.selectedAllergiesData.value?.size!!
            }
            val size = sizeDiets + sizeAllergies
            if (size == 0) {
                dietsButton.text = "DIETS"
                setButtonTextColorBasedOnTheme(dietsButton, R.color.white, R.color.rose_ebony)
                setButtonBackgroundBasedOnTheme(
                    dietsButton,
                    R.color.eerie_black,
                    R.color.whiteSmoke
                )
            } else {
                dietsButton.text = "DIETS ($size)"
                setButtonTextColorBasedOnTheme(dietsButton, R.color.white, R.color.white)
                setButtonBackgroundBasedOnTheme(
                    dietsButton,
                    R.color.congo_pink,
                    R.color.big_foot_feet
                )
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setFiltersSize() {
        binding.apply {
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
            if (size == 0) {
                filtersButton.text = "FILTERS"
                setButtonTextColorBasedOnTheme(filtersButton, R.color.white, R.color.rose_ebony)
                setButtonBackgroundBasedOnTheme(
                    filtersButton,
                    R.color.eerie_black,
                    R.color.whiteSmoke
                )
            } else {
                filtersButton.text = "FILTERS ($size)"
                setButtonTextColorBasedOnTheme(filtersButton, R.color.white, R.color.white)
                setButtonBackgroundBasedOnTheme(
                    filtersButton,
                    R.color.congo_pink,
                    R.color.big_foot_feet
                )
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
            val action =
                SearchFragmentDirections.actionToSearchAllIngredients()
            viewModel.updateSelectedIngredientsName()
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView(ingredientsList: RecyclerView) {
        ingredientsList.setupRecyclerview(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            ),
            advancedSearchAdapter
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
                        simpleSearchList.isVisible = true
                        emptyTxt.isVisible = false
                        simpleSearchList.showShimmer()
                    }

                    is NetworkRequest.Success -> {
                        simpleSearchList.hideShimmer()
                        response.data.let { data ->
                            if (data?.results!!.isNotEmpty()) {
                                binding.simpleSearchList.isVisible = true
                                emptyTxt.isVisible = false
                                searchAdapter.setData(data.results)
                                initSearchListRecycler()
                            } else {
                                simpleSearchList.isVisible = false
                                emptyTxt.isVisible = true
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        advancedSearchScroll.isVisible = false
                        emptyTxt.isVisible = false
                        simpleSearchLay.isVisible = false
                        simpleSearchList.hideShimmer()
                        root.showSnackBar(response.message!!)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun initSearchListRecycler() {
        binding.simpleSearchList.apply {
            layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
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

    private fun setOneButtonTextColor(button: Button, color: Int) {
        button.setTextColor(
            ContextCompat.getColor(
                requireContext(), color
            )
        )
    }

    private fun setButtonBackgroundBasedOnTheme(
        button: Button,
        darkThemeColor: Int,
        lightThemeColor: Int
    ) {
        if (isDarkTheme()) {
            setOneButtonBackgroundTint(button, darkThemeColor)
        } else {
            setOneButtonBackgroundTint(button, lightThemeColor)
        }
    }

    private fun setButtonTextColorBasedOnTheme(
        button: Button,
        darkThemeColor: Int,
        lightThemeColor: Int
    ) {
        if (isDarkTheme()) {
            button.setTextColor(
                ContextCompat.getColor(
                    requireContext(), darkThemeColor
                )
            )
        } else {
            button.setTextColor(
                ContextCompat.getColor(
                    requireContext(), lightThemeColor
                )
            )
        }
    }

    private fun isDarkTheme(): Boolean {
        return requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}