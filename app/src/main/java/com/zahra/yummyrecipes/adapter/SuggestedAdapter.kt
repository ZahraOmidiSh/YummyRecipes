package com.zahra.yummyrecipes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.adapter.SuggestedAdapter.ViewHolder
import com.zahra.yummyrecipes.databinding.ItemSuggestedBinding
import com.zahra.yummyrecipes.models.recipe.ResponseRecipes.Result
import com.zahra.yummyrecipes.utils.BaseDiffUtils
import com.zahra.yummyrecipes.utils.Constants.NEW_IMAGE_SIZE
import com.zahra.yummyrecipes.utils.Constants.OLD_IMAGE_SIZE
import javax.inject.Inject

class SuggestedAdapter @Inject constructor() : RecyclerView.Adapter<ViewHolder>() {
    private lateinit var binding: ItemSuggestedBinding
    private var items = emptyList<Result>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemSuggestedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Result) {
            binding.apply {
                //Text
                suggestedTitleTxt.text = item.title
                suggestedTitleTxt.text = "${item.readyInMinutes} min"
                //Image
                val imageSize = item.image!!.replace(OLD_IMAGE_SIZE, NEW_IMAGE_SIZE)
                suggestedImg.load(imageSize) {
                    crossfade(true)
                    crossfade(800)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    error(R.drawable.salad)
                }
                //Click
                root.setOnClickListener {
                    onItemClickListener?.let { it(item.id!!) }

                }
            }
        }

    }

    var onItemClickListener: ((Int) -> Unit)? = null

    fun setonItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<Result>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}