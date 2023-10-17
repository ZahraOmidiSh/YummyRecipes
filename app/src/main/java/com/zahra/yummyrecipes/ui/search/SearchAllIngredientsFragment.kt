package com.zahra.yummyrecipes.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zahra.yummyrecipes.adapter.AdvancedAllSearchAdapter
import com.zahra.yummyrecipes.databinding.FragmentSearchAllIngredientsBinding
import com.zahra.yummyrecipes.models.search.IngredientsModel
import com.zahra.yummyrecipes.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchAllIngredientsFragment : BottomSheetDialogFragment() {
    //Binding
    private var _binding: FragmentSearchAllIngredientsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var searchAdapter: AdvancedAllSearchAdapter

    //Others
    private val viewModel: SearchViewModel by viewModels()
    private val args: SearchAllIngredientsFragmentArgs by navArgs()
    private var ingredientName = "_"

    //    private val expandedIngredientsList: MutableList<IngredientsModel> = mutableListOf()
    private val selectedIngredientsList: MutableList<IngredientsModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadExpandedIngredientsList()
    }

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
        //Args
        args.let {
            ingredientName = args.ingredientName
            if (ingredientName != "_") {
                val expandedIngredientsList: MutableList<IngredientsModel> = mutableListOf()
                viewModel.expandedIngredientsList.value?.forEach {ingredientModel ->
                    Log.e(
                        "expandedIngredientsList0",
                        viewModel.expandedIngredientsList.value.toString()
                    )
                        if (ingredientName == ingredientModel.ingredientsName) {
                            ingredientModel.isSelected = !ingredientModel.isSelected
                        }
                        expandedIngredientsList.add(ingredientModel)
                    }
                    Log.e("expandedIngredientsList1", expandedIngredientsList.toString())
                    viewModel.expandedIngredientsList.postValue(expandedIngredientsList)
                    Log.e(
                        "expandedIngredientsList2",
                        viewModel.expandedIngredientsList.value.toString()
                    )
            }
        }
        //InitViews
        binding.apply {
            //close button
            closeImg.setOnClickListener { findNavController().navigateUp() }
            //load data
            viewModel.expandedIngredientsList.observe(viewLifecycleOwner) {
                Log.e("expandedIngredientsList3", viewModel.expandedIngredientsList.value.toString() )
                searchAdapter.setData(it)
                //RecyclerView setup
                expandedList.apply {
                    layoutManager =
                        GridLayoutManager(requireContext(), 4)
                    adapter = searchAdapter
                    setHasFixedSize(true)
                }
                Log.e("expandedIngredientsList4", viewModel.expandedIngredientsList.value.toString() )
                //Click
                searchAdapter.setonItemClickListener { ingredientModel ->
                    val expandedIngredientsList: MutableList<IngredientsModel> = mutableListOf()
                    viewModel.expandedIngredientsList.value?.forEach { ingredient ->
                        if (ingredientModel == ingredient) {
                            ingredient.isSelected = !ingredient.isSelected
                        }
                        expandedIngredientsList.add(ingredient)
                    }
                    Log.e("expandedIngredientsList5", expandedIngredientsList.toString() )
                    viewModel.expandedIngredientsList.postValue(expandedIngredientsList)
                    Log.e("expandedIngredientsList6", viewModel.expandedIngredientsList.value.toString() )

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}