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
            //make just selected items "isSelected=true"
            viewModel.updateExpandedIngredientBySelectedNames()

            viewModel.selectedIngredientsNameData.observe(viewLifecycleOwner) {
                notSureItems = it.toMutableList()
            }

            //set button first position
            setButtonFirstPosition()

            //close button listener
            closeButton(closeImg)

            // Set up RecyclerView
            setupRecyclerView(expandedList)

            // Observe and update Ingredients data
            viewModel.expandedIngredientsList.observe(
                viewLifecycleOwner
            ) { expandedIngredients ->
                advancedAllSearchAdapter.setData(expandedIngredients)
            }

            //Observe and update Selected data
            viewModel.selectedIngredientsNameData.observe(viewLifecycleOwner) {
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
                searchWithIngredientsButton.text = "SEARCH WITH ${notSureItems.size} INGREDIENTS"
            } else {
                if (viewModel.isSearchWithIngredient.value == true) {
                    if (isDarkTheme()) {
                        setButtonBackgroundTint(searchWithIngredientsButton, R.color.congo_pink)
                    } else {
                        setButtonBackgroundTint(searchWithIngredientsButton, R.color.big_foot_feet)
                    }
                    searchWithIngredientsButton.text =
                        "NO INGREDIENTS!"
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
                    searchWithIngredientsButton.text =
                        "SEARCH WITH ${notSureItems.size} INGREDIENTS"
                }
            }
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
                    searchWithIngredientsButton.text =
                        "SEARCH WITH ${notSureItems.size} INGREDIENTS"
                } else {
                    if (viewModel.isSearchWithIngredient.value == true) {
                        searchWithIngredientsButton.isEnabled = true
                        if (isDarkTheme()) {
                            setButtonBackgroundTint(searchWithIngredientsButton, R.color.congo_pink)
                        } else {
                            setButtonBackgroundTint(
                                searchWithIngredientsButton,
                                R.color.big_foot_feet
                            )
                        }
                        searchWithIngredientsButton.text =
                            "NO INGREDIENTS!"
                    } else {
                        searchWithIngredientsButton.isEnabled = false
                        searchWithIngredientsButton.setTextColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.gray
                            )
                        )
                        if (isDarkTheme()) {
                            setButtonBackgroundTint(
                                searchWithIngredientsButton,
                                R.color.eerie_black
                            )
                        } else {
                            setButtonBackgroundTint(searchWithIngredientsButton, R.color.mediumGray)
                        }
                        searchWithIngredientsButton.text =
                            "SEARCH WITH ${notSureItems.size} INGREDIENTS"
                    }
                }
            }
        }
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
            viewModel.slideOffset = 0f
            if (viewModel.isSearchWithIngredient.value == true) {
                viewModel.updateExpandedIngredientBySelectedNames()
            } else {
                viewModel.expandedIngredientsList.value!!.forEach {
                    if (it.isSelected) {
                        it.isSelected = false
                    }
                }
                viewModel.updateSelectedIngredientsName()
            }
            findNavController().navigateUp()
        }
    }


    private fun setBottomSheetCallback() {
        (dialog as? BottomSheetDialog)?.setOnDismissListener {
            viewModel.slideOffset = 0f
            if (viewModel.isSearchWithIngredient.value == true) {
                viewModel.updateExpandedIngredientBySelectedNames()
            } else {
                viewModel.expandedIngredientsList.value!!.forEach {
                    if (it.isSelected) {
                        it.isSelected = false
                    }
                }
                viewModel.updateSelectedIngredientsName()
            }
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