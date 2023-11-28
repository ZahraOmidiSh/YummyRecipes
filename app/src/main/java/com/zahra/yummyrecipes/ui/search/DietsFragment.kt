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
            //set button first position
//            setButtonFirstPosition()
//            // Set up BottomSheetCallback
//            setBottomSheetCallback()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}