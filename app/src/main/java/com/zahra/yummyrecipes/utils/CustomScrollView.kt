package com.zahra.yummyrecipes.utils

import android.content.Context
import android.graphics.Canvas
import android.widget.ScrollView

class CustomScrollView(context: Context) : ScrollView(context) {

    override fun dispatchDraw(canvas: Canvas?) {
        if (childCount > 0) {
            val child = getChildAt(0)
            val fadingEdgeLength = verticalFadingEdgeLength
            if (scrollY < fadingEdgeLength) {
                canvas?.save()
                canvas?.translate(0f, (-scrollY).toFloat())
                drawChild(canvas, child, drawingTime)
                canvas?.restore()
            } else {
                super.dispatchDraw(canvas)
            }
        }
    }
}