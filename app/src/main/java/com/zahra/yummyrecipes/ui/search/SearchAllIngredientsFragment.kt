package com.zahra.yummyrecipes.ui.search

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zahra.yummyrecipes.adapter.AdvancedAllSearchAdapter
import com.zahra.yummyrecipes.databinding.FragmentSearchAllIngredientsBinding
import com.zahra.yummyrecipes.models.search.IngredientsModel
import com.zahra.yummyrecipes.utils.setupRecyclerview
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
    private val expandedIngredientsList: MutableList<IngredientsModel> = mutableListOf()
    private val selectedIngredientsList: MutableList<IngredientsModel> = mutableListOf()
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
//        args.let {
//            ingredientName = args.ingredientName
//            if (ingredientName!="_"){
//                addToSelectedItems(ingredientName)
//            }
//
//        }
        //InitViews
        binding.apply {
            //close button
            closeImg.setOnClickListener { findNavController().navigateUp() }
            //load data
            viewModel.loadExpandedIngredientsList()
            viewModel.expandedIngredientsList.observe(viewLifecycleOwner) {
                expandedIngredientsList.clear()
                expandedIngredientsList.addAll(it)
                Log.e("expandedIngredientsList2",expandedIngredientsList.toString() )
                searchAdapter.setData(it)
                //RecyclerView setup
                expandedList.apply {
                    layoutManager =
                        GridLayoutManager(requireContext(), 4)
                    adapter = searchAdapter
                    setHasFixedSize(true)
                }
            }

            searchAdapter.setonItemClickListener {
                changeSelection(it)
                searchAdapter.setData(expandedIngredientsList)
                Log.e("expandedIngredientsList3",expandedIngredientsList.toString() )
                Log.e("expandedIngredientsList4",viewModel.expandedIngredientsList.toString() )
                //RecyclerView setup
                expandedList.apply {
                    layoutManager =
                        GridLayoutManager(requireContext(), 4)
                    adapter = searchAdapter
                    setHasFixedSize(true)
                }
            }

/*
            //Click on ingredients
            searchAdapter.setonItemClickListener { ingredientName ->
                addToSelectedItems(ingredientName)
            }


            */

        }
    }

    private fun changeSelection(ingredientsModel: IngredientsModel){
        expandedIngredientsList.forEach { ingredient ->
            if (ingredientsModel == ingredient) {
                ingredient.isSelected = !ingredient.isSelected
            }
        }
        Log.e("expandedIngredientsList",expandedIngredientsList.toString() )
//            viewModel.expandedIngredientsList.value=expandedIngredientsList
//            searchAdapter.setData(expandedIngredientsList)
//            //RecyclerView setup
//            binding.apply {
//                expandedList.apply {
//                    layoutManager =
//                        GridLayoutManager(requireContext(), 4)
//                    adapter = searchAdapter
//                    setHasFixedSize(true)
//                }
//            }


    }

    private fun findIngredientModel(ingredientsName: String): IngredientsModel{
        var ingredientsModel=IngredientsModel()
        viewModel.expandedIngredientsList.observe(viewLifecycleOwner) {
            it.forEach { ingredient ->
                if (ingredientsName == ingredient.ingredientsName) {
                    ingredientsModel=ingredient
                }
                val index = it.indexOf(ingredient)
            }
        }
        return ingredientsModel
    }
    private fun addOrRemoveToSelectedItems(ingredientName:String){
        val ingredientsModel=findIngredientModel(ingredientName)
        if(ingredientsModel.isSelected){
            selectedIngredientsList.remove(ingredientsModel)
            ingredientsModel.isSelected=false
            viewModel.selectedItems.postValue(selectedIngredientsList)
        }else{
            selectedIngredientsList.add(ingredientsModel)
            ingredientsModel.isSelected=true
            viewModel.selectedItems.postValue(selectedIngredientsList)

        }
    }
    private fun setupSelectedItemsRecyclerView(list: MutableList<IngredientsModel>) {


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}