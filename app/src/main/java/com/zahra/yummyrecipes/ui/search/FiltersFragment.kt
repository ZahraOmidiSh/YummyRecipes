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
    private var notSureTime = mutableListOf<String>()
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
            //observe Meal data - set showResultsButton color
            viewModel.selectedMealsData.observe(viewLifecycleOwner) { meals ->
                notSureMeals = meals.toMutableList()
                setMealsButtonColor(notSureMeals)
                setShowResultButtonColor()
            }
            //observe Time data - set showResultsButton color
            viewModel.selectedTimeData.observe(viewLifecycleOwner) { time ->
                notSureTime = time.toMutableList()
                setTimeButtonColor(notSureTime)
                setShowResultButtonColor()
            }

            breakfastButton.setOnClickListener {
                setMealButtonClickListener(breakfastButton, "breakfast")
            }
            mainCourseButton.setOnClickListener {
                setMealButtonClickListener(mainCourseButton, "main course")
            }
            snackButton.setOnClickListener {
                setMealButtonClickListener(snackButton, "snack")
            }
            dessertButton.setOnClickListener {
                setMealButtonClickListener(dessertButton, "dessert")
            }

            under15MinutesButton.setOnClickListener {
                setTimeButtonClickListener(under15MinutesButton, "15")
            }
            under30MinutesButton.setOnClickListener {
                setTimeButtonClickListener(under30MinutesButton, "30")
            }
            under60MinutesButton.setOnClickListener {
                setTimeButtonClickListener(under60MinutesButton, "60")
            }

            //Show results Button Listener
            showResultsButton.setOnClickListener {
                //Meals
                viewModel._selectedMealsData.value = notSureMeals
                viewModel.isSearchWithMeals.value =
                    viewModel.selectedMealsData.value?.isNotEmpty() == true
                //Time
                viewModel._selectedTimeData.value = notSureTime
                viewModel.isSearchWithTime.value =
                    viewModel.selectedTimeData.value?.isNotEmpty() == true
                //CloseButton
                if (viewModel.isSearchWithMeals.value == true || viewModel.isSearchWithTime.value == true) {
                    viewModel.isCloseButtonPressed.value = false
                    viewModel.isSearchWithMealOrTime.value = true
                } else {
                    viewModel.isSearchWithMealOrTime.value = false
                }
                notSureMeals.clear()
                notSureTime.clear()
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

            setButtonBackgroundBasedOnTheme(button, R.color.eerie_black, R.color.whiteSmoke)
            setButtonTextColorBasedOnTheme(button, R.color.white, R.color.rose_ebony)

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

    private fun setTimeButtonClickListener(button: Button, time: String) {
        if (notSureTime.contains(time)) {
            notSureTime.remove(time)
            setButtonBackgroundBasedOnTheme(button, R.color.eerie_black, R.color.whiteSmoke)
            setButtonTextColorBasedOnTheme(button, R.color.white, R.color.rose_ebony)
        } else {
            setButtonToDefault(binding.under15MinutesButton)
            setButtonToDefault(binding.under30MinutesButton)
            setButtonToDefault(binding.under60MinutesButton)
            notSureTime.clear()
            notSureTime.add(time)
            setButtonBackgroundBasedOnTheme(button, R.color.congo_pink, R.color.big_foot_feet)
            setButtonTextColorBasedOnTheme(button, R.color.white, R.color.white)
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
                    "breakfast" ->{
                        setButtonBackgroundBasedOnTheme(breakfastButton, R.color.congo_pink, R.color.big_foot_feet)
                        setButtonTextColorBasedOnTheme(breakfastButton, R.color.white, R.color.white)
                    }


                    "main course" -> {
                        setButtonBackgroundBasedOnTheme(mainCourseButton, R.color.congo_pink, R.color.big_foot_feet)
                        setButtonTextColorBasedOnTheme(mainCourseButton, R.color.white, R.color.white)
                    }

                    "dessert" -> {
                        setButtonBackgroundBasedOnTheme(dessertButton, R.color.congo_pink, R.color.big_foot_feet)
                        setButtonTextColorBasedOnTheme(dessertButton, R.color.white, R.color.white)
                    }

                    "snack" -> {
                        setButtonBackgroundBasedOnTheme(snackButton, R.color.congo_pink, R.color.big_foot_feet)
                        setButtonTextColorBasedOnTheme(snackButton, R.color.white, R.color.white)
                    }
                }
            }
        }
    }

    private fun setTimeButtonColor(notSureTime: List<String>) {
        binding.apply {
            setButtonToDefault(under15MinutesButton)
            setButtonToDefault(under30MinutesButton)
            setButtonToDefault(under60MinutesButton)
            notSureTime.forEach { time ->
                when (time) {
                    "15" -> {
                        setButtonBackgroundBasedOnTheme(under15MinutesButton, R.color.congo_pink, R.color.big_foot_feet)
                        setButtonTextColorBasedOnTheme(under15MinutesButton, R.color.white, R.color.white)
                    }

                    "30" -> {
                        setButtonBackgroundBasedOnTheme(under30MinutesButton, R.color.congo_pink, R.color.big_foot_feet)
                        setButtonTextColorBasedOnTheme(under30MinutesButton, R.color.white, R.color.white)
                    }

                    "60" ->{
                        setButtonBackgroundBasedOnTheme(under60MinutesButton, R.color.congo_pink, R.color.big_foot_feet)
                        setButtonTextColorBasedOnTheme(under60MinutesButton, R.color.white, R.color.white)
                    }
                }
            }
        }
    }

    private fun setButtonToDefault(button: Button) {
        setButtonBackgroundBasedOnTheme(
            button, R.color.eerie_black, R.color.whiteSmoke
        )
        setButtonTextColorBasedOnTheme(button, R.color.white, R.color.rose_ebony)
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

    private fun setButtonTextColorBasedOnTheme(
        button: Button, darkThemeColor: Int, lightThemeColor: Int
    ) {
        if (isDarkTheme()) {
            button.setTextColor(
                ContextCompat.getColor(
                    requireContext(), darkThemeColor
                )
            )
        } else {
            button.setTextColor(
                ContextCompat.getColor(
                    requireContext(), lightThemeColor
                )
            )
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