package com.zahra.yummyrecipes.ui.search

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zahra.yummyrecipes.R
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
    private var notSureItems = mutableListOf<String>()
    private var temporaryItems = mutableListOf<IngredientsModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchAllIngredientsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            //Selected Ingredients List
            viewModel.updateSelectedIngredientsName()
            viewModel.selectedIngredientsNameData.observe(viewLifecycleOwner) {
                notSureItems = it.toMutableList()
                println("clearing1: ${notSureItems.size}")
            }

            //set button first position
            setButtonFirstPosition()

            //close button listener
//            closeButton(closeImg)
            closeImg.setOnClickListener {
                notSureItems.clear()
                viewModel.slideOffset = 0f
                if (viewModel.isSearchWithIngredient.value == true) {


                } else {
                    viewModel.expandedIngredientsList.value!!.forEach {
                        if (it.isSelected) {
                            it.isSelected = false
                        }
                    }
                }

                findNavController().navigateUp()
            }

            // Set up RecyclerView
            setupRecyclerView(expandedList)

            // Observe and update Ingredients data
            viewModel.expandedIngredientsList.observe(
                viewLifecycleOwner
            ) { expandedIngredients ->
                advancedAllSearchAdapter.setData(expandedIngredients)
            }

            //Observe and update Selected data
            viewModel.selectedIngredientsNameData.observe(viewLifecycleOwner) { selectedList ->
                setButtonColor(notSureItems)
            }

            //Selected Ingredients Button Listener
            searchWithIngredientsButton.setOnClickListener {
                viewModel.slideOffset = 0f

                notSureItems.forEach {
                    viewModel.updateExpandedIngredientByName(
                        it, true
                    )
                }
                viewModel.updateSelectedIngredientsName()
                viewModel.isSearchWithIngredient.value =
                    viewModel.selectedIngredientsNameData.value?.isNotEmpty() == true
                notSureItems.clear()
                findNavController().navigateUp()
            }

            // Set item click listener
            setItemClickListener()
        }

        // Set up BottomSheetCallback
        setBottomSheetCallback()
    }

    @SuppressLint("SetTextI18n")
    private fun setButtonColor(notSureItems: List<String>) {
        binding.apply {
            if (notSureItems.isNotEmpty()) {
                searchWithIngredientsButton.isEnabled = true
                searchWithIngredientsButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.white
                    )
                )
                if (isDarkTheme()) {
                    setButtonBackgroundTint(searchWithIngredientsButton, R.color.congo_pink)
                } else {
                    setButtonBackgroundTint(searchWithIngredientsButton, R.color.big_foot_feet)
                }
            } else {
                searchWithIngredientsButton.isEnabled = false
                searchWithIngredientsButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.gray
                    )
                )
                if (isDarkTheme()) {
                    setButtonBackgroundTint(searchWithIngredientsButton, R.color.eerie_black)
                } else {
                    setButtonBackgroundTint(searchWithIngredientsButton, R.color.mediumGray)
                }
            }
            searchWithIngredientsButton.text = "SEARCH WITH ${notSureItems.size} INGREDIENTS"
        }
    }

    private fun setButtonBackgroundTint(Button: Button, color: Int) {
        Button.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), color)
        )
    }

    private fun setButtonFirstPosition() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                setButtonPosition(viewModel.slideOffset)
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setItemClickListener() {
        advancedAllSearchAdapter.setonItemClickListener { ingredientModel ->
            if (notSureItems.contains(ingredientModel.ingredientsName)) {
                notSureItems.remove(ingredientModel.ingredientsName)
            } else {
                notSureItems.add(ingredientModel.ingredientsName)
            }
            binding.apply {
                if (notSureItems.isNotEmpty()) {
                    searchWithIngredientsButton.isEnabled = true
                    searchWithIngredientsButton.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.white
                        )
                    )
                    if (isDarkTheme()) {
                        setButtonBackgroundTint(searchWithIngredientsButton, R.color.congo_pink)
                    } else {
                        setButtonBackgroundTint(searchWithIngredientsButton, R.color.big_foot_feet)
                    }
                } else {
                    searchWithIngredientsButton.isEnabled = false
                    searchWithIngredientsButton.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.gray
                        )
                    )
                    if (isDarkTheme()) {
                        setButtonBackgroundTint(searchWithIngredientsButton, R.color.eerie_black)
                    } else {
                        setButtonBackgroundTint(searchWithIngredientsButton, R.color.mediumGray)
                    }
                }
                searchWithIngredientsButton.text = "SEARCH WITH ${notSureItems.size} INGREDIENTS"
            }
//            viewModel.updateExpandedIngredientByName(
//                ingredientModel.ingredientsName, ingredientModel.isSelected
//            )
//            viewModel.updateSelectedIngredientsName()
        }
        println("clearing3.5: ${viewModel.expandedIngredientsList.value.toString()}")
    }

    private fun setupRecyclerView(expandedList: RecyclerView) {
        expandedList.apply {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = advancedAllSearchAdapter
            setHasFixedSize(true)
        }
    }

    private fun closeButton(closeImg: ImageView) {
        closeImg.setOnClickListener {
//            if (viewModel.isSearchWithIngredient.value == true) {
            notSureItems.clear()
            viewModel.updateSelectedIngredientsName()
            findNavController().navigateUp()
//            } else {
//                viewModel.expandedIngredientsList.value!!.forEach {
//                    it.isSelected = false
//                }
//                viewModel.slideOffset = 0f
//                viewModel._selectedIngredientsNameData.value = emptyList()
//                notSureItems.clear()
//                findNavController().navigateUp()
//            }

        }
    }


    private fun setBottomSheetCallback() {
        (dialog as? BottomSheetDialog)?.setOnDismissListener {
            viewModel.slideOffset = 0f
            viewModel.expandedIngredientsList.value!!.forEach {
                if (it.isSelected) {
                    it.isSelected = false
                }
            }
            viewModel._selectedIngredientsNameData.value = emptyList()
        }
        val behavior = (dialog as? BottomSheetDialog)?.behavior
        behavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                viewModel.slideOffset = slideOffset
                setButtonPosition(slideOffset)
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }
        })
    }

    private fun setButtonPosition(slideOffset: Float) {
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

    private fun isDarkTheme(): Boolean {
        return requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}