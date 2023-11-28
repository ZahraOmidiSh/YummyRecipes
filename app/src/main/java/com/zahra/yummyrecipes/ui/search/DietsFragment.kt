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
import com.zahra.yummyrecipes.databinding.FragmentDietsBinding
import com.zahra.yummyrecipes.databinding.FragmentSearchAllIngredientsBinding
import com.zahra.yummyrecipes.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
        viewModel.selectedDietsData.observe(viewLifecycleOwner) { diets ->
            notSureDiets = diets.toMutableList()
            setButtonColor(notSureDiets)
        }
        binding.apply {
            ketoButton.setOnClickListener {
                if(notSureDiets.contains("Keto")){
                    notSureDiets.remove("Keto,")
                    if (isDarkTheme()) {
                        setButtonBackgroundTint(ketoButton, R.color.eerie_black)
                    } else {
                        setButtonBackgroundTint(ketoButton, R.color.mediumGray)
                    }
                }else{
                    notSureDiets.add("Keto,")
                    if (isDarkTheme()) {
                        setButtonBackgroundTint(ketoButton, R.color.congo_pink)
                    } else {
                        setButtonBackgroundTint(ketoButton, R.color.big_foot_feet)
                    }
                }

            }

            //Selected Ingredients Button Listener
            showResultsButton.setOnClickListener {
                viewModel._selectedDietsData.value=notSureDiets
                viewModel.isSearchWithDiets.value =
                    viewModel.selectedDietsData.value?.isNotEmpty() == true
                notSureDiets.clear()
                findNavController().navigateUp()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setButtonColor(notSureDiets: List<String>) {
        binding.apply {
            if (notSureDiets.isNotEmpty()) {
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
            } else {
                if (viewModel.isSearchWithDiets.value == true) {
                    if (isDarkTheme()) {
                        setButtonBackgroundTint(showResultsButton, R.color.congo_pink)
                    } else {
                        setButtonBackgroundTint(showResultsButton, R.color.big_foot_feet)
                    }
                } else {
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
                }
            }
        }
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

    private fun setBottomSheetCallback() {
        (dialog as? BottomSheetDialog)?.setOnDismissListener {
            viewModel.slideOffset = 0f
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