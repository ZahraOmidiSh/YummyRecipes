package com.zahra.yummyrecipes.ui.recipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.todkars.shimmer.ShimmerRecyclerView
import com.zahra.yummyrecipes.adapter.GeneralRecipesAdapter
import com.zahra.yummyrecipes.adapter.SuggestedAdapter
import com.zahra.yummyrecipes.databinding.FragmentRecipeBinding
import com.zahra.yummyrecipes.models.recipe.ResponseRecipes
import com.zahra.yummyrecipes.utils.Constants.DELAY_TIME
import com.zahra.yummyrecipes.utils.Constants.REPEAT_TIME
import com.zahra.yummyrecipes.utils.NetworkChecker
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.onceObserve
import com.zahra.yummyrecipes.utils.setupRecyclerview
import com.zahra.yummyrecipes.utils.showSnackBar
import com.zahra.yummyrecipes.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class RecipeFragment : Fragment() {
    //Binding
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var suggestedAdapter: SuggestedAdapter
    @Inject
    lateinit var economicalAdapter: GeneralRecipesAdapter
    @Inject
    lateinit var quickAdapter: GeneralRecipesAdapter
    @Inject
    lateinit var veganAdapter: GeneralRecipesAdapter
    @Inject
    lateinit var healthyAdapter: GeneralRecipesAdapter
    @Inject
    lateinit var networkChecker: NetworkChecker

    //other
    private val recipeViewModel: RecipeViewModel by viewModels()
    private var autoScrollIndex = 0
    private val TAG = "RecipeFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews

        //Show Greeting
        showGreeting()
        showSlogan()

        //Call data
        callSuggestedData()
        callEconomicalData()
        callQuickData()
        callVeganData()
        callHealthyData()

        //Load data
        loadSuggestedData()
        loadEconomicalData()
        loadQuickData()
        loadVeganData()
        loadHealthyData()


        //
//        binding.viewAllMealsOnABudget.setOnClickListener{
//
//        }
        //


    }

    //Suggested
    private fun callSuggestedData(){
        initSuggestedRecycler()
        lifecycleScope.launch {
            withStarted {  }
            networkChecker.checkNetworkAvailability().collect{state ->
                if(state){
                    recipeViewModel.callSuggestedApi(recipeViewModel.suggestedQueries())
                }else{
                    recipeViewModel.readSuggestedFromDb.onceObserve(viewLifecycleOwner) { database ->
                        if(database.isNotEmpty()){
                            database[0].response.results?.let {results ->
                                fillSuggestedAdapter(results.toMutableList())
                            }
                        }
                    }
                }

            }
        }

    }
    private fun loadSuggestedData() {
        recipeViewModel.suggestedData.observe(viewLifecycleOwner) { response ->
            binding.apply {
                when (response) {
                    is NetworkRequest.Loading -> {
                        setupLoading(true, suggestedList)
                    }

                    is NetworkRequest.Success -> {
                        setupLoading(false, suggestedList)
                        response.data?.let { data ->
                            if (data.results!!.isNotEmpty()) {
                                fillSuggestedAdapter(data.results.toMutableList())
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        setupLoading(false, suggestedList)
                        root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }
    private fun fillSuggestedAdapter(results:MutableList<ResponseRecipes.Result>){
        suggestedAdapter.setData(results)
        autoScrollSuggested(results)
    }
    private fun initSuggestedRecycler() {
        val snapHelper = LinearSnapHelper()
        binding.suggestedList.setupRecyclerview(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            suggestedAdapter
        )
        //Snap
        snapHelper.attachToRecyclerView(binding.suggestedList)
        //Click
        suggestedAdapter.setonItemClickListener {
            val action = RecipeFragmentDirections.actionToDetail(it)
            findNavController().navigate(action)
        }

    }
    private fun autoScrollSuggested(list:List<ResponseRecipes.Result>){
        lifecycleScope.launch {
            withStarted {  }
            repeat(REPEAT_TIME){
                delay(DELAY_TIME)
                val myLayoutManager: LinearLayoutManager = binding.suggestedList.layoutManager as LinearLayoutManager
                val scrollPosition = myLayoutManager.findFirstVisibleItemPosition()

                autoScrollIndex=scrollPosition
                if(autoScrollIndex < list.size){
                    autoScrollIndex++
                }else{
                    autoScrollIndex=0
                }
                binding.suggestedList.smoothScrollToPosition(autoScrollIndex)
                if(autoScrollIndex==10){
                    binding.suggestedList.smoothScrollToPosition(0)
                }

            }
        }

    }

    //Economical
    private fun callEconomicalData(){
        initEconomicalRecycler()
        recipeViewModel.readEconomicalFromDb.onceObserve(viewLifecycleOwner){database ->
            if(database.isNotEmpty() && database.size>1){
                database[1].response.results?.let {results ->
                    setupLoading(false, binding.mealsOnABudgetList)
                    economicalAdapter.setData(results)
                }
            }else{
                recipeViewModel.callEconomicalApi(recipeViewModel.economicalQueries(10))
            }

        }
    }
    private fun loadEconomicalData() {
        recipeViewModel.economicalData.observe(viewLifecycleOwner) { response ->
            binding.apply {
                when (response) {
                    is NetworkRequest.Loading -> {
                        setupLoading(true, mealsOnABudgetList)
                    }

                    is NetworkRequest.Success -> {
                        setupLoading(false, mealsOnABudgetList)
                        response.data?.let { data ->
                            if (data.results!!.isNotEmpty()) {
                                economicalAdapter.setData(data.results)
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        setupLoading(false, mealsOnABudgetList)
                        root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }


    private fun initEconomicalRecycler() {
        binding.mealsOnABudgetList.setupRecyclerview(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            economicalAdapter
        )

        //Click
        economicalAdapter.setonItemClickListener {
            val action = RecipeFragmentDirections.actionToDetail(it)
            findNavController().navigate(action)
        }

    }

    //Quick
    private fun callQuickData(){
        initQuickRecycler()
        recipeViewModel.readQuickFromDb.onceObserve(viewLifecycleOwner){database ->
            if(database.isNotEmpty() && database.size>2){
                database[2].response.results?.let {results ->
                    setupLoading(false, binding.quickAndEasyList)
                    quickAdapter.setData(results)
                }
            }else{
                recipeViewModel.callQuickApi(recipeViewModel.quickQueries())
            }

        }
    }
    private fun loadQuickData() {
        recipeViewModel.quickData.observe(viewLifecycleOwner) { response ->
            binding.apply {
                when (response) {
                    is NetworkRequest.Loading -> {
                        setupLoading(true, quickAndEasyList)
                    }

                    is NetworkRequest.Success -> {
                        setupLoading(false, quickAndEasyList)
                        response.data?.let { data ->
                            if (data.results!!.isNotEmpty()) {
                                quickAdapter.setData(data.results)
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        setupLoading(false, quickAndEasyList)
                        root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }
    private fun initQuickRecycler() {
        binding.quickAndEasyList.setupRecyclerview(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            quickAdapter
        )

        //Click
        quickAdapter.setonItemClickListener {
            val action = RecipeFragmentDirections.actionToDetail(it)
            findNavController().navigate(action)
        }

    }


    //Vegan
    private fun callVeganData(){
        initVeganRecycler()
        recipeViewModel.readVeganFromDb.onceObserve(viewLifecycleOwner){database ->
            if(database.isNotEmpty() && database.size>3){
                database[3].response.results?.let {results ->
                    setupLoading(false, binding.veganList)
                    veganAdapter.setData(results)
                }
            }else{
                recipeViewModel.callVeganApi(recipeViewModel.veganQueries())
            }

        }
    }
    private fun loadVeganData() {
        recipeViewModel.veganData.observe(viewLifecycleOwner) { response ->
            binding.apply {
                when (response) {
                    is NetworkRequest.Loading -> {
                        setupLoading(true, veganList)
                    }

                    is NetworkRequest.Success -> {
                        setupLoading(false, veganList)
                        response.data?.let { data ->
                            if (data.results!!.isNotEmpty()) {
                                veganAdapter.setData(data.results)
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        setupLoading(false, veganList)
                        root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }
    private fun initVeganRecycler() {
        binding.veganList.setupRecyclerview(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            veganAdapter
        )

        //Click
        veganAdapter.setonItemClickListener {
            val action = RecipeFragmentDirections.actionToDetail(it)
            findNavController().navigate(action)
        }

    }


    //Healthy
    private fun callHealthyData(){
        initHealthyRecycler()
        recipeViewModel.readHealthyFromDb.onceObserve(viewLifecycleOwner){database ->
            if(database.isNotEmpty() && database.size>4){
                database[4].response.results?.let {results ->
                    setupLoading(false, binding.healthyList)
                    healthyAdapter.setData(results)
                }
            }else{
                recipeViewModel.callHealthyApi(recipeViewModel.healthyQueries())
            }

        }
    }
    private fun loadHealthyData() {
        recipeViewModel.healthyData.observe(viewLifecycleOwner) { response ->
            binding.apply {
                when (response) {
                    is NetworkRequest.Loading -> {
                        setupLoading(true, healthyList)
                    }

                    is NetworkRequest.Success -> {
                        setupLoading(false, healthyList)
                        response.data?.let { data ->
                            if (data.results!!.isNotEmpty()) {
                                healthyAdapter.setData(data.results)
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        setupLoading(false, healthyList)
                        root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }
    private fun initHealthyRecycler() {
        binding.healthyList.setupRecyclerview(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            healthyAdapter
        )

        //Click
        healthyAdapter.setonItemClickListener {
            val action = RecipeFragmentDirections.actionToDetail(it)
            findNavController().navigate(action)
        }

    }

    private fun setupLoading(isShownLoading: Boolean, shimmer: ShimmerRecyclerView) {
        shimmer.apply {
            if (isShownLoading) showShimmer() else hideShimmer()
        }
    }


    private fun showGreeting() {
        recipeViewModel.showGreeting()
        recipeViewModel.Greeting.observe(viewLifecycleOwner) {
            binding.usernameTxt.text = it
        }
    }

    private fun showSlogan() {
        recipeViewModel.getSlogan()
        recipeViewModel.slogan.observe(viewLifecycleOwner) {
            binding.sloganTxt.text = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}