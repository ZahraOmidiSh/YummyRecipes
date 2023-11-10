package com.zahra.yummyrecipes.ui.search

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
    private var isThemeChanged: Boolean = false
    private lateinit var viewModel: SearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchAllIngredientsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews


        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val parentHeight = binding.root.height
                 val parentWidth = binding.root.width
                Log.e("parentHeight", parentHeight.toString() )
                Log.e("parentWidth", parentWidth.toString() )
                val fraction = parentHeight/parentWidth
                Log.e("fraction", fraction.toString() )
                var desiredPosition=0.0
                if(fraction>=1.85){
                    desiredPosition =0.622*parentHeight
                }else{
                    desiredPosition =0.54*parentHeight
                }
                // Set the position of your view
                binding.searchWithIngredientsButton.y = desiredPosition.toFloat()
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                // Use the parent width here
            }
        })
//        Log.e("parentWidth", parentWidth.toString() )




        if (savedInstanceState != null) {
            // Check if the activity is being recreated due to a theme change
            isThemeChanged = savedInstanceState.getBoolean("themeChanged", false)
        }
        binding.apply {

//            val layoutParams = searchWithIngredientsButton.layoutParams as ConstraintLayout.LayoutParams
//            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
//            if(fraction>=1.8){
//                layoutParams.topMargin = (0.95*parentHeight).toInt()
//            }else{
//                layoutParams.topMargin  = (0.50*parentHeight).toInt()
//            }
//            layoutParams.topMargin = 200 // Set the top margin in pixels
//            searchWithIngredientsButton.layoutParams = layoutParams
//            searchWithIngredientsButton.requestLayout()

            //close button
            closeImg.setOnClickListener {
                viewModel.expandedIngredientsList.value!!.forEach {
                    it.isSelected = false
                }
                findNavController().navigateUp()
            }
            // Set up RecyclerView
            expandedList.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 4)
                adapter = advancedAllSearchAdapter
                setHasFixedSize(true)
            }
            // Observe and update data
            viewModel.expandedIngredientsList.observe(
                viewLifecycleOwner
            ) { expandedIngredients ->
                advancedAllSearchAdapter.setData(expandedIngredients)
            }
            // Set item click listener
            advancedAllSearchAdapter.setonItemClickListener { ingredientModel ->
                viewModel.updateExpandedIngredientByName(
                    ingredientModel.ingredientsName,
                    ingredientModel.isSelected
                )
            }
        }

        // Set up BottomSheetCallback
        val behavior = (dialog as? BottomSheetDialog)?.behavior
        behavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset == -1f && !isThemeChanged) {
                    // Bottom sheet is fully expanded and the theme has not changed
                    viewModel.expandedIngredientsList.value!!.forEach {
                        if (it.isSelected) {
                            it.isSelected = false
                        }
                    }
                }

//                val desiredPosition = ((binding.root.height - 1000) * slideOffset)+900

                // Set the position of your view
//                binding.searchWithIngredientsButton.y = desiredPosition


                val bottomSheetHeight = bottomSheet.height



                // Calculate the desired position based on the height of the bottom sheet
//                val desiredPosition = parentHeight - bottomSheetHeight.toFloat()+1350+(600*slideOffset)
                val parentHeight = binding.root.height
                val parentWidth = binding.root.width
//                Log.e("screenHeight", parentHeight.toString() )
                val fraction = parentHeight/parentWidth
                var desiredPosition=0.0
                if(fraction>=1.85){
                     desiredPosition =0.622*parentHeight+(600*slideOffset)
                }else{
                     desiredPosition =0.54*parentHeight+(600*slideOffset)
                }

//                Log.e("parentHeight", parentHeight.toString() )
//                Log.e("parentWidth", parentWidth.toString() )

                // Set the position of your view
                binding.searchWithIngredientsButton.y = desiredPosition.toFloat()
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // Handle state changes if needed

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}