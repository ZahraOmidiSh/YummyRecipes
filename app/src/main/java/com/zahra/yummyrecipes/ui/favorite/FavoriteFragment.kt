package com.zahra.yummyrecipes.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zahra.yummyrecipes.adapter.FavoriteAdapter
import com.zahra.yummyrecipes.databinding.FragmentFavoriteBinding
import com.zahra.yummyrecipes.ui.recipe.RecipeFragmentDirections
import com.zahra.yummyrecipes.utils.isVisible
import com.zahra.yummyrecipes.utils.setupRecyclerview
import com.zahra.yummyrecipes.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    //Binding
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter

    //Other
    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Load favorites
            viewModel.readFavoriteData.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    emptyTxt.isVisible(false, favoriteList)
                    //Data
                    favoriteAdapter.setData(it)
                    favoriteList.apply {
                        layoutManager =
                            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        adapter = favoriteAdapter
                    }
                    //Click
                    favoriteAdapter.setonItemClickListener { id ->
                        val action = RecipeFragmentDirections.actionToDetail(id)
                        findNavController().navigate(action)
                    }

                } else {
                    emptyTxt.isVisible(true, favoriteList)
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}