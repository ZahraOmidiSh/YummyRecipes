package com.zahra.yummyrecipes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zahra.yummyrecipes.databinding.ItemInstructionStepBinding
import com.zahra.yummyrecipes.models.detail.ResponseDetail.AnalyzedInstruction.Step
import com.zahra.yummyrecipes.utils.BaseDiffUtils
import com.zahra.yummyrecipes.utils.minToHour
import javax.inject.Inject

class InstructionsStepsAdapter @Inject constructor() :
    RecyclerView.Adapter<InstructionsStepsAdapter.ViewHolder>() {
    private lateinit var binding: ItemInstructionStepBinding
    private var items = emptyList<Step>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemInstructionStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()
    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Step) {
            binding.apply {
                //Text
                stepTxt.text = item.number.toString()
                item.length?.let {
                    timeTxt.text = item.length.number!!.minToHour()
                }
                infoTxt.text = item.step
            }
        }
    }


    fun setData(data: List<Step>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}