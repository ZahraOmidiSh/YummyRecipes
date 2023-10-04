package com.zahra.yummyrecipes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.databinding.ItemIngredientsAllSearchBinding
import com.zahra.yummyrecipes.models.search.IngredientsModel
import com.zahra.yummyrecipes.utils.BaseDiffUtils
import com.zahra.yummyrecipes.viewmodel.SearchViewModel
import javax.inject.Inject

class AdvancedAllSearchAdapter @Inject constructor() :
    RecyclerView.Adapter<AdvancedAllSearchAdapter.ViewHolder>() {
    private var ingredientItems: List<IngredientsModel> = emptyList()
    var tracker: SelectionTracker<Long>? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIngredientsAllSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ingredientItems[position])

    }

    override fun getItemCount() = ingredientItems.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    fun setItems(items: List<IngredientsModel>) {
        ingredientItems = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemIngredientsAllSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: IngredientsModel) {
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

                // Set the item's selected state based on the tracker
                tracker?.let {
                    root.isActivated = it.isSelected(adapterPosition.toLong())
                }

                // Handle item click to toggle selection
                binding.root.setOnClickListener {
                    tracker?.let { tracker ->
                        if (tracker.isSelected(adapterPosition.toLong())) {
                            tracker.deselect(adapterPosition.toLong())
                        } else {
                            tracker.select(adapterPosition.toLong())
                        }
                    }
                }
            }
        }
    }


    fun setData(data: List<IngredientsModel>) {
        val adapterDiffUtils = BaseDiffUtils(ingredientItems, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
//        ingredientItems.clear()
//        ingredientItems.addAll(data)
        ingredientItems = data
        diffUtils.dispatchUpdatesTo(this)
    }


}