package com.zahra.yummyrecipes.ui.search

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zahra.yummyrecipes.adapter.AdvancedAllSearchAdapter
import com.zahra.yummyrecipes.databinding.FragmentSearchAllIngredientsBinding
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
    private var isThemeChanged: Boolean = false
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
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
        if (savedInstanceState != null) {
            // Check if the activity is being recreated due to a theme change
            isThemeChanged = savedInstanceState.getBoolean("themeChanged", false)
        }
        binding.apply {
            //close button
            closeImg.setOnClickListener {
                viewModel.expandedIngredientsList.value!!.forEach {
                    it.isSelected=false
                }
                findNavController().navigateUp() }
            // Set up RecyclerView
            expandedList.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 4)
                adapter = advancedAllSearchAdapter
                setHasFixedSize(true)
            }

            // Observe and update data
            viewModel.expandedIngredientsList.observe(
                viewLifecycleOwner
            ) { expandedIngredients ->
                advancedAllSearchAdapter.setData(expandedIngredients)
            }
            // Set item click listener
            advancedAllSearchAdapter.setonItemClickListener { ingredientModel ->
                viewModel.updateExpandedIngredientByName(
                    ingredientModel.ingredientsName,
                    ingredientModel.isSelected
                )
            }
        }

// Set up BottomSheetCallback
        val behavior = (dialog as? BottomSheetDialog)?.behavior
        behavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset == -1f && !isThemeChanged) {
                    // Bottom sheet is fully expanded and the theme has not changed
                    // Perform any necessary action here
                    viewModel.expandedIngredientsList.value!!.forEach {
                        if(it.isSelected){
                            it.isSelected=false
                        }

                    }
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // Handle other state changes if needed
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}