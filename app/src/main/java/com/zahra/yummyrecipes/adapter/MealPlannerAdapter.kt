package com.zahra.yummyrecipes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.data.database.entity.MealPlannerEntity
import com.zahra.yummyrecipes.databinding.ItemSimilarBinding
import com.zahra.yummyrecipes.utils.BaseDiffUtils
import com.zahra.yummyrecipes.utils.Constants
import com.zahra.yummyrecipes.utils.Constants.NEW_IMAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MealPlannerAdapter @Inject constructor() :
    RecyclerView.Adapter<MealPlannerAdapter.ViewHolder>() {
    private lateinit var binding: ItemSimilarBinding
    private var items = emptyList<MealPlannerEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemSimilarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: MealPlannerEntity) {
            binding.apply {
                //Text
                similarTitleTxt.text = item.result.title
                //Image
                val image = "${Constants.BASE_URL_IMAGE_RECIPES}${item.id}-${NEW_IMAGE_SIZE}"
                similarImg.load(image) {
                    crossfade(true)
                    crossfade(800)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    error(R.drawable.salad)
                }
                //Click
                root.setOnClickListener {
//                        onItemClickListener?.let {
//                            it(item.id!!) .
                }

            }
        }
    }


    var onItemClickListener: ((Int) -> Unit)? = null

    fun setonItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<MealPlannerEntity>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}