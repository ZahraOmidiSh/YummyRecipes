package com.zahra.yummyrecipes.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.adapter.AdvancedSearchAdapter
import com.zahra.yummyrecipes.databinding.FragmentSearchAllIngredientsBinding
import com.zahra.yummyrecipes.models.search.IngredientsModel
import com.zahra.yummyrecipes.viewmodel.SearchViewModel
import javax.inject.Inject

class SearchAllIngredientsFragment : Fragment() {
    //Binding
    private var _binding: FragmentSearchAllIngredientsBinding? = null
    private val binding get() = _binding!!


    @Inject
    lateinit var advancedSearchAdapter: AdvancedSearchAdapter

    //Others
    private val viewModel: SearchViewModel by viewModels()
    private val searchIngredientsList: MutableList<IngredientsModel> = mutableListOf()

    val hotDog = IngredientsModel(0, "hot_dog", R.drawable.hot_dog)
    val cupcake = IngredientsModel(0, "cupcake", R.drawable.cupcake)
    val doughnut = IngredientsModel(0, "doughnut", R.drawable.doughnut)
    val data = mutableListOf(hotDog, cupcake, doughnut)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchAllIngredientsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Search by Ingredients
            viewModel.loadLimitIngredientsList()
            viewModel.limitIngredientsList.observe(viewLifecycleOwner) {
                searchIngredientsList.addAll(it)
                advancedSearchAdapter.setData(searchIngredientsList)
//                ingredientsList.setupRecyclerview(
//                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
//                    searchVariousAdapter
//                )
//            }
//            viewAllSearchByIngredients.setOnClickListener {
                //go to searchAllIngredients
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}