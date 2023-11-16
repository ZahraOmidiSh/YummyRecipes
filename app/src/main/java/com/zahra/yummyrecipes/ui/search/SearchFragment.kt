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
import kotlin.properties.Delegates

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
    private var isNetworkAvailable by Delegates.notNull<Boolean>()
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
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
            viewAllSearchByIngredients.setOnClickListener {
                viewModel.expandedIngredientsList.value?.forEach {
                    it.isSelected=false
                }
                val direction = SearchFragmentDirections.actionToSearchAllIngredients()
                findNavController().navigate(direction)
            }
            ingredientsList.setupRecyclerview(
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                ),
                advancedSearchAdapter
            )
            //Click on items
            advancedSearchAdapter.setonItemClickListener { ingredientName ->
                viewModel.expandedIngredientsList.value?.forEach {
                    it.isSelected=false
                }
                viewModel.updateExpandedIngredientByName(ingredientName, true)
                val action =
                    SearchFragmentDirections.actionToSearchAllIngredients()
                viewModel.updateSelectedIngredientsName()
                findNavController().navigate(action)
            }

            //Check Internet
            lifecycleScope.launch {
                withStarted { }
                networkChecker.checkNetworkAvailability().collect { state ->
                    initInternetLayout(state)
                    isNetworkAvailable = state
                }
            }
            //Search
            searchEdt.addTextChangedListener {
//                if (it.toString().length > 2 && isNetworkAvailable) {
                if (isNetworkAvailable) {
                    searchString = it.toString()
                    viewModel.callSearchApi(viewModel.searchQueries(it.toString()))
                    loadRecentData()
                } else {
                    simpleSearchLay.isVisible = false
                    advancedSearchScroll.isVisible = true
                }
            }

            viewModel.isSearchWithIngredient.observe(viewLifecycleOwner) {
                if (it) {
                    closeImg.isVisible = true
                    viewModel.callSearchApi(viewModel.searchQueries(searchString))
                    loadRecentData()
                    ingredientsButton.text =
                        "INGREDIENTS " + "(" + viewModel.selectedIngredientsNameData.value?.size.toString() + ")"
                    if (isDarkTheme()) {
                        setButtonBackgroundTint(ingredientsButton, R.color.congo_pink)
                    } else {
                        setButtonBackgroundTint(ingredientsButton, R.color.big_foot_feet)
                    }
                } else {
                    closeImg.isVisible = false
                    ingredientsButton.text = "INGREDIENTS"
                    if (isDarkTheme()) {
                        setButtonBackgroundTint(ingredientsButton, R.color.pale_pink)
                    } else {
                        setButtonBackgroundTint(ingredientsButton, R.color.eerie_black)
                    }
                }
            }
            ingredientsButton.setOnClickListener {
                val direction = SearchFragmentDirections.actionToSearchAllIngredients()
                findNavController().navigate(direction)
            }
            //Close listener
            closeImg.setOnClickListener {
                viewModel.expandedIngredientsList.value!!.forEach {
                    if (it.isSelected) {
                        it.isSelected = false
                    }
                }
                searchString = ""
                simpleSearchLay.isVisible = false
                advancedSearchScroll.isVisible = true
                closeImg.isVisible = false

            }
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

    private fun setButtonBackgroundTint(Button: Button, color: Int) {
        Button.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), color)
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