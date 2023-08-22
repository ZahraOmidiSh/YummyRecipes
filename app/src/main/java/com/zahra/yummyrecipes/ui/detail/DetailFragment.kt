package com.zahra.yummyrecipes.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.request.CachePolicy
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.R.color.congo_pink
import com.zahra.yummyrecipes.adapter.EquipmentsAdapter
import com.zahra.yummyrecipes.adapter.IngredientsAdapter
import com.zahra.yummyrecipes.adapter.InstructionsStepsAdapter
import com.zahra.yummyrecipes.databinding.FragmentDetailBinding
import com.zahra.yummyrecipes.models.detail.ResponseDetail
import com.zahra.yummyrecipes.models.detail.ResponseDetail.ExtendedIngredient
import com.zahra.yummyrecipes.utils.Constants.MY_API_KEY
import com.zahra.yummyrecipes.utils.Constants.NEW_IMAGE_SIZE
import com.zahra.yummyrecipes.utils.Constants.OLD_IMAGE_SIZE
import com.zahra.yummyrecipes.utils.NetworkChecker
import com.zahra.yummyrecipes.utils.NetworkRequest
import com.zahra.yummyrecipes.utils.isVisible
import com.zahra.yummyrecipes.utils.minToHour
import com.zahra.yummyrecipes.utils.setupRecyclerview
import com.zahra.yummyrecipes.utils.showSnackBar
import com.zahra.yummyrecipes.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {
    //Binding
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var instructionsStepsAdapter: InstructionsStepsAdapter

    @Inject
    lateinit var ingredientsAdapter: IngredientsAdapter

    @Inject
    lateinit var equipmentsAdapter: EquipmentsAdapter

    @Inject
    lateinit var networkChecker: NetworkChecker

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
        //Load data
        loadDetailDataFromApi()
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

    @SuppressLint("SetTextI18n")
    private fun initViewsWithData(data:ResponseDetail){
        binding.apply {
            //Image
            val imageSplit = data.image!!.split("-")
            val imageSize = imageSplit[1].replace(OLD_IMAGE_SIZE, NEW_IMAGE_SIZE)
            coverImg.load("${imageSplit[0]}-$imageSize") {
                crossfade(true)
                crossfade(800)
                memoryCachePolicy(CachePolicy.ENABLED)
                error(R.drawable.salad)
            }
            //Text
            heartTxt.text= data.aggregateLikes.toString()
            timeTxt.text= data.readyInMinutes!!.minToHour()
            foodNameTxt.text= data.title
            servingTxt.text= "Servings: ${data.servings.toString()}"
            pricePerServingTxt.text= "Price Per Serving: ${data.pricePerServing.toString()} $"
            //Ingredient
            ingredientsCount.text= "${data.extendedIngredients!!.size} items"
            initIngredientsList(data.extendedIngredients.toMutableList())
            instructionCount.text= "${data.analyzedInstructions!!.size} steps"
            //Diets
            setupChip(data.diets!!.toMutableList(),dietsChipGroup)

        }

    }

    private fun initIngredientsList(list:MutableList<ExtendedIngredient>){
        if(list.isNotEmpty()){
            ingredientsAdapter.setData(list)
            binding.ingredientsList.setupRecyclerview(
                LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL,false),
                ingredientsAdapter
            )
        }
    }

    private fun setupChip(list: MutableList<String> , view:ChipGroup){
        list.forEach {
            val chip = Chip(requireContext())
            val drawable = ChipDrawable.createFromAttributes(requireContext(),null,0,R.style.DietChip)
            chip.setChipDrawable(drawable)
            chip.setTextColor(ContextCompat.getColor(requireContext(),R.color.congo_pink))
            chip.text=it
            view.addView(chip)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}