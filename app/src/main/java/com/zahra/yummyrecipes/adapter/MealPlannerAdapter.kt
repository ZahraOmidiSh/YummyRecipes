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
import com.zahra.yummyrecipes.databinding.ItemPlannedMealsBinding
import com.zahra.yummyrecipes.databinding.ItemSimilarBinding
import com.zahra.yummyrecipes.utils.BaseDiffUtils
import com.zahra.yummyrecipes.utils.Constants
import com.zahra.yummyrecipes.utils.Constants.NEW_IMAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MealPlannerAdapter @Inject constructor() :
    RecyclerView.Adapter<MealPlannerAdapter.ViewHolder>() {
    private var items = mutableListOf<MealPlannerEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPlannedMealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder(private val binding: ItemPlannedMealsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: MealPlannerEntity) {
            binding.apply {
                item.result.let { result ->
                    //Text
                    similarTitleTxt.text = result.title
                    //Image
                    val imageSize = result.image!!.replace(Constants.OLD_IMAGE_SIZE, NEW_IMAGE_SIZE)
                    similarImg.load(imageSize) {
                        crossfade(true)
                        crossfade(800)
                        memoryCachePolicy(CachePolicy.ENABLED)
                        error(R.drawable.salad)
                    }
                    //Click
                    root.setOnClickListener {
                        var idString = item.id.toString()
                        idString = idString.substring(8)
                        val newId = idString.toInt()

                        onItemClickListener?.invoke(newId)
                    }
                    deleteImg.setOnClickListener {
                        onItemClickListenerForDelete?.invoke(item)
                    }
                }
            }
        }
    }


    var onItemClickListener: ((Int) -> Unit)? = null

    fun setonItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    var onItemClickListenerForDelete: ((MealPlannerEntity) -> Unit)? = null

    fun setonItemClickListenerForDelete(listener: (MealPlannerEntity) -> Unit) {
        onItemClickListenerForDelete = listener
    }

    fun setData(data: List<MealPlannerEntity>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
        diffUtils.dispatchUpdatesTo(this)
    }
}