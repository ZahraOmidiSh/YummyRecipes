package com.zahra.yummyrecipes.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
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
    private lateinit var binding: ItemIngredientsAllSearchBinding
    private val items = mutableListOf<IngredientsModel>()
    private val selectedItems = HashSet<IngredientsModel>()
    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemIngredientsAllSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder :
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
                if (item.isSelected) {
                    cardLay.setBackgroundResource(R.drawable.bg_rounded_big_foot_feet)
                    root.isEnabled = false
                } else {
                    if (isDarkTheme()) {
                        cardLay.setBackgroundResource(R.drawable.bg_rounded_linen_stroke_dark)
                        root.isEnabled = true
                    } else {
                        cardLay.setBackgroundResource(R.drawable.bg_rounded_linen_stroke_light)
                        root.isEnabled = true
                    }
                }


                //Click
                root.setOnClickListener {
                    onItemClickListener?.let { it(item) }
                }

            }
        }
    }

    var onItemClickListener: ((IngredientsModel) -> Unit)? = null

    fun setonItemClickListener(listener: (IngredientsModel) -> Unit) {
        onItemClickListener = listener
    }

    fun isDarkTheme(): Boolean {
        return context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }


    fun setData(data: List<IngredientsModel>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items.clear()
        items.addAll(data)
        diffUtils.dispatchUpdatesTo(this)
    }
}