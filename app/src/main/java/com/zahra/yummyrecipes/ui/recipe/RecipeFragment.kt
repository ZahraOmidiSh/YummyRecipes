package com.zahra.yummyrecipes.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.zahra.yummyrecipes.databinding.FragmentRecipeBinding
import com.zahra.yummyrecipes.viewmodel.TimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.util.Calendar


@AndroidEntryPoint
class RecipeFragment : Fragment() {
    //Binding
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    //Others
    private val timeViewModel: TimeViewModel by viewModels()

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

//            val calendar = Calendar.getInstance()
//
//            val current = "${calendar.get(Calendar.HOUR_OF_DAY)} : ${calendar.get(Calendar.MINUTE)}"
//            timeTxt.text=current

            timeViewModel.currentTime.observe(viewLifecycleOwner) {
            var currentTime = it.toInt()
                if(currentTime in 7..12){
                    timeTxt.text="GoodMorning"
                }
                else if(currentTime in 12..17){
                    timeTxt.text="GoodAfternoon"
                }
                else if(currentTime in 17..20){
                    timeTxt.text="GoodEvening"
                }
                else {
                    timeTxt.text="GoodNight"
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}