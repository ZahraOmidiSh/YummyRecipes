package com.zahra.yummyrecipes.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zahra.yummyrecipes.adapter.AdvancedSearchAdapter
import com.zahra.yummyrecipes.adapter.SearchAdapter
import com.zahra.yummyrecipes.databinding.FragmentSearchBinding
import com.zahra.yummyrecipes.utils.NetworkChecker
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.setupRecyclerview
import com.zahra.yummyrecipes.utils.showSnackBar
import com.zahra.yummyrecipes.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class SearchFragment : Fragment() {
    //Binding
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var advancedSearchAdapter: AdvancedSearchAdapter

    @Inject
    lateinit var searchAdapter: SearchAdapter

    @Inject
    lateinit var networkChecker: NetworkChecker

    //Others
    private lateinit var viewModel: SearchViewModel
    private var isNetworkAvailable by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
    }

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
            //load data
            viewModel.expandedIngredientsList.observe(
                viewLifecycleOwner,
                Observer { expandedIngredients ->
                    Log.e("viewModel2", expandedIngredients.toString())
                    advancedSearchAdapter.setData(expandedIngredients)
                })
            viewAllSearchByIngredients.setOnClickListener {
                val direction = SearchFragmentDirections.actionToSearchAllIngredients("_", -1)
                findNavController().navigate(direction)
            }
            ingredientsList.setupRecyclerview(
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                ),
                advancedSearchAdapter
            )

            //Click on items
            advancedSearchAdapter.setonItemClickListener { ingredientName ->
                val id = viewModel.getIngredientId(ingredientName)
                val action =
                    SearchFragmentDirections.actionToSearchAllIngredients(ingredientName, id)
                findNavController().navigate(action)
            }

            //Check Internet
            lifecycleScope.launch {
                withStarted { }
                networkChecker.checkNetworkAvailability().collect { state ->
                    initInternetLayout(state)
                    isNetworkAvailable = state

                }
            }

            //Search
            searchEdt.addTextChangedListener {
                if (it.toString().length > 2 && isNetworkAvailable) {
                    simpleSearchLay.isVisible = true
                    advancedSearchLay.isVisible = false
                    viewModel.callSearchApi(viewModel.searchQueries(it.toString()))
                } else {
                    simpleSearchLay.isVisible = false
                    advancedSearchLay.isVisible = true
                }
            }
        }
        //Show data
        loadRecentData()
    }

    private fun loadRecentData() {
        binding.apply {
            viewModel.searchData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        advancedSearchLay.isVisible = false
                        simpleSearchList.showShimmer()
                    }

                    is NetworkRequest.Success -> {
                        advancedSearchLay.isVisible = false
                        simpleSearchList.hideShimmer()
                        response.data.let { data ->
                            if (data?.results!!.isNotEmpty()) {
                                searchAdapter.setData(data.results)
                                initSearchListRecycler()
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        advancedSearchLay.isVisible = false
                        simpleSearchList.hideShimmer()
                        binding.root.showSnackBar(response.message!!)
                    }

                    else -> {}
                }

            }
        }

    }

    private fun initSearchListRecycler() {
        binding.simpleSearchList.apply {
            layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = searchAdapter
        }

        //Click
        searchAdapter.setonItemClickListener {
            goToDetailPage(it)

        }
    }

    private fun goToDetailPage(id: Int) {
        val action = SearchFragmentDirections.actionToDetail(id)
        findNavController().navigate(action)
    }

    private fun initInternetLayout(isConnected: Boolean) {
        binding.internetLay.isVisible = isConnected.not()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}