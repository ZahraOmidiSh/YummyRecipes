package com.zahra.yummyrecipes.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.databinding.ItemIngredientsAllSearchBinding
import com.zahra.yummyrecipes.models.search.IngredientsModel
import com.zahra.yummyrecipes.utils.BaseDiffUtils
import javax.inject.Inject

class AdvancedAllSearchAdapter @Inject constructor() :
    RecyclerView.Adapter<AdvancedAllSearchAdapter.ViewHolder>() {
    //    private lateinit var binding: ItemIngredientsAllSearchBinding
    private var items = mutableListOf<IngredientsModel>()
    private var selectedItems = mutableSetOf<Int>()
    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIngredientsAllSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], selectedItems.contains(position))
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder(private val binding: ItemIngredientsAllSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: IngredientsModel, isSelected: Boolean) {
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
                // Set item color based on selection
                if (isSelected) {
                    root.setBackgroundColor(context.getColor(R.color.big_foot_feet2))
                } else {
                    root.setBackgroundColor(context.getColor(android.R.color.transparent))
                }
                //Click
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        toggleItemSelection(position)
                        onItemClickListener?.let { it(item) }
                    }
                }
            }
        }
    }

    fun toggleItemSelection(position: Int) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position)
        } else {
            selectedItems.add(position)
        }
        notifyItemChanged(position)
    }

    var onItemClickListener: ((IngredientsModel) -> Unit)? = null

    fun setonItemClickListener(listener: (IngredientsModel) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<IngredientsModel>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items.clear()
        items.addAll(data)
        diffUtils.dispatchUpdatesTo(this)
    }
}