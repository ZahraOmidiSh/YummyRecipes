package com.zahra.yummyrecipes.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.databinding.FragmentRecipeBinding
import com.zahra.yummyrecipes.utils.Constants.MY_QUOTES_API_KEY
import com.zahra.yummyrecipes.viewmodel.RecipeViewModel
import com.zahra.yummyrecipes.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar


@AndroidEntryPoint
class RecipeFragment : Fragment() {
    //Binding
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    //other
    private val registerViewModel: RegisterViewModel by viewModels()
    private val recipeViewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {

//            val current = LocalDateTime.now()

            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)

            /*
            when (hour) {
                in 7..12 -> {
                    timeTxt.text = getString(R.string.goodMorning)
                }

                in 12..17 -> {
                    timeTxt.text = getString(R.string.goodAfternoon)
                }

                in 17..20 -> {
                    timeTxt.text = getString(R.string.goodEvening)
                }

                else -> {
                    timeTxt.text = getString(R.string.goodNight)
                }
            }
            */
            callQuotesApi()

        }

    }

    private fun callQuotesApi(){
        recipeViewModel.callQuoteApi(MY_QUOTES_API_KEY,"food")
        recipeViewModel.quoteData.observe(viewLifecycleOwner) {
            binding.timeTxt.text =it.data.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        registerViewModel.cancelDatastoreStack()
    }
}