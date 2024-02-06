package com.zahra.yummyrecipes.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zahra.yummyrecipes.adapter.CartAdapter
import com.zahra.yummyrecipes.data.database.entity.ShoppingListEntity
import com.zahra.yummyrecipes.databinding.FragmentCartBinding
import com.zahra.yummyrecipes.utils.isVisible
import com.zahra.yummyrecipes.utils.setupRecyclerview
import com.zahra.yummyrecipes.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : Fragment() {
    //Binding
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var cartAdapter: CartAdapter

    //Other
    private val viewModel: CartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //load Shopping List
            viewModel.readShoppingListData.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    emptyIngredientTxt.isVisible(false, shoppingListRecyclerView)
                    //Data
                    cartAdapter.setData(it)
                    shoppingListRecyclerView.setupRecyclerview(
                        LinearLayoutManager(requireContext()), cartAdapter
                    )
                    //Delete
                    cartAdapter.setonItemClickListener {entity->
                        deleteShoppingListItem(entity)
                    }
                } else {
                    emptyIngredientTxt.isVisible(true, shoppingListRecyclerView)
                }
            }

        }
    }

    private fun deleteShoppingListItem(entity: ShoppingListEntity){
        viewModel.deleteShoppingListItem(entity)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}