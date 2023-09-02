package com.zahra.yummyrecipes.ui.detail

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.request.CachePolicy
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.adapter.EquipmentsAdapter
import com.zahra.yummyrecipes.adapter.IngredientsAdapter
import com.zahra.yummyrecipes.adapter.InstructionsStepsAdapter
import com.zahra.yummyrecipes.adapter.SimilarAdapter
import com.zahra.yummyrecipes.databinding.FragmentDetailBinding
import com.zahra.yummyrecipes.models.detail.ResponseDetail
import com.zahra.yummyrecipes.models.detail.ResponseDetail.AnalyzedInstruction.Step
import com.zahra.yummyrecipes.models.detail.ResponseDetail.AnalyzedInstruction.Step.Equipment
import com.zahra.yummyrecipes.models.detail.ResponseDetail.ExtendedIngredient
import com.zahra.yummyrecipes.models.detail.ResponseSimilar
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
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()
    private var recipeId = 0
    private val TAG = "Detail"
    private var isExistsCache = false


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
                if(state){
                    viewModel.callSimilarApi(recipeId, MY_API_KEY)
                }
            }
        }
        //Load data
        loadSimilarData()


    }


    private fun loadDetailDataFromApi() {
        viewModel.callDetailApi(recipeId, MY_API_KEY)
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
    }

    @SuppressLint("SetTextI18n")
    private fun initViewsWithData(data: ResponseDetail) {
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
            //Source
            data.sourceUrl?.let { source ->
                sourceImg.isVisible = true
                sourceImg.setOnClickListener {
                    val direction = DetailFragmentDirections.actionToWebView(source)
                    findNavController().navigate(direction)
                }

            }
            //Text
            heartTxt.text = data.aggregateLikes.toString()
            calorieTxt.text = data.nutrition?.nutrients?.get(0)?.amount.toString()+" Kcal"
            timeTxt.text = data.readyInMinutes!!.minToHour()
            foodNameTxt.text = data.title
            servingTxt.text = "Servings: ${data.servings.toString()}"
            pricePerServingTxt.text = "Price Per Serving: ${data.pricePerServing.toString()} $"
            //Nutrient
            fatAmount.text = "Fat: ${data.nutrition?.nutrients?.get(1)?.amount.toString()} g"
            carbAmount.text =
                "Carbohydrate: ${data.nutrition?.nutrients?.get(3)?.amount.toString()} g"
            proteinAmount.text =
                "Protein: ${data.nutrition?.nutrients?.get(8)?.amount.toString()} g"
            //Ingredient
            ingredientsCount.text = "${data.extendedIngredients!!.size} items"
            initIngredientsList(data.extendedIngredients.toMutableList())
            //Equipment
            equipmentCount.text =
                "${data.analyzedInstructions!![0].steps!![0].equipment!!.size} items"
            initEquipmentsList(data.analyzedInstructions[0].steps!![0].equipment!!.toMutableList())
            //Steps
            instructionCount.text = "${data.analyzedInstructions[0].steps!!.size} steps"
            initInstructionStepList(data.analyzedInstructions[0].steps!!.toMutableList())
            //Diets
            setupChip(data.diets!!.toMutableList(), dietsChipGroup)
            //Nutrient
            initNutrientChart(data)

        }

    }
    private fun initNutrientChart(data:ResponseDetail){
        binding.apply {

            // on below line we are setting user percent value,
            // setting description as enabled and offset for pie chart
            pieChart.setUsePercentValues(true)
            pieChart.getDescription().setEnabled(false)
            pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

            // on below line we are setting drag for our pie chart
            pieChart.setDragDecelerationFrictionCoef(0.95f)

            // on below line we are setting hole
            // and hole color for pie chart
            pieChart.setDrawHoleEnabled(true)
            pieChart.setHoleColor(Color.WHITE)

            // on below line we are setting circle color and alpha
            pieChart.setTransparentCircleColor(Color.WHITE)
            pieChart.setTransparentCircleAlpha(110)

            // on  below line we are setting hole radius
            pieChart.setHoleRadius(58f)
            pieChart.setTransparentCircleRadius(61f)

            // on below line we are setting center text
            pieChart.setDrawCenterText(true)

            // on below line we are setting
            // rotation for our pie chart
            pieChart.setRotationAngle(0f)

            // enable rotation of the pieChart by touch
            pieChart.setRotationEnabled(true)
            pieChart.setHighlightPerTapEnabled(true)

            // on below line we are setting animation for our pie chart
            pieChart.animateY(1400, Easing.EaseInOutQuad)

            // on below line we are disabling our legend for pie chart
            pieChart.legend.isEnabled = false
            pieChart.setEntryLabelColor(Color.WHITE)
            pieChart.setEntryLabelTextSize(12f)

            // on below line we are creating array list and
            // adding data to it to display in pie chart
            val entries: ArrayList<PieEntry> = ArrayList()
            entries.add(PieEntry(30f))
            entries.add(PieEntry(12f))
            entries.add(PieEntry(8f))

            // on below line we are setting pie data set
            val dataSet = PieDataSet(entries, "Mobile OS")

            // on below line we are setting icons.
            dataSet.setDrawIcons(false)

            // on below line we are setting slice for pie
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0f, 40f)
            dataSet.selectionShift = 5f

            // add a lot of colors to list
            val colors: ArrayList<Int> = ArrayList()
            colors.add(resources.getColor(R.color.congo_pink))
            colors.add(resources.getColor(R.color.gray))
            colors.add(resources.getColor(R.color.red))

            // on below line we are setting colors.
            dataSet.colors = colors

            // on below line we are setting pie data set
            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(15f)
            data.setValueTypeface(Typeface.DEFAULT_BOLD)
            data.setValueTextColor(Color.WHITE)
            pieChart.setData(data)

            // undo all highlights
            pieChart.highlightValues(null)

            // loading chart
            pieChart.invalidate()


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
        }
    }

    private fun initEquipmentsList(list: MutableList<Equipment>) {
        if (list.isNotEmpty()) {
            equipmentsAdapter.setData(list)
            binding.equipmentsList.setupRecyclerview(
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                equipmentsAdapter
            )
        }
    }

    private fun setupChip(list: MutableList<String>, view: ChipGroup) {
        list.forEach {
            val chip = Chip(requireContext())
            val drawable =
                ChipDrawable.createFromAttributes(requireContext(), null, 0, R.style.DietChip)
            chip.setChipDrawable(drawable)
            chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_person_24)
            chip.text = it
//            chip.setChipIconTintResource( R.color.white)
            view.addView(chip)
        }
    }

    private fun initInternetLayout(isConnected: Boolean) {
        binding.internetLay.isVisible = isConnected.not()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}