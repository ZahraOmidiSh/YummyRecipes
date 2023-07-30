package com.zahra.yummyrecipes.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(message:String){
    Snackbar.make(this,message,Snackbar.LENGTH_SHORT).show()
}

fun RecyclerView.setupRecyclerview(
    myLayoutManager:RecyclerView.LayoutManager , myAdapter:RecyclerView.Adapter<*>
){
    this.apply {
        layoutManager=myLayoutManager
        setHasFixedSize(true)
        adapter=myAdapter
    }
}