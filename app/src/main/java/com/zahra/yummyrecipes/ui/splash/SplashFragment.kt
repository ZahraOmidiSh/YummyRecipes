package com.zahra.yummyrecipes.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.navigation.fragment.findNavController
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.databinding.FragmentSplashBinding
import com.zahra.yummyrecipes.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashFragment : Fragment() {
    //Binding
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    //Other
    private val viewModel: RegisterViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Application Version
            val manager = context?.packageManager
            val info = context?.packageName?.let {
                manager?.getPackageInfo(
                    it, 0
                )
            }
            val versionName = info?.versionName
            versionTxt.text = "version : $versionName"
            //Auto navigate
            lifecycleScope.launch {
                withStarted {}
                delay(3000)
                //Check user info
                viewModel.readData.asLiveData().observe(viewLifecycleOwner){
                    findNavController().popBackStack(R.id.splashFragment,true)
                    if(it.username.isNotEmpty()){
                        findNavController().navigate(R.id.actionToRecipe)
                    }else{
                        findNavController().navigate(R.id.actionToRegister)
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