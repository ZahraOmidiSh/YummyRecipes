package com.zahra.yummyrecipes.ui.detail

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.request.CachePolicy
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.adapter.EquipmentsAdapter
import com.zahra.yummyrecipes.adapter.IngredientsAdapter
import com.zahra.yummyrecipes.adapter.InstructionsStepsAdapter
import com.zahra.yummyrecipes.adapter.SimilarAdapter
import com.zahra.yummyrecipes.data.database.entity.FavoriteEntity
import com.zahra.yummyrecipes.data.database.entity.ShoppingListEntity
import com.zahra.yummyrecipes.databinding.FragmentDetailBinding
import com.zahra.yummyrecipes.models.detail.ResponseDetail
import com.zahra.yummyrecipes.models.detail.ResponseDetail.AnalyzedInstruction.Step
import com.zahra.yummyrecipes.models.detail.ResponseDetail.AnalyzedInstruction.Step.Equipment
import com.zahra.yummyrecipes.models.detail.ResponseDetail.ExtendedIngredient
import com.zahra.yummyrecipes.models.detail.ResponseSimilar
import com.zahra.yummyrecipes.ui.recipe.RecipeFragmentDirections
import com.zahra.yummyrecipes.utils.*
import com.zahra.yummyrecipes.utils.Constants.NEW_IMAGE_SIZE
import com.zahra.yummyrecipes.utils.Constants.OLD_IMAGE_SIZE
import com.zahra.yummyrecipes.utils.Constants.setAPIKEY
import com.zahra.yummyrecipes.viewmodel.DetailViewModel
import com.zahra.yummyrecipes.viewmodel.ShowAddViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    lateinit var similarAdapter: SimilarAdapter

    @Inject
    lateinit var networkChecker: NetworkChecker

    //Other
    private lateinit var showAddViewModel: ShowAddViewModel
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()
    private var recipeId = 0
    private val TAG = "Detail"
    private var isExistsCache = false
    private var isExistsFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showAddViewModel = ViewModelProvider(requireActivity())[ShowAddViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Args
        args.let {
            recipeId = args.recipeID
            //Call api
            if (recipeId > 0) {
                checkExistsDetailInCache(recipeId)
            }
        }
        //InitViews
        binding.apply {
            //Back
            backImg.setOnClickListener { findNavController().popBackStack() }

            //Add To Meal Planner
            mealPlannerImg.setOnClickListener {
                showAddViewModel.setShowAddFlag(1)
                val direction = DetailFragmentDirections.actionToMealPlanner(recipeId)
                findNavController().navigate(direction)
            }

            watchTutorialCardLay.setOnClickListener {
                Toast.makeText(requireContext(), "Please upgrade to Premium to use this feature!", Toast.LENGTH_SHORT).show()
            }

        }
        //Check Internet
        lifecycleScope.launch {
            withStarted { }
            networkChecker.checkNetworkAvailability().collect() { state ->
                delay(200)
                if (isExistsCache.not()) {
                    initInternetLayout(state)
                    if (state) {
                        loadDetailDataFromApi()
                    }
                }
                //Similar
                if (state) {
                    viewModel.callSimilarApi(recipeId, setAPIKEY())
                    binding.similarTitle.isVisible = true
                }
            }
        }
        //Load data
        loadSimilarData()


    }


    private fun loadDetailDataFromApi() {
        viewModel.callDetailApi(recipeId, setAPIKEY())
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

    private fun checkExistsDetailInCache(id: Int) {
        viewModel.existsDetail(id)
        //Load
        viewModel.existsDetailData.observe(viewLifecycleOwner) {
            isExistsCache = it
            if (it) {
                loadDetailDataFromDb()
                binding.contentLay.isVisible = true
            }
        }
    }

    private fun loadDetailDataFromDb() {
        viewModel.readDetailFromDb(recipeId).observe(viewLifecycleOwner) {
            initViewsWithData(it.result)
        }
    }

    private fun loadSimilarData() {
        binding.apply {
            viewModel.similarData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        similarList.showShimmer()
                    }

                    is NetworkRequest.Success -> {
                        similarList.hideShimmer()
                        response.data?.let { data ->
                            initSimilarData(data)
                        }
                    }

                    is NetworkRequest.Error -> {
                        similarList.hideShimmer()
                        binding.root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }

    private fun initSimilarData(list: MutableList<ResponseSimilar.ResponseSimilarItem>) {
        similarAdapter.setData(list)
        binding.similarList.setupRecyclerview(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            similarAdapter
        )
        //click
        similarAdapter.setonItemClickListener {
            val action = RecipeFragmentDirections.actionToDetail(it)
            findNavController().navigate(action)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViewsWithData(data: ResponseDetail) {
        binding.apply {
            //Favorite
            viewModel.existsFavorite(data.id!!)
            checkExistFavorite()
            //Click favorite
            collectionImg.setOnClickListener {
                if (isExistsFavorite) {
                    deleteFavorite(data)
                } else {
                    saveFavorite(data)
                }
            }
            //Image
            val imageSplit = data.image!!.split("-")
            val imageSize = imageSplit[1].replace(OLD_IMAGE_SIZE, NEW_IMAGE_SIZE)
            coverImg.load("${imageSplit[0]}-$imageSize") {
                crossfade(true)
                crossfade(800)
                memoryCachePolicy(CachePolicy.ENABLED)
                error(R.drawable.salad)
            }
            //Source
            data.sourceUrl?.let { source ->
                sourceImg.isVisible = true
                sourceImg.setOnClickListener {
                    val direction = DetailFragmentDirections.actionToWebView(source)
                    findNavController().navigate(direction)
                }

            }
            Log.i("mahdis", "initViewsWithData: ${data.nutrition}")
            //Text
            heartTxt.text = data.aggregateLikes.toString()
            calorieTxt.text = data.nutrition?.nutrients?.get(0)?.amount.toString() + " Kcal"
            timeTxt.text = data.readyInMinutes!!.minToHour()
            foodNameTxt.text = data.title
            servingTxt.text = "Servings: ${data.servings.toString()}"
            pricePerServingTxt.text = "Price Per Serving: ${data.pricePerServing.toString()} $"
            ingredientsCount.text = "${data.extendedIngredients!!.size} items"
            initIngredientsList(data.extendedIngredients.toMutableList())
            data.analyzedInstructions?.let {
                if (it.isNotEmpty()) {
                    it[0]?.let { step ->
                        if (step.steps!!.isNotEmpty()) {
                            //Equipment
                            equipmentTitle.isVisible = true
                            equipmentCount.isVisible = true
                            equipmentsList.isVisible = true
                            line4.isVisible = true
                            equipmentCount.text =
                                "${step.steps[0].equipment!!.size} items"
                            initEquipmentsList(step.steps[0].equipment!!.toMutableList())
                            equipmentTitle.isVisible = true


                            //Steps
                            cookingTitle.isVisible = true
                            instructionCount.isVisible = true
                            cookingInstructionsList.isVisible = true
                            instructionCount.text = "${step.steps.size} steps"
                            initInstructionStepList(step.steps.toMutableList())


                        }
                    }
                }
            }

            //Diets
            setupChip(data.diets!!.toMutableList(), dietsChipGroup)
            //Nutrient
                carbAmount.text = data.nutrition?.nutrients?.get(2)?.amount.toString() + " g"
            proteinAmount.text = data.nutrition?.nutrients?.get(3)?.amount.toString() + " g"
            fatAmount.text = data.nutrition?.nutrients?.get(4)?.amount.toString() + " g"

        }

    }

    private fun initInstructionStepList(list: MutableList<Step>) {
        if (list.isNotEmpty()) {
            instructionsStepsAdapter.setData(list)
            binding.cookingInstructionsList.setupRecyclerview(
                LinearLayoutManager(requireContext()), instructionsStepsAdapter
            )
        }


    }

    private fun initIngredientsList(list: MutableList<ExtendedIngredient>) {
        if (list.isNotEmpty()) {
            ingredientsAdapter.setData(list)
            binding.ingredientsList.setupRecyclerview(
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                ingredientsAdapter
            )
            //click
            ingredientsAdapter.setAnotherCustomClickListener {
                saveToShoppingList(it)
            }
        }
    }

    private fun initEquipmentsList(list: MutableList<Equipment>) {
        binding.equipmentTitle.isVisible = list.isNotEmpty()
        if (list.isNotEmpty()) {
            equipmentsAdapter.setData(list)
            binding.equipmentsList.setupRecyclerview(
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                equipmentsAdapter
            )
        }
    }

    private fun setupChip(list: MutableList<String>, view: ChipGroup) {
        binding.dietTitle.isVisible = list.isNotEmpty()
        binding.dietsChipGroup.removeAllViews()
        val list2 = list.distinct()
        list2.forEach {
            val chip = Chip(requireContext())
            val drawable =
                ChipDrawable.createFromAttributes(requireContext(), null, 0, R.style.DietChip)
            chip.setChipDrawable(drawable)
            when (it) {
                Constants.GLUTEN_FREE -> {
                    chip.chipIcon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.logo_gluten_free)
                }

                Constants.KETOGENIC -> {
                    chip.chipIcon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.logo_ketogenic)
                }

                Constants.VEGETARIAN -> {
                    chip.chipIcon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.logo_vegeterian)
                }

                Constants.LACTO_VEGETARIAN -> {
                    chip.chipIcon =
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.logo_lacto_vegeterian
                        )
                }

                Constants.OVO_VEGETARIAN -> {
                    chip.chipIcon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.logo_ovo_vegeterian)
                }

                Constants.VEGAN -> {
                    chip.chipIcon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.logo_vegan)
                }

                Constants.PESCETARIAN -> {
                    chip.chipIcon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.logo_pescetarian)
                }

                Constants.PALEO -> {
                    chip.chipIcon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.logo_paleo)
                }

                Constants.PRIMAL -> {
                    chip.chipIcon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.logo_paleo)
                }

                Constants.LOW_FODMAP -> {
                    chip.chipIcon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.logo_paleo)
                }

                Constants.WHOLE30 -> {
                    chip.chipIcon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.logo_30)
                }

                Constants.DAIRY_FREE -> {
                    chip.chipIcon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.logo_dairy_free)
                }

                Constants.LACTO_OVO_VEGETARIAN -> {
                    chip.chipIcon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.vegetarian)
                }

                else -> chip.chipIcon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.logo_30)
            }
            chip.text = it
            view.addView(chip)
        }

    }


    private fun initInternetLayout(isConnected: Boolean) {
        binding.internetLay.isVisible = isConnected.not()
    }

    //Favorite
    private fun saveFavorite(data: ResponseDetail) {
        val entity = FavoriteEntity(data.id!!, data)
        viewModel.saveFavorite(entity)
        binding.collectionImg.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.big_foot_feet))
    }

    private fun deleteFavorite(data: ResponseDetail) {
        val entity = FavoriteEntity(data.id!!, data)
        viewModel.deleteFavorite(entity)
        binding.collectionImg.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray))
    }

    private fun checkExistFavorite() {
        viewModel.existsFavoriteData.observe(viewLifecycleOwner) {
            isExistsFavorite = it
            if (it) {
                binding.collectionImg.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.big_foot_feet
                    )
                )
            } else {
                binding.collectionImg.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gray
                    )
                )

            }
        }
    }

    //Favorite
    private fun saveToShoppingList(data: ExtendedIngredient) {
        val image = "${Constants.BASE_URL_IMAGE_INGREDIENTS}${data.image}"
        val entity = ShoppingListEntity(0, data.name!!, data.amount!!, data.unit!!, image)
        viewModel.saveIngredientToShoppingList(entity)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}