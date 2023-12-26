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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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
    private var notSureRegion = mutableListOf<String>()
    private var notSureCalorie = mutableListOf<String>()
    private var notSureTools = mutableListOf<String>()
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
            //observe Tools data - set showResultsButton color
            viewModel.selectedToolsData.observe(viewLifecycleOwner) { tools ->
                notSureTools = tools.toMutableList()
                setToolsButtonColor(notSureTools)
                setShowResultButtonColor()
            }
            //observe Region data - set showResultsButton color
            viewModel.selectedRegionData.observe(viewLifecycleOwner) { region ->
                notSureRegion = region.toMutableList()
                setRegionButtonColor(notSureRegion)
                setShowResultButtonColor()
            }
            //observe Time data - set showResultsButton color
            viewModel.selectedTimeData.observe(viewLifecycleOwner) { time ->
                notSureTime = time.toMutableList()
                setTimeButtonColor(notSureTime)
                setShowResultButtonColor()
            }
            //observe Calorie data - set showResultsButton color
            viewModel.selectedCalorieData.observe(viewLifecycleOwner) { calorie ->
                notSureCalorie = calorie.toMutableList()
                setCalorieButtonColor(notSureCalorie)
                setShowResultButtonColor()
            }

            //set button first position
            setButtonFirstPosition()

            //Time
            timeFilters()

            //Calorie
            calorieFilters()

            //Meals
            mealFilters()

            //Tools
            toolFilters()

            //Region
            regionFilters()

            //Show results Button Listener
            showResultsButton.setOnClickListener {
                resultButton()
            }
            closeImg.setOnClickListener {
                viewModel.filterSlideOffset = 0f
                findNavController().navigateUp()
            }
        }
        // Set up BottomSheetCallback
        setBottomSheetCallback()
    }

    private fun resultButton() {
        binding.apply {
            //Meals
            viewModel._selectedMealsData.value = notSureMeals
            viewModel.isSearchWithMeals.value =
                viewModel.selectedMealsData.value?.isNotEmpty() == true
            //Tools
            viewModel._selectedToolsData.value = notSureTools
            viewModel.isSearchWithTools.value =
                viewModel.selectedToolsData.value?.isNotEmpty() == true
            //Region
            viewModel._selectedRegionData.value = notSureRegion
            viewModel.isSearchWithRegion.value =
                viewModel.selectedRegionData.value?.isNotEmpty() == true
            //Calorie
            viewModel._selectedCalorieData.value = notSureCalorie
            viewModel.isSearchWithCalorie.value =
                viewModel.selectedCalorieData.value?.isNotEmpty() == true
            //Time
            viewModel._selectedTimeData.value = notSureTime
            viewModel.isSearchWithTime.value =
                viewModel.selectedTimeData.value?.isNotEmpty() == true
            //CloseButton
            if (viewModel.isSearchWithMeals.value == true || viewModel.isSearchWithTime.value == true ||
                viewModel.isSearchWithRegion.value == true || viewModel.isSearchWithCalorie.value == true || viewModel.isSearchWithTools.value == true
            ) {
                viewModel.isCloseButtonPressed.value = false
                viewModel.isSearchWithFilters.value = true
            } else {
                viewModel.isSearchWithFilters.value = false
            }
            viewModel.updateTotalSearchValue()
            notSureMeals.clear()
            notSureTime.clear()
            notSureRegion.clear()
            notSureCalorie.clear()
            notSureTools.clear()
            findNavController().navigateUp()
        }
    }

    private fun timeFilters() {
        binding.apply {
            under15MinutesButton.setOnClickListener {
                setTimeButtonClickListener(under15MinutesButton, "15")
            }
            under30MinutesButton.setOnClickListener {
                setTimeButtonClickListener(under30MinutesButton, "30")
            }
            under60MinutesButton.setOnClickListener {
                setTimeButtonClickListener(under60MinutesButton, "60")
            }
        }

    }

    private fun calorieFilters() {
        binding.apply {
            under200CalButton.setOnClickListener {
                setCalorieButtonClickListener(under200CalButton, "0-200")
            }
            between200400CalButton.setOnClickListener {
                setCalorieButtonClickListener(between200400CalButton, "200-400")
            }
            between400600CalButton.setOnClickListener {
                setCalorieButtonClickListener(between400600CalButton, "400-600")
            }
            between600800CalButton.setOnClickListener {
                setCalorieButtonClickListener(between600800CalButton, "600-800")
            }
        }

    }

    private fun mealFilters() {
        binding.apply {
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
        }

    }

    private fun toolFilters() {
        binding.apply {
            ovenButton.setOnClickListener {
                setToolsButtonClickListener(ovenButton, "oven")
            }
            blenderButton.setOnClickListener {
                setToolsButtonClickListener(blenderButton, "blender")
            }
            foodProcessorButton.setOnClickListener {
                setToolsButtonClickListener(foodProcessorButton, "food-processor")
            }
        }

    }

    private fun regionFilters() {
        binding.apply {
            asianButton.setOnClickListener {
                setRegionButtonClickListener(asianButton, "Asian")
            }
            chineseButton.setOnClickListener {
                setRegionButtonClickListener(chineseButton, "Chinese")
            }
            frenchButton.setOnClickListener {
                setRegionButtonClickListener(frenchButton, "French")
            }
            indianButton.setOnClickListener {
                setRegionButtonClickListener(indianButton, "Indian")
            }
            italianButton.setOnClickListener {
                setRegionButtonClickListener(italianButton, "Italian")
            }
            mediterraneanButton.setOnClickListener {
                setRegionButtonClickListener(mediterraneanButton, "Mediterranean")
            }
        }

    }


    private fun setButtonFirstPosition() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                setButtonPosition(viewModel.filterSlideOffset)
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun setBottomSheetCallback() {
        (dialog as? BottomSheetDialog)?.setOnDismissListener {
            viewModel.filterSlideOffset = 0f
            findNavController().navigateUp()
        }
        val behavior = (dialog as? BottomSheetDialog)?.behavior
        behavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                viewModel.filterSlideOffset = slideOffset
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
        binding.showResultsButton.y = desiredPosition.toFloat()

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

    private fun setToolsButtonClickListener(button: Button, toolName: String) {
        if (notSureTools.contains(toolName)) {
            notSureTools.remove(toolName)

            setButtonBackgroundBasedOnTheme(button, R.color.eerie_black, R.color.whiteSmoke)
            setButtonTextColorBasedOnTheme(button, R.color.white, R.color.rose_ebony)

        } else {
            notSureTools.add(toolName)
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

    private fun setRegionButtonClickListener(button: Button, regionName: String) {
        if (notSureRegion.contains(regionName)) {
            notSureRegion.remove(regionName)

            setButtonBackgroundBasedOnTheme(button, R.color.eerie_black, R.color.whiteSmoke)
            setButtonTextColorBasedOnTheme(button, R.color.white, R.color.rose_ebony)

        } else {
            notSureRegion.add(regionName)
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

    private fun setCalorieButtonClickListener(button: Button, calorie: String) {
        if (notSureCalorie.contains(calorie)) {
            notSureCalorie.remove(calorie)
            setButtonBackgroundBasedOnTheme(button, R.color.eerie_black, R.color.whiteSmoke)
            setButtonTextColorBasedOnTheme(button, R.color.white, R.color.rose_ebony)
        } else {
            setButtonToDefault(binding.under200CalButton)
            setButtonToDefault(binding.between200400CalButton)
            setButtonToDefault(binding.between400600CalButton)
            setButtonToDefault(binding.between600800CalButton)
            notSureCalorie.clear()
            notSureCalorie.add(calorie)
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
                    "breakfast" -> {
                        setButtonBackgroundBasedOnTheme(
                            breakfastButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(
                            breakfastButton, R.color.white, R.color.white
                        )
                    }


                    "main course" -> {
                        setButtonBackgroundBasedOnTheme(
                            mainCourseButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(
                            mainCourseButton, R.color.white, R.color.white
                        )
                    }

                    "dessert" -> {
                        setButtonBackgroundBasedOnTheme(
                            dessertButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(dessertButton, R.color.white, R.color.white)
                    }

                    "snack" -> {
                        setButtonBackgroundBasedOnTheme(
                            snackButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(snackButton, R.color.white, R.color.white)
                    }
                }
            }
        }
    }

    private fun setToolsButtonColor(notSureTools: List<String>) {
        binding.apply {
            notSureTools.forEach { tool ->
                when (tool) {
                    "oven" -> {
                        setButtonBackgroundBasedOnTheme(
                            ovenButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(
                            ovenButton, R.color.white, R.color.white
                        )
                    }

                    "blender" -> {
                        setButtonBackgroundBasedOnTheme(
                            blenderButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(
                            blenderButton, R.color.white, R.color.white
                        )
                    }

                    "food-processor" -> {
                        setButtonBackgroundBasedOnTheme(
                            foodProcessorButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(
                            foodProcessorButton,
                            R.color.white,
                            R.color.white
                        )
                    }

                }
            }
        }
    }

    private fun setRegionButtonColor(notSureRegions: List<String>) {
        binding.apply {
            notSureRegions.forEach { region ->
                when (region) {
                    "Asian" -> {
                        setButtonBackgroundBasedOnTheme(
                            asianButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(
                            asianButton, R.color.white, R.color.white
                        )
                    }


                    "Chinese" -> {
                        setButtonBackgroundBasedOnTheme(
                            chineseButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(
                            chineseButton, R.color.white, R.color.white
                        )
                    }

                    "French" -> {
                        setButtonBackgroundBasedOnTheme(
                            frenchButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(frenchButton, R.color.white, R.color.white)
                    }

                    "Indian" -> {
                        setButtonBackgroundBasedOnTheme(
                            indianButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(indianButton, R.color.white, R.color.white)
                    }

                    "Italian" -> {
                        setButtonBackgroundBasedOnTheme(
                            italianButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(italianButton, R.color.white, R.color.white)
                    }

                    "Mediterranean" -> {
                        setButtonBackgroundBasedOnTheme(
                            mediterraneanButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(
                            mediterraneanButton, R.color.white, R.color.white
                        )
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
                        setButtonBackgroundBasedOnTheme(
                            under15MinutesButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(
                            under15MinutesButton, R.color.white, R.color.white
                        )
                    }

                    "30" -> {
                        setButtonBackgroundBasedOnTheme(
                            under30MinutesButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(
                            under30MinutesButton, R.color.white, R.color.white
                        )
                    }

                    "60" -> {
                        setButtonBackgroundBasedOnTheme(
                            under60MinutesButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(
                            under60MinutesButton, R.color.white, R.color.white
                        )
                    }
                }
            }
        }
    }

    private fun setCalorieButtonColor(notSureCalorie: List<String>) {
        binding.apply {
            setButtonToDefault(binding.under200CalButton)
            setButtonToDefault(binding.between200400CalButton)
            setButtonToDefault(binding.between400600CalButton)
            notSureCalorie.forEach { calorie ->
                when (calorie) {
                    "0-200" -> {
                        setButtonBackgroundBasedOnTheme(
                            under200CalButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(
                            under200CalButton, R.color.white, R.color.white
                        )
                    }

                    "200-400" -> {
                        setButtonBackgroundBasedOnTheme(
                            between200400CalButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(
                            between200400CalButton, R.color.white, R.color.white
                        )
                    }

                    "400-600" -> {
                        setButtonBackgroundBasedOnTheme(
                            between400600CalButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(
                            between400600CalButton, R.color.white, R.color.white
                        )
                    }

                    "600-800" -> {
                        setButtonBackgroundBasedOnTheme(
                            between600800CalButton, R.color.congo_pink, R.color.big_foot_feet
                        )
                        setButtonTextColorBasedOnTheme(
                            between600800CalButton, R.color.white, R.color.white
                        )
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
            if (notSureMeals.isEmpty() && notSureTime.isEmpty() && notSureRegion.isEmpty()
                && notSureCalorie.isEmpty() && notSureTools.isEmpty()
            ) {
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