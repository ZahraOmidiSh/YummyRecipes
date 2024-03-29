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
import com.zahra.yummyrecipes.data.database.entity.ShoppingListEntity
import com.zahra.yummyrecipes.databinding.ItemShoppingListBinding
import com.zahra.yummyrecipes.utils.BaseDiffUtils
import javax.inject.Inject

class CartAdapter @Inject constructor() : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private lateinit var binding: ItemShoppingListBinding
    private var items = emptyList<ShoppingListEntity>()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemShoppingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()
    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ShoppingListEntity) {
            binding.apply {
                item.let { ingredient ->
                    //Text
                    ingredientNameTxt.text = ingredient.name
                    ingredientAmountTxt.text = ingredient.amount.toString()
                    ingredientUnitTxt.text = ingredient.unit
                    //Image
                    ingredientImg.load(ingredient.image) {
                        crossfade(true)
                        crossfade(500)
                        memoryCachePolicy(CachePolicy.ENABLED)
                        error(R.drawable.salad)
                    }
                    //Click
                    deleteImg.setOnClickListener {
                        onItemClickListener?.let {
                            it(item)
                        }
                    }
                }
            }
        }

    }

    var onItemClickListener: ((ShoppingListEntity) -> Unit)? = null

    fun setonItemClickListener(listener: (ShoppingListEntity) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<ShoppingListEntity>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}