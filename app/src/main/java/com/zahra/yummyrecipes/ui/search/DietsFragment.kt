package com.zahra.yummyrecipes.ui.search

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.databinding.FragmentDietsBinding
import com.zahra.yummyrecipes.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DietsFragment : BottomSheetDialogFragment() {

    //Binding
    private var _binding: FragmentDietsBinding? = null
    private val binding get() = _binding!!

    //    //Others
    private lateinit var viewModel: SearchViewModel
    private var notSureDiets = mutableListOf<String>()
    private var notSureAllergies = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
    }

    //
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDietsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    //
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            //observe data - set showResultsButton color
            viewModel.selectedDietsData.observe(viewLifecycleOwner) { diets ->
                notSureDiets = diets.toMutableList()
//                setShowResultButtonColor()
                setDietsButtonColor(notSureDiets)
                setShowResultButtonColor()
            }

            //observe data - set showResultsButton color
            viewModel.selectedAllergiesData.observe(viewLifecycleOwner) { allergies ->
                notSureAllergies = allergies.toMutableList()
//                setShowResultButtonColor()
                setAllergiesButtonColor(notSureAllergies)
                setShowResultButtonColor()
            }


            //lifestyles
            ketogenicButton.setOnClickListener {
                setLifestylesButtonClickListener(ketogenicButton, "Ketogenic")
            }
            veganButton.setOnClickListener {
                setLifestylesButtonClickListener(veganButton, "Vegan")
            }
            vegetarianButton.setOnClickListener {
                setLifestylesButtonClickListener(vegetarianButton, "Vegetarian")
            }
            glutenFreeButton.setOnClickListener {
                setLifestylesButtonClickListener(glutenFreeButton, "Gluten Free")
            }
            pescetarianButton.setOnClickListener {
                setLifestylesButtonClickListener(pescetarianButton, "Pescetarian")
            }
            paleoButton.setOnClickListener {
                setLifestylesButtonClickListener(paleoButton, "Paleo")
            }

            //allergies
            dairyButton.setOnClickListener {
                setAllergiesButtonClickListener(dairyButton, "Dairy")
            }
            eggButton.setOnClickListener {
                setAllergiesButtonClickListener(eggButton, "Egg")
            }
            grainButton.setOnClickListener {
                setAllergiesButtonClickListener(grainButton, "Grain")
            }
            peanutButton.setOnClickListener {
                setAllergiesButtonClickListener(peanutButton, "Peanut")
            }
            seafoodButton.setOnClickListener {
                setAllergiesButtonClickListener(seafoodButton, "Seafood")
            }
            soyButton.setOnClickListener {
                setAllergiesButtonClickListener(soyButton, "Soy")
            }

            //Show results Button Listener
            showResultsButton.setOnClickListener {
                viewModel._selectedDietsData.value = notSureDiets
                viewModel.isSearchWithDiets.value =
                    viewModel.selectedDietsData.value?.isNotEmpty() == true
                viewModel._selectedAllergiesData.value = notSureAllergies
                viewModel.isSearchWithAllergies.value =
                    viewModel.selectedAllergiesData.value?.isNotEmpty() == true
                if (viewModel.isSearchWithDiets.value == true || viewModel.isSearchWithAllergies.value == true) {
                    viewModel.isCloseButtonPressed.value = false
                    viewModel.isSearchWithDietsOrAllergies.value=true
                }else{
                    viewModel.isSearchWithDietsOrAllergies.value=false
                }
                notSureDiets.clear()
                notSureAllergies.clear()
                findNavController().navigateUp()
            }
            closeImg.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setLifestylesButtonClickListener(button: Button, dietName: String) {
        if (notSureDiets.contains(dietName)) {
            notSureDiets.remove(dietName)
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
            notSureDiets.add(dietName)
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

    private fun setAllergiesButtonClickListener(button: Button, allergyName: String) {
        if (notSureAllergies.contains(allergyName)) {
            notSureAllergies.remove(allergyName)
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
            notSureAllergies.add(allergyName)
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


    @SuppressLint("SetTextI18n")
    private fun setShowResultButtonColor() {
        binding.apply {
            if (notSureDiets.isEmpty() && notSureAllergies.isEmpty()) {
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

    private fun setDietsButtonColor(notSureDiets: List<String>) {
        binding.apply {
            notSureDiets.forEach { diet ->
                when (diet) {
                    "Ketogenic" -> setButtonBackgroundBasedOnTheme(
                        ketogenicButton, R.color.congo_pink, R.color.big_foot_feet
                    )

                    "Vegetarian" -> setButtonBackgroundBasedOnTheme(
                        vegetarianButton, R.color.congo_pink, R.color.big_foot_feet
                    )

                    "Vegan" -> setButtonBackgroundBasedOnTheme(
                        veganButton, R.color.congo_pink, R.color.big_foot_feet
                    )

                    "Pescetarian" -> setButtonBackgroundBasedOnTheme(
                        pescetarianButton, R.color.congo_pink, R.color.big_foot_feet
                    )

                    "Gluten Free" -> setButtonBackgroundBasedOnTheme(
                        glutenFreeButton, R.color.congo_pink, R.color.big_foot_feet
                    )

                    "Paleo" -> setButtonBackgroundBasedOnTheme(
                        paleoButton, R.color.congo_pink, R.color.big_foot_feet
                    )

                }
            }
        }
    }

    private fun setAllergiesButtonColor(notSureAllergies: List<String>) {
        binding.apply {
            notSureAllergies.forEach { allergy ->
                when (allergy) {
                    "Dairy" -> setButtonBackgroundBasedOnTheme(
                        dairyButton, R.color.congo_pink, R.color.big_foot_feet
                    )

                    "Egg" -> setButtonBackgroundBasedOnTheme(
                        eggButton, R.color.congo_pink, R.color.big_foot_feet
                    )

                    "Grain" -> setButtonBackgroundBasedOnTheme(
                        grainButton, R.color.congo_pink, R.color.big_foot_feet
                    )

                    "Peanut" -> setButtonBackgroundBasedOnTheme(
                        peanutButton, R.color.congo_pink, R.color.big_foot_feet
                    )

                    "Seafood" -> setButtonBackgroundBasedOnTheme(
                        seafoodButton, R.color.congo_pink, R.color.big_foot_feet
                    )

                    "Soy" -> setButtonBackgroundBasedOnTheme(
                        soyButton, R.color.congo_pink, R.color.big_foot_feet
                    )

                }
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