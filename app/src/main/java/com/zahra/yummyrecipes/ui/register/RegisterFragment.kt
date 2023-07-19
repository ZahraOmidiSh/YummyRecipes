package com.zahra.yummyrecipes.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import com.zahra.yummyrecipes.databinding.FragmentRegisterBinding
import com.zahra.yummyrecipes.models.register.BodyRegister
import com.zahra.yummyrecipes.utils.NetworkChecker
import com.zahra.yummyrecipes.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    //Binding
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var body:BodyRegister

    @Inject
    lateinit var networkChecker: NetworkChecker

    //Other
    private val viewModel: RegisterViewModel by viewModels()
    private var email = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Init views
        binding.apply {
            eMailEdt.addTextChangedListener {
                eMailEdt.error = null
            }
            //Register Btn Click
            registerButton.setOnClickListener {
                val firstname = firstNameEdt.text.toString()
                val lastname = lastNameEdt.text.toString()
                val username = userNameEdt.text.toString()
                val email = eMailEdt.text.toString()
                if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || email.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Please fill all the boxes!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (!email.contains("@") || !email.contains(".com")) {
                        eMailEdt.error = "Email is not valid"
                    } else {
                        //Body
                        body.email = email
                        body.firstName = firstname
                        body.lastName = lastname
                        body.username = username
                        //CheckNetwork
//                            lifecycleScope.launch {
//                                withStarted {
//                                    networkChecker.checkNetworkAvailability().collect {state ->
//                                        if(state){
//
//                                }
//
//                            }
//
//                        }
//                    }
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