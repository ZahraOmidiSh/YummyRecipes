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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.todkars.shimmer.ShimmerRecyclerView
import com.zahra.yummyrecipes.adapter.SuggestedAdapter
import com.zahra.yummyrecipes.databinding.FragmentRecipeBinding
import com.zahra.yummyrecipes.models.recipe.ResponseRecipes
import com.zahra.yummyrecipes.utils.Constants.DELAY_TIME
import com.zahra.yummyrecipes.utils.Constants.REPEAT_TIME
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.setupRecyclerview
import com.zahra.yummyrecipes.utils.showSnackBar
import com.zahra.yummyrecipes.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class RecipeFragment : Fragment() {
    //Binding
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var suggestedAdapter: SuggestedAdapter

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
        //Call api
        recipeViewModel.callSuggestedApi(recipeViewModel.suggestedQueries())
        //Load data
        loadSuggestedData()


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
                                suggestedAdapter.setData(data.results)
                                initSuggestedRecycler()
                                autoScrollSuggested(data.results)
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
            //Go to detail page
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