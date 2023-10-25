package com.zahra.yummyrecipes.ui.search

import android.annotation.SuppressLint
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
    private var selectedIngredientsList: MutableList<IngredientsModel> = mutableListOf()

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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Args
        args.let {
            ingredientName = args.ingredientName
        }
        //InitViews
        binding.apply {
            //close button
            closeImg.setOnClickListener { findNavController().navigateUp() }
            //search with ingredientsButton
            searchWithIngredientsButton.setOnClickListener {
                val direction = SearchAllIngredientsFragmentDirections.actionToSearch()
                findNavController().navigate(direction)
            }
            //show the selected items size in button
            viewModel.selectedItems.observe(viewLifecycleOwner){
                searchWithIngredientsButton.isEnabled = it.size>0
                searchWithIngredientsButton.text="SEARCH WITH ${it.size} INGREDIENTS"
            }
            //load data
            viewModel.expandedIngredientsList.observe(viewLifecycleOwner) {

                //consider the args
                val expandedIngredientsList: MutableList<IngredientsModel> = mutableListOf()
                if (ingredientName != "_") {
                    it.forEach { ingredient ->
                        if (ingredientName == ingredient.ingredientsName) {
                            ingredient.isSelected = true
//                            selectedIngredientsList.add(ingredient)
//                            viewModel.selectedItems.value=selectedIngredientsList
//                            Log.e("selectedItems",viewModel.selectedItems.value.toString() )
                            ingredientName = "_"
                        }
                        expandedIngredientsList.add(ingredient)
                    }
                    viewModel.expandedIngredientsList.postValue(expandedIngredientsList)
                    selectedIngredientsList=
                        viewModel.expandedIngredientsList.value?.filter { ingredientModel ->
                            ingredientModel.isSelected } as MutableList<IngredientsModel>
                    Log.e("selectedItems",selectedIngredientsList.toString() )
                    viewModel.selectedItems.postValue(selectedIngredientsList)
                    Log.e("selectedItems",viewModel.selectedItems.value.toString() )

                }
                //set data
                searchAdapter.setData(it)
                //RecyclerView setup
                expandedList.apply {
                    layoutManager =
                        GridLayoutManager(requireContext(), 4)
                    adapter = searchAdapter
                    setHasFixedSize(true)
                }
                //Click
                searchAdapter.setonItemClickListener { ingredientModel ->
                    val expandIngredientsList: MutableList<IngredientsModel> = mutableListOf()
                    viewModel.expandedIngredientsList.value?.forEach { ingredient ->
                        if (ingredientModel == ingredient) {
                            ingredient.isSelected = !ingredient.isSelected
                        }
                        expandIngredientsList.add(ingredient)
                    }
                    viewModel.expandedIngredientsList.postValue(expandIngredientsList)
                    selectedIngredientsList=
                        viewModel.expandedIngredientsList.value?.filter { ingredientModels ->
                            ingredientModels.isSelected } as MutableList<IngredientsModel>
                    Log.e("selectedItems2",selectedIngredientsList.toString() )
                    viewModel.selectedItems.postValue(selectedIngredientsList)
                    Log.e("selectedItems",viewModel.selectedItems.value.toString() )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}