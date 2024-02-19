package com.zahra.yummyrecipes.ui.viewmore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zahra.yummyrecipes.databinding.FragmentViewMoreBinding

class ViewMoreFragment : Fragment() {
    //Binding
    private var _binding: FragmentViewMoreBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewMoreBinding.inflate(layoutInflater)
        return binding.root
    }

}