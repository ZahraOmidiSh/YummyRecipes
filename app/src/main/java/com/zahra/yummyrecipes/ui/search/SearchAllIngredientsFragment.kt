package com.zahra.yummyrecipes.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zahra.yummyrecipes.adapter.AdvancedAllSearchAdapter
import com.zahra.yummyrecipes.adapter.AdvancedSearchAdapter
import com.zahra.yummyrecipes.databinding.FragmentSearchAllIngredientsBinding
import com.zahra.yummyrecipes.models.search.IngredientsModel
import com.zahra.yummyrecipes.utils.setupRecyclerview
import com.zahra.yummyrecipes.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchAllIngredientsFragment : Fragment() {
    //Binding
    private var _binding: FragmentSearchAllIngredientsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var searchAdapter: AdvancedAllSearchAdapter

    @Inject
    lateinit var selectedAdapter: AdvancedSearchAdapter

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
        args.let {
            ingredientName = args.ingredientName
            if (ingredientName!="_"){
                addToSelectedItems(ingredientName)
            }

        }
        //InitViews
        binding.apply {
            //close button
            closeImg.setOnClickListener { findNavController().navigateUp() }
            //load data
            viewModel.loadExpandedIngredientsList()
            viewModel.expandedIngredientsList.observe(viewLifecycleOwner) {
                expandedIngredientsList.clear()
                expandedIngredientsList.addAll(it)
                searchAdapter.setData(it)
                //RecyclerView setup
                expandedList.apply {
                    layoutManager =
                        GridLayoutManager(requireContext(), 4)
                    adapter = searchAdapter
                    setHasFixedSize(true)
                }
            }

            //Click
            searchAdapter.setonItemClickListener { ingredientName ->
                addToSelectedItems(ingredientName)

            }
        }
    }

    private fun addToSelectedItems(ingredientsName: String) {
        viewModel.expandedIngredientsList.observe(viewLifecycleOwner) {
            it.forEach { ingredient ->
                if (ingredientsName == ingredient.ingredientsName) {
                    selectedIngredientsList.add(ingredient)
                    viewModel.selectedItems.postValue(selectedIngredientsList)

                    //add to list
                    setupSelectedItemsRecyclerView(selectedIngredientsList)
                }

            }
        }
    }

    private fun setupSelectedItemsRecyclerView(list: MutableList<IngredientsModel>) {
        binding.apply {
            SelectedIngredientsTxt.isVisible=true
            selectedList.isVisible=true
            selectedAdapter.setData(list)
            selectedList.setupRecyclerview(
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                selectedAdapter
            )
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}