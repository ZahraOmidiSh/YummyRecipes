package com.zahra.yummyrecipes.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.databinding.ItemIngredientsBinding
import com.zahra.yummyrecipes.models.detail.ResponseDetail.ExtendedIngredient
import com.zahra.yummyrecipes.utils.BaseDiffUtils
import com.zahra.yummyrecipes.utils.Constants.BASE_URL_IMAGE_INGREDIENTS
import javax.inject.Inject

class IngredientsAdapter @Inject constructor() :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    private lateinit var binding: ItemIngredientsBinding
    private var items = emptyList<ExtendedIngredient>()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()
    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ExtendedIngredient) {
            binding.apply {
                //Text
                ingredientNameTxt.text = item.name
                ingredientAmountTxt.text = "${item.amount} ${item.unit}"
                //Image
                val image = "${BASE_URL_IMAGE_INGREDIENTS}${item.image}"
                ingredientImg.load(image) {
                    crossfade(true)
                    crossfade(500)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    error(R.drawable.bg_rounded_white)
                }
                addToIngredientsButton.setOnClickListener { view ->
                    customClickListener?.let {
                        it(item)
                        if (isDarkTheme()) {
                            view.setBackgroundResource(R.drawable.bg_below_rounded_deselected_dark_ingredients_add_button)
                        } else {
                            view.setBackgroundResource(R.drawable.bg_below_rounded_deselected_light_ingredients_add_button)
                        }
                        view.isEnabled = false
                    }
                }
            }
        }
    }

    var customClickListener: ((ExtendedIngredient) -> Unit)? = null

    fun setAnotherCustomClickListener(listener: (ExtendedIngredient) -> Unit) {
        customClickListener = listener
    }

    fun isDarkTheme(): Boolean {
        return context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    fun setData(data: List<ExtendedIngredient>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}