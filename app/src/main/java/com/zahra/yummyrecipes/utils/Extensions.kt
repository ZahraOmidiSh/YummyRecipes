package com.zahra.yummyrecipes.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(message:String){
    Snackbar.make(this,message,Snackbar.LENGTH_SHORT).show()
}