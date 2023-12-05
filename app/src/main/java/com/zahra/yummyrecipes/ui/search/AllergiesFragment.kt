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
import com.zahra.yummyrecipes.databinding.FragmentAllergiesBinding
import com.zahra.yummyrecipes.viewmodel.SearchViewModel

class AllergiesFragment : BottomSheetDialogFragment() {
    //Binding
    private var _binding: FragmentAllergiesBinding? = null
    private val binding get() = _binding!!

    //Others
    private lateinit var viewModel: SearchViewModel
    private var notSureAllergies = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllergiesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            //observe data - set showResultsButton color
            viewModel.selectedAllergiesData.observe(viewLifecycleOwner) { allergies ->
                notSureAllergies = allergies.toMutableList()
                setShowResultButtonColor()
                setAllergiesButtonColor(notSureAllergies)
            }

            dairyButton.setOnClickListener {
                setButtonClickListener(dairyButton, "Dairy")
            }
            eggButton.setOnClickListener {
                setButtonClickListener(eggButton, "Egg")
            }
            grainButton.setOnClickListener {
                setButtonClickListener(grainButton, "Grain")
            }
            peanutButton.setOnClickListener {
                setButtonClickListener(peanutButton, "Peanut")
            }
            seafoodButton.setOnClickListener {
                setButtonClickListener(seafoodButton, "Seafood")
            }
            soyButton.setOnClickListener {
                setButtonClickListener(soyButton, "Soy")
            }

            //Show results Button Listener
            showResultsButton.setOnClickListener {
                viewModel._selectedAllergiesData.value = notSureAllergies
                viewModel.isSearchWithAllergies.value =
                    viewModel.selectedAllergiesData.value?.isNotEmpty() == true
                if (viewModel.isSearchWithAllergies.value == true) {
                    viewModel.isCloseButtonPressed.value = false
                }
                notSureAllergies.clear()
                findNavController().navigateUp()
            }
            closeImg.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setButtonClickListener(button: Button, allergyName: String) {
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
            if (notSureAllergies.isEmpty()) {
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