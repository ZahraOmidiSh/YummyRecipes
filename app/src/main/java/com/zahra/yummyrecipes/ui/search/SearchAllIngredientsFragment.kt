package com.zahra.yummyrecipes.ui.search

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zahra.yummyrecipes.adapter.AdvancedAllSearchAdapter
import com.zahra.yummyrecipes.databinding.FragmentSearchAllIngredientsBinding
import com.zahra.yummyrecipes.models.search.IngredientsModel
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

    //Others
    private val viewModel: SearchViewModel by viewModels()
    private val expandedIngredientsList: MutableList<IngredientsModel> = mutableListOf()
    private var tracker: SelectionTracker<IngredientsModel>? = null
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
            //close button
            closeImg.setOnClickListener { findNavController().navigateUp() }
            //load data
//            viewModel.loadExpandedIngredientsList()
//            viewModel.expandedIngredientsList.observe(viewLifecycleOwner) {
//                expandedIngredientsList.clear()
//                expandedIngredientsList.addAll(it)
//                searchAdapter.setData(expandedIngredientsList)
//
//
//                viewModel.selectedItemsLiveData.observe(viewLifecycleOwner){
//                    Toast.makeText(requireContext(), "Selected Items: $it", Toast.LENGTH_SHORT).show()
////                    searchAdapter.notifyDataSetChanged()
//                }

            //RecyclerView setup
            expandedList.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 4)
                adapter = searchAdapter
                setHasFixedSize(true)
            }

            // Set up the selection tracker
            tracker = SelectionTracker.Builder<IngredientsModel>(
                "ingredientSelection",
                expandedList,
                DemoStableIdProvider(searchAdapter),
                IngredientItemDetailsLookup(expandedList),
                StorageStrategy.createParcelableStorage()
            ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
            ).build()

            searchAdapter.tracker = tracker

            tracker?.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    // Handle selection change here if needed
                }
            })

            // Load the ingredient items from the ViewModel
            viewModel.loadIngredients()
            searchAdapter.setData(viewModel.ingredientItems)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

