package com.zahra.yummyrecipes.utils

import androidx.recyclerview.widget.DiffUtil

class BaseDiffUtils<T>(private val oldItems: List<T>, private val newItems: List<T>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] === newItems[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] === newItems[newItemPosition]
    }
}