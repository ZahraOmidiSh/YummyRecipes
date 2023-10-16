package com.zahra.yummyrecipes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.databinding.ItemIngredientsSearchBinding
import com.zahra.yummyrecipes.models.search.IngredientsModel
import com.zahra.yummyrecipes.utils.BaseDiffUtils
import javax.inject.Inject

class SelectedSearchAdapter @Inject constructor() :
    RecyclerView.Adapter<SelectedSearchAdapter.ViewHolder>() {
    private var items = mutableListOf<IngredientsModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIngredientsSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder(private val binding: ItemIngredientsSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: IngredientsModel) {
            binding.apply {
                //closeImg Visibility
                closeImg.isVisible=true
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

                closeImg.setOnClickListener{
                    onItemClickListener?.let { it(item.ingredientsName) }
                }

                //Click
//                cardLay.setOnClickListener {
//                    item.isSelected=true
//                    onItemClickListener?.let { it(item.ingredientsName) }
//
//                }
            }
        }
    }

    var onItemClickListener: ((String) -> Unit)? = null

    fun setonItemClickListener(listener: (String) -> Unit) {
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