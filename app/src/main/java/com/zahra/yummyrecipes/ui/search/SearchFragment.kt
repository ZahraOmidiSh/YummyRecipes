package com.zahra.yummyrecipes.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zahra.yummyrecipes.adapter.AdvancedSearchAdapter
import com.zahra.yummyrecipes.adapter.FavoriteAdapter
import com.zahra.yummyrecipes.databinding.FragmentSearchBinding
import com.zahra.yummyrecipes.models.search.IngredientsModel
import com.zahra.yummyrecipes.utils.NetworkChecker
import com.zahra.yummyrecipes.utils.setupRecyclerview
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
    lateinit var favoriteAdapter: FavoriteAdapter

    @Inject
    lateinit var networkChecker: NetworkChecker

    //Others
    private val viewModel: SearchViewModel by viewModels()
    private var isNetworkAvailable by Delegates.notNull<Boolean>()
    private val searchIngredientsList: MutableList<IngredientsModel> = mutableListOf()

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

            /*Ingredients*/
            //load data
            viewModel.loadLimitIngredientsList()
            viewModel.limitIngredientsList.observe(viewLifecycleOwner) {
                if (searchIngredientsList.size == 0) {
                    searchIngredientsList.clear()
                    searchIngredientsList.addAll(it)
                    advancedSearchAdapter.setData(searchIngredientsList)
                }
                //RecyclerView setup
                ingredientsList.setupRecyclerview(
                    LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    ),
                    advancedSearchAdapter
                )
                viewAllSearchByIngredients.setOnClickListener {
                    val direction = SearchFragmentDirections.actionToSearchAllIngredients("_")
                    findNavController().navigate(direction)
                }

                //Click on items
                advancedSearchAdapter.setonItemClickListener {ingredientName ->
                    val action = SearchFragmentDirections.actionToSearchAllIngredients(ingredientName)
                    findNavController().navigate(action)
                }

            }

            //Check Internet
            lifecycleScope.launch {
                withStarted {  }
                networkChecker.checkNetworkAvailability().collect{ state ->
                    initInternetLayout(state)
                    isNetworkAvailable=state

                }
            }

            //Search
            searchEdt.addTextChangedListener {
                if(it.toString().length >2 && isNetworkAvailable){
                    viewModel.callSearchApi(viewModel.searchQueries(it.toString()))
                }
            }
        }

        //Show data

    }

    private fun initInternetLayout(isConnected: Boolean) {
        binding.internetLay.isVisible = isConnected.not()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}