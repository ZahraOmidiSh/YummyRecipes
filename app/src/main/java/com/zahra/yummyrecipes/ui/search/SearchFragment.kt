package com.zahra.yummyrecipes.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.zahra.yummyrecipes.adapter.SearchIngredientsAdapter
import com.zahra.yummyrecipes.databinding.FragmentSearchBinding
import com.zahra.yummyrecipes.models.search.IngredientsModel
import com.zahra.yummyrecipes.utils.setupRecyclerview
import com.zahra.yummyrecipes.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    //Binding
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!


    @Inject
    private lateinit var searchIngredientsAdapter: SearchIngredientsAdapter

    //Others
    private val viewModel:SearchViewModel by viewModels()
    private val searchIngredientsList:MutableList<IngredientsModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            viewModel.loadIngredientsList()
            viewModel.IngredientsList.observe(viewLifecycleOwner){
                searchIngredientsList.addAll(it)
                ingredientsList.setupRecyclerview(
                    LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false),
                    searchIngredientsAdapter)
            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}