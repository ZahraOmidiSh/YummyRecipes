package com.zahra.yummyrecipes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.databinding.ItemEquipmentBinding
import com.zahra.yummyrecipes.models.detail.ResponseDetail.AnalyzedInstruction.Step.Equipment
import com.zahra.yummyrecipes.utils.BaseDiffUtils
import com.zahra.yummyrecipes.utils.Constants.BASE_URL_IMAGE_EQUIPMENT
import javax.inject.Inject

class EquipmentsAdapter @Inject constructor() :
    RecyclerView.Adapter<EquipmentsAdapter.ViewHolder>() {
    private lateinit var binding: ItemEquipmentBinding
    private var items = emptyList<Equipment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemEquipmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()
    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Equipment) {
            binding.apply {
                //Text
                equipmentNameTxt.text = item.name
                //Image
                val image = "$BASE_URL_IMAGE_EQUIPMENT${item.image}"
                equipmentImg.load(image) {
                    crossfade(true)
                    crossfade(500)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    error(R.drawable.bg_rounded_white)
                }
            }
        }
    }


    fun setData(data: List<Equipment>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}