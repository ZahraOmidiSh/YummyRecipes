package com.zahra.yummyrecipes.ui.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.databinding.FragmentRecipeBinding
import com.zahra.yummyrecipes.utils.Constants.MY_QUOTES_API_KEY
import com.zahra.yummyrecipes.viewmodel.RecipeViewModel
import com.zahra.yummyrecipes.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
        //Show username
        lifecycleScope.launchWhenCreated { showUsername() }

    }


    @SuppressLint("SetTextI18n")
    suspend fun showUsername(){
        registerViewModel.readData.collect{
            binding.usernameTxt.text =
                "${getGreeting()},${it.username} ${getEmojiByUnicode()}"

        }
    }

    private fun getGreeting():String{
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        var greeting=""

        when (hour) {
            in 7..12 -> {
                greeting = getString(R.string.goodMorning)
            }

            in 12..17 -> {
                greeting= getString(R.string.goodAfternoon)
            }

            in 17..20 -> {
                greeting= getString(R.string.goodEvening)
            }

            else -> {
                greeting= getString(R.string.goodNight)
            }
        }
        return greeting
    }
    private fun getEmojiByUnicode():String {
        return String(Character.toChars(0x1f44b))
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