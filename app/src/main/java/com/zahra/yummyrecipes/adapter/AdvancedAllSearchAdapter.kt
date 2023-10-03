package com.zahra.yummyrecipes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.databinding.ItemIngredientsAllSearchBinding
import com.zahra.yummyrecipes.models.search.IngredientsModel
import com.zahra.yummyrecipes.utils.BaseDiffUtils
import com.zahra.yummyrecipes.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class AdvancedAllSearchAdapter @Inject constructor() :
    RecyclerView.Adapter<AdvancedAllSearchAdapter.ViewHolder>() {
    private var items = mutableListOf<IngredientsModel>()
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIngredientsAllSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position], position)

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()
    inner class ViewHolder(private val binding: ItemIngredientsAllSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: IngredientsModel, position: Int) {
            binding.apply {
                //Text
                ingredientNameTxt.text = item.ingredientsName
                //Image
                val image = item.ingredientsImg
                ingredientImg.load(image) {
                    crossfade(true)
                    crossfade(500)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    error(R.drawable.bg_rounded_white)
                }
                //Item selection
                if (viewModel.selectedItems.contains(position)) {
                    cardLay.setBackgroundResource(R.drawable.bg_rounded_big_foot_feet)
                } else {
                    cardLay.setBackgroundResource(R.drawable.bg_round_pale_pink)
                }
                //Item click listener
                itemView.setOnClickListener {
                    toggleSelection(position)
                }
            }
        }
    }


    fun setData(data: List<IngredientsModel>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items.clear()
        items.addAll(data)
        diffUtils.dispatchUpdatesTo(this)
    }

    private fun toggleSelection(position: Int) {
        if (viewModel.selectedItems.contains(position)) {
            viewModel.selectedItems.remove(position)
        } else {
            viewModel.selectedItems.add(position)
        }
        notifyDataSetChanged()
    }
}