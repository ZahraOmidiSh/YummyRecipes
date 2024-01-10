package com.zahra.yummyrecipes.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.data.database.entity.MealPlannerEntity
import com.zahra.yummyrecipes.databinding.ItemFavoriteBinding
import com.zahra.yummyrecipes.utils.BaseDiffUtils
import com.zahra.yummyrecipes.utils.Constants.NEW_IMAGE_SIZE
import com.zahra.yummyrecipes.utils.Constants.OLD_IMAGE_SIZE
import com.zahra.yummyrecipes.utils.minToHour
import javax.inject.Inject

class MealPlannerAdapter @Inject constructor() :
    RecyclerView.Adapter<MealPlannerAdapter.ViewHolder>() {
    private lateinit var binding: ItemFavoriteBinding
    private var items = emptyList<MealPlannerEntity>()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.initAnimation()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.clearAnimation()
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: MealPlannerEntity) {
            binding.apply {
                item.result.let { result ->
                    //Text
                    suggestedTitleTxt.text = result.title
                    suggestedTimeTxt.text = result.readyInMinutes!!.minToHour()
                    suggestedHeartTxt.text = result.aggregateLikes.toString()
                    //Image
                    val imageSize = result.image!!.replace(OLD_IMAGE_SIZE, NEW_IMAGE_SIZE)
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

        fun initAnimation() {
            binding.root.animation = AnimationUtils.loadAnimation(context, R.anim.item_anim)
        }

        fun clearAnimation() {
            binding.root.clearAnimation()
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