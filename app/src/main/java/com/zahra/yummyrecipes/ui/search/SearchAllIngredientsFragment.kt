package com.zahra.yummyrecipes.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zahra.yummyrecipes.adapter.AdvancedAllSearchAdapter
import com.zahra.yummyrecipes.databinding.FragmentSearchAllIngredientsBinding
import com.zahra.yummyrecipes.models.search.IngredientsModel
import com.zahra.yummyrecipes.utils.setupRecyclerview
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
    private val viewModel: SearchViewModel by viewModels()
    private val args: SearchAllIngredientsFragmentArgs by navArgs()
    private val searchIngredientsList: MutableList<IngredientsModel> = mutableListOf()
    private var ingredientName = "_"
    private lateinit var selectedIngredientsList: List<IngredientsModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadExpandedIngredientsList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchAllIngredientsBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Args
        args.let {
            if (it.ingredientName != "_") {

            }
        }
        //InitViews
        binding.apply {
            //close button
            closeImg.setOnClickListener { findNavController().navigateUp() }
            //load data
            advancedAllSearchAdapter.setSize(24)
            //RecyclerView setup
            expandedList.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 4)
                adapter = advancedAllSearchAdapter
                setHasFixedSize(true)
            }
            viewModel.expandedIngredientsList.observe(viewLifecycleOwner) {
                advancedAllSearchAdapter.setData(it)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}