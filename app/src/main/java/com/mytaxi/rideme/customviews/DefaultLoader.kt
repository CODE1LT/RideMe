package com.mytaxi.rideme.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.mytaxi.rideme.R

class DefaultLoader(context: Context, attributeSet: AttributeSet?) : RelativeLayout(context, attributeSet) {

    constructor(context: Context) : this(context, null)

    init {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_default_loader, this, true)
    }
}