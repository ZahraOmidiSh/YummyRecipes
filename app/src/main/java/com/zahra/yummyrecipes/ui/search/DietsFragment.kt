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
import com.zahra.yummyrecipes.databinding.FragmentDietsBinding
import com.zahra.yummyrecipes.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DietsFragment : BottomSheetDialogFragment() {

    //Allergies
    //dairy free - egg free- gluten free - grain free - peanut free -
    // seafood free - shellfish free - soy free - wheat free

    //Binding
    private var _binding: FragmentDietsBinding? = null
    private val binding get() = _binding!!

    //    //Others
    private lateinit var viewModel: SearchViewModel
    private var notSureDiets = mutableListOf<String>()
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
                setShowResultButtonColor()
//                if (isDarkTheme()) {
//                    setButtonBackgroundTint(ketogenicButton, R.color.eerie_black)
//                } else {
//                    setButtonBackgroundTint(ketogenicButton, R.color.mediumGray)
//                }
                setDietsButtonColor(notSureDiets)
            }

            ketogenicButton.setOnClickListener {
                if (notSureDiets.contains("Ketogenic")) {
                    notSureDiets.remove("Ketogenic")
                    if (isDarkTheme()) {
                        setButtonBackgroundTint(ketogenicButton, R.color.eerie_black)
                        ketogenicButton.setTextColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.white
                            )
                        )
                    } else {
                        setButtonBackgroundTint(ketogenicButton, R.color.whiteSmoke)
                        ketogenicButton.setTextColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.rose_ebony
                            )
                        )
                    }
                } else {
                    notSureDiets.add("Ketogenic")
                    if (isDarkTheme()) {
                        setButtonBackgroundTint(ketogenicButton, R.color.congo_pink)
                    } else {
                        setButtonBackgroundTint(ketogenicButton, R.color.big_foot_feet)
                    }
                    ketogenicButton.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.white
                        )
                    )
                }
                showResultsButton.isEnabled = true
                showResultsButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.white
                    )
                )
                if (isDarkTheme()) {
                    setButtonBackgroundTint(showResultsButton, R.color.congo_pink)
                } else {
                    setButtonBackgroundTint(showResultsButton, R.color.big_foot_feet)
                }
            }

            veganButton.setOnClickListener {
                if (notSureDiets.contains("Vegan")) {
                    notSureDiets.remove("Vegan")
                    if (isDarkTheme()) {
                        setButtonBackgroundTint(veganButton, R.color.eerie_black)
                        veganButton.setTextColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.white
                            )
                        )
                    } else {
                        setButtonBackgroundTint(veganButton, R.color.whiteSmoke)
                        veganButton.setTextColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.rose_ebony
                            )
                        )
                    }
                } else {
                    notSureDiets.add("Vegan")
                    if (isDarkTheme()) {
                        setButtonBackgroundTint(veganButton, R.color.congo_pink)
                    } else {
                        setButtonBackgroundTint(veganButton, R.color.big_foot_feet)
                    }
                    veganButton.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.white
                        )
                    )
                }
                showResultsButton.isEnabled = true
                showResultsButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.white
                    )
                )
                if (isDarkTheme()) {
                    setButtonBackgroundTint(showResultsButton, R.color.congo_pink)
                } else {
                    setButtonBackgroundTint(showResultsButton, R.color.big_foot_feet)
                }
            }

            vegetarianButton.setOnClickListener {
                if (notSureDiets.contains("Vegetarian")) {
                    notSureDiets.remove("Vegetarian")
                    if (isDarkTheme()) {
                        setButtonBackgroundTint(vegetarianButton, R.color.eerie_black)
                        vegetarianButton.setTextColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.white
                            )
                        )
                    } else {
                        setButtonBackgroundTint(vegetarianButton, R.color.whiteSmoke)
                        vegetarianButton.setTextColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.rose_ebony
                            )
                        )
                    }
                } else {
                    notSureDiets.add("Vegetarian")
                    if (isDarkTheme()) {
                        setButtonBackgroundTint(vegetarianButton, R.color.congo_pink)
                    } else {
                        setButtonBackgroundTint(vegetarianButton, R.color.big_foot_feet)
                    }
                    vegetarianButton.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.white
                        )
                    )
                }
                showResultsButton.isEnabled = true
                showResultsButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.white
                    )
                )
                if (isDarkTheme()) {
                    setButtonBackgroundTint(showResultsButton, R.color.congo_pink)
                } else {
                    setButtonBackgroundTint(showResultsButton, R.color.big_foot_feet)
                }
            }

            //Selected Ingredients Button Listener
            showResultsButton.setOnClickListener {
                viewModel._selectedDietsData.value = notSureDiets
                viewModel.isSearchWithDiets.value =
                    viewModel.selectedDietsData.value?.isNotEmpty() == true
                notSureDiets.clear()
                findNavController().navigateUp()
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setShowResultButtonColor() {
        binding.apply {
            if (notSureDiets.isEmpty()) {
                showResultsButton.isEnabled = false
                showResultsButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.gray
                    )
                )
                if (isDarkTheme()) {
                    setButtonBackgroundTint(showResultsButton, R.color.eerie_black)
                } else {
                    setButtonBackgroundTint(showResultsButton, R.color.mediumGray)
                }
            } else {
                showResultsButton.isEnabled = true
                showResultsButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.white
                    )
                )
                if (isDarkTheme()) {
                    setButtonBackgroundTint(showResultsButton, R.color.congo_pink)
                } else {
                    setButtonBackgroundTint(showResultsButton, R.color.big_foot_feet)
                }
            }
        }
    }

    private fun setDietsButtonColor(notSureDiets: List<String>) {
        binding.apply {
            notSureDiets.forEach { diet ->
                when (diet) {
                    "Ketogenic" -> if (isDarkTheme()) {
                        setButtonBackgroundTint(ketogenicButton, R.color.congo_pink)
                    } else {
                        setButtonBackgroundTint(ketogenicButton, R.color.big_foot_feet)
                    }

                    "Vegetarian" -> if (isDarkTheme()) {
                        setButtonBackgroundTint(vegetarianButton, R.color.congo_pink)
                    } else {
                        setButtonBackgroundTint(vegetarianButton, R.color.big_foot_feet)
                    }

                    "Vegan" -> if (isDarkTheme()) {
                        setButtonBackgroundTint(veganButton, R.color.congo_pink)
                    } else {
                        setButtonBackgroundTint(veganButton, R.color.big_foot_feet)
                    }
                }
            }
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