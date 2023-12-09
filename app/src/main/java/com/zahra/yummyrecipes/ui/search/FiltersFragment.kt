package com.zahra.yummyrecipes.ui.search

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.databinding.FragmentFiltersBinding
import com.zahra.yummyrecipes.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FiltersFragment : BottomSheetDialogFragment() {
    //Binding
    private var _binding: FragmentFiltersBinding? = null
    private val binding get() = _binding!!

    //Others
    private lateinit var viewModel: SearchViewModel
    private var notSureMeals = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFiltersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            //observe data - set showResultsButton color
            viewModel.selectedMealsData.observe(viewLifecycleOwner) { meals ->
                notSureMeals = meals.toMutableList()
                setMealsButtonColor(notSureMeals)
                setShowResultButtonColor()
            }
            breakfastButton.setOnClickListener {
                setMealButtonClickListener(breakfastButton, "breakfast")
            }
            mainCourseButton.setOnClickListener {
                setMealButtonClickListener(breakfastButton, "main course")
            }
            snackButton.setOnClickListener {
                setMealButtonClickListener(breakfastButton, "snack")
            }
            dessertButton.setOnClickListener {
                setMealButtonClickListener(breakfastButton, "dessert")
            }

            //Show results Button Listener
            showResultsButton.setOnClickListener {
                viewModel._selectedMealsData.value = notSureMeals
                viewModel.isSearchWithMeals.value =
                    viewModel.selectedMealsData.value?.isNotEmpty() == true
                if (viewModel.isSearchWithMeals.value == true ) {
                    viewModel.isCloseButtonPressed.value = false
                }
                notSureMeals.clear()
                findNavController().navigateUp()
            }
            closeImg.setOnClickListener {
                findNavController().navigateUp()
            }

        }

    }

    private fun setMealButtonClickListener(button: Button, mealName: String) {
        if (notSureMeals.contains(mealName)) {
            notSureMeals.remove(mealName)
            if (isDarkTheme()) {
                setButtonBackgroundTint(button, R.color.eerie_black)
                button.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.white
                    )
                )
            } else {
                setButtonBackgroundTint(button, R.color.whiteSmoke)
                button.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.rose_ebony
                    )
                )
            }
        } else {
            notSureMeals.add(mealName)
            setButtonBackgroundBasedOnTheme(button, R.color.congo_pink, R.color.big_foot_feet)

            button.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.white
                )
            )
        }
        binding.showResultsButton.isEnabled = true
        binding.showResultsButton.setTextColor(
            ContextCompat.getColor(
                requireContext(), R.color.white
            )
        )
        setButtonBackgroundBasedOnTheme(
            binding.showResultsButton, R.color.congo_pink, R.color.big_foot_feet
        )

    }

    private fun setMealsButtonColor(notSureMeals: List<String>) {
        binding.apply {
            notSureMeals.forEach { meal ->
                when (meal) {
                    "breakfast" -> setButtonBackgroundBasedOnTheme(
                        breakfastButton, R.color.congo_pink, R.color.big_foot_feet
                    )

                    "main course" -> setButtonBackgroundBasedOnTheme(
                        mainCourseButton, R.color.congo_pink, R.color.big_foot_feet
                    )

                    "dessert" -> setButtonBackgroundBasedOnTheme(
                        dessertButton, R.color.congo_pink, R.color.big_foot_feet
                    )

                    "snack" -> setButtonBackgroundBasedOnTheme(
                        snackButton, R.color.congo_pink, R.color.big_foot_feet
                    )
                }
            }
        }
    }

    private fun setShowResultButtonColor() {
        binding.apply {
            if (notSureMeals.isEmpty()) {
                showResultsButton.isEnabled = false
                showResultsButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.gray
                    )
                )
                setButtonBackgroundBasedOnTheme(
                    showResultsButton, R.color.eerie_black, R.color.mediumGray
                )
            } else {
                showResultsButton.isEnabled = true
                showResultsButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.white
                    )
                )
                setButtonBackgroundBasedOnTheme(
                    showResultsButton, R.color.congo_pink, R.color.big_foot_feet
                )
            }
        }
    }

    private fun setButtonBackgroundBasedOnTheme(
        button: Button, darkThemeColor: Int, lightThemeColor: Int
    ) {
        if (isDarkTheme()) {
            setButtonBackgroundTint(button, darkThemeColor)
        } else {
            setButtonBackgroundTint(button, lightThemeColor)
        }
    }

    private fun isDarkTheme(): Boolean {
        return requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    private fun setButtonBackgroundTint(Button: Button, color: Int) {
        Button.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), color)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}