package com.zahra.yummyrecipes.utils

import android.graphics.text.LineBreaker
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun RecyclerView.setupRecyclerview(
    myLayoutManager: RecyclerView.LayoutManager, myAdapter: RecyclerView.Adapter<*>
) {
    this.apply {
        layoutManager = myLayoutManager
        setHasFixedSize(true)
        adapter = myAdapter
    }
}

fun TextView.justify() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        this.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
    }
}

fun Int.minToHour(): String {
    val time: String
    val hours: Int = this / 60
    val minutes: Int = this % 60
    time = if (hours > 0) {
        "${hours}h:${minutes}min"
    } else {
        "${minutes}min"
    }
    return time
}

fun <T> LiveData<T>.onceObserve(owner: LifecycleOwner, observe: Observer<T>) {
    observe(owner, object : Observer<T> {
        override fun onChanged(t: T) {
            removeObserver(this)
            observe.onChanged(t)
        }
    })
}