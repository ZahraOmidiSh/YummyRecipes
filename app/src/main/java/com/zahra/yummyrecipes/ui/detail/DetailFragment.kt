package com.zahra.yummyrecipes.ui.detail

import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zahra.yummyrecipes.databinding.FragmentDetailBinding
import com.zahra.yummyrecipes.utils.Constants.MY_API_KEY
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.isVisible
import com.zahra.yummyrecipes.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    //Binding
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    //Other
    private val viewModel:DetailViewModel by viewModels()
    private val args:DetailFragmentArgs by navArgs()
    private var recipeId=0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Args
        args.let {
            recipeId=args.recipeID
            //Call api
            if(recipeId>0){
                viewModel.callDetailApi(recipeId, MY_API_KEY)
            }
        }
        //InitViews
        binding.apply {
            //Back
            backImg.setOnClickListener{findNavController().popBackStack()}

        }
    }

    private fun loadDetailDataFromApi(){
        viewModel.callDetailApi(recipeId , MY_API_KEY)
        binding.apply {
            viewModel.detailData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        loading.isVisible(true, contentLay)

                    }
                    is NetworkRequest.Success -> {
                        loading.isVisible(false, contentLay)
                        response.data?.let { data ->
                            initViewsWithData(data)
                        }
                    }
                    is NetworkRequest.Error -> {
                        loading.isVisible(false, contentLay)
                        binding.root.showSnackBar(response.message!!)
                    }

                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}