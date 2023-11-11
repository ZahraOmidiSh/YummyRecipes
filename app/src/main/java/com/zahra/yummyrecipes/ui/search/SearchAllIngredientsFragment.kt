package com.zahra.yummyrecipes.ui.search

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        //set button position
        setButtonPosition()

        // Check if the activity is being recreated due to a theme change
        themeChangeChecker(savedInstanceState)

        binding.apply {

            //close button listener
            closeButton(closeImg)

            // Set up RecyclerView
            setupRecyclerView(expandedList)

            // Observe and update data
            viewModel.expandedIngredientsList.observe(
                viewLifecycleOwner
            ) { expandedIngredients ->
                advancedAllSearchAdapter.setData(expandedIngredients)
            }

            // Set item click listener
            setItemClickListener()
        }

        // Set up BottomSheetCallback
        setBottomSheetCallback()
    }

    private fun setItemClickListener() {
        advancedAllSearchAdapter.setonItemClickListener { ingredientModel ->
            viewModel.updateExpandedIngredientByName(
                ingredientModel.ingredientsName,
                ingredientModel.isSelected
            )
        }
    }

    private fun setupRecyclerView(expandedList: RecyclerView) {
        expandedList.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 4)
            adapter = advancedAllSearchAdapter
            setHasFixedSize(true)
        }
    }

    private fun closeButton(closeImg: ImageView) {
        closeImg.setOnClickListener {
            viewModel.expandedIngredientsList.value!!.forEach {
                it.isSelected = false
            }
            findNavController().navigateUp()
        }
    }

    private fun themeChangeChecker(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            isThemeChanged = savedInstanceState.getBoolean("themeChanged", false)
        }
    }

    private fun setButtonPosition() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                setButtonPosition(0f)
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun setBottomSheetCallback() {
        val behavior = (dialog as? BottomSheetDialog)?.behavior
        behavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset == -1f && !isThemeChanged) {
                    // Bottom sheet is fully expanded and the theme has not changed
                    viewModel.expandedIngredientsList.value!!.forEach {
                        if (it.isSelected) {
                            it.isSelected = false
                        }
                    }
                }
                setButtonPosition(slideOffset)
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }
        })
    }

    fun setButtonPosition(slideOffset: Float) {
        val parentHeight = binding.root.height
        val parentWidth = binding.root.width
        val fraction = parentHeight / parentWidth
        var desiredPosition = 0.0
        if (fraction >= 1.85) {
            desiredPosition = 0.622 * parentHeight + (600 * slideOffset)
        } else {
            desiredPosition = 0.54 * parentHeight + (600 * slideOffset)
        }
        // Set the position of your view
        binding.searchWithIngredientsButton.y = desiredPosition.toFloat()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}