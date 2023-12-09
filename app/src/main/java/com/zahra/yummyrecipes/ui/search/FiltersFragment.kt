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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
    private var notSureAllergies = mutableListOf<String>()
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