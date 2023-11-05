package com.zahra.yummyrecipes.ui.search

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
    lateinit var advancedAllSearchAdapter: AdvancedAllSearchAdapter

    //Others
    private lateinit var viewModel: SearchViewModel
    private val args: SearchAllIngredientsFragmentArgs by navArgs()
    private var ingredientName = "_"
    private var ingredientPosition = -1
    private lateinit var selectedIngredientsList: List<IngredientsModel>
    private lateinit var selectedItemsInFragment: MutableSet<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel.expandedIngredientsList.value?.let { advancedAllSearchAdapter.setData(it) }
        Log.e("logZahra 11", viewModel.expandedIngredientsList.value.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchAllIngredientsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
//            viewModel.setAllIsSelectedToFalse()
            //close button
            closeImg.setOnClickListener { findNavController().navigateUp() }
            // Set up RecyclerView
            expandedList.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 4)
                adapter = advancedAllSearchAdapter
                setHasFixedSize(true)
            }

            //Args
//            args.let {
//                ingredientName = it.ingredientName
//                ingredientPosition = it.ingredientPosition
//                viewModel.setAllIsSelectedToFalse()
//                if (ingredientName != "_") {
//                    viewModel.updateExpandedIngredientByName(ingredientName, true)
//                    advancedAllSearchAdapter.toggleItemSelection(ingredientPosition)
//                    ingredientName = "_"
//                }
//            }
            // Observe and update data
            viewModel.expandedIngredientsList.observe(
                viewLifecycleOwner,
                Observer { expandedIngredients ->
//                    selectedIngredientsList = expandedIngredients.filter {
//                        it.isSelected
//                    }
                    advancedAllSearchAdapter.setData(expandedIngredients)
                    Log.e("logZahra 4", viewModel.expandedIngredientsList.value.toString())
//                    viewModel.updateSelectedList(selectedIngredientsList)
//                    Log.e("selectedIngredientsList", selectedIngredientsList.toString())
//                    Log.e("selectedIngredientsList2", viewModel.selectedList.value.toString())
                })
//            viewModel.expandedIngredientsList.value?.let { advancedAllSearchAdapter.setData(it) }
//            viewModel.selectedList.observe(viewLifecycleOwner) {
//                searchWithIngredientsButton.text = "SEARCH WITH ${it.size} INGREDIENTS"
//                searchWithIngredientsButton.isEnabled = it.isNotEmpty()
//            }
            // Set item click listener
            advancedAllSearchAdapter.setonItemClickListener { ingredientModel ->
                viewModel.updateExpandedIngredientByName(
                    ingredientModel.ingredientsName,
                    ingredientModel.isSelected
                )
                Log.e("logZahra 5", viewModel.expandedIngredientsList.value.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}