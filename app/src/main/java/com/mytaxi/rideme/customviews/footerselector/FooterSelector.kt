package com.mytaxi.rideme.customviews.footerselector

import android.content.Context
import androidx.databinding.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import com.mytaxi.rideme.R

@BindingMethods(
    BindingMethod(type = FooterSelector::class,
        attribute = "app:onFooterItemTypeSelected",
        method = "setOnFooterItemTypeSelectListener"))
@InverseBindingMethods(
    InverseBindingMethod(
        type = FooterSelector::class,
        attribute = "app:footerItemType",
        method = "getFooterItemType",
        event = "android:footerItemTypeAttrChanged"))


class FooterSelector(context: Context, attributeSet: AttributeSet?)
    : ConstraintLayout(context, attributeSet) {

    constructor(context: Context) : this(context, null)

    companion object {
        @JvmStatic
        @BindingAdapter(value = ["android:footerItemTypeAttrChanged"])
        fun setListener(footerSelectorItemTypeSelector: FooterSelector?, listener: InverseBindingListener?) {
            if (listener != null) {
                val onClickListener = { it: View? ->
                    val paymentsFooterItemType = when (it?.id) {
                        R.id.l_footer_type_selector_tv_fleet,
                        R.id.l_footer_type_selector_ibtn_fleet -> FooterSelectorItemType.LIST
                        R.id.l_footer_type_selector_tv_map,
                        R.id.l_footer_type_selector_ibtn_map -> FooterSelectorItemType.MAP
                        else -> FooterSelectorItemType.LIST
                    }
                    footerSelectorItemTypeSelector?.setPaymentsFooterItemType(paymentsFooterItemType)
                    footerSelectorItemTypeSelector?.onFooterItemSelectListener?.onFooterItemSelected()
                    listener.onChange()
                }

                footerSelectorItemTypeSelector?.tvFleet?.setOnClickListener(onClickListener)
                footerSelectorItemTypeSelector?.tvMap?.setOnClickListener(onClickListener)
                footerSelectorItemTypeSelector?.ibtnFleet?.setOnClickListener(onClickListener)
                footerSelectorItemTypeSelector?.ibtnMap?.setOnClickListener(onClickListener)
            }
        }

        @JvmStatic
        @BindingAdapter("app:footerItemType")
        fun setPaymentsFooterItemType(
            footerSelectorItemTypeSelector: FooterSelector,
            footerSelectorItemType: FooterSelectorItemType?) {
            footerSelectorItemTypeSelector.setPaymentsFooterItemType(footerSelectorItemType
                ?: FooterSelectorItemType.LIST)
        }
    }

    private var tvFleet: AppCompatTextView? = null
    private var tvMap: AppCompatTextView? = null
    private var ibtnFleet: ImageButton? = null
    private var ibtnMap: ImageButton? = null

    private var footerSelectorItemType: FooterSelectorItemType? = null
    private var onFooterItemSelectListener: OnFooterItemSelectListener? = null

    init {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.layout_footer_type_selector, this, true)
        tvFleet = layout.findViewById(R.id.l_footer_type_selector_tv_fleet)
        tvMap = layout.findViewById(R.id.l_footer_type_selector_tv_map)
        ibtnFleet = layout.findViewById(R.id.l_footer_type_selector_ibtn_fleet)
        ibtnMap = layout.findViewById(R.id.l_footer_type_selector_ibtn_map)
    }

    fun setOnFooterItemTypeSelectListener(onFooterItemSelectListener: OnFooterItemSelectListener) {
        this.onFooterItemSelectListener = onFooterItemSelectListener
    }

    fun setPaymentsFooterItemType(footerSelectorItemType: FooterSelectorItemType) {
        if (this.footerSelectorItemType == footerSelectorItemType) {
            return
        }
        this.footerSelectorItemType = footerSelectorItemType
        tvFleet?.setTextColor(ContextCompat.getColor(context, R.color.default_color_inactive))
        tvMap?.setTextColor(ContextCompat.getColor(context, R.color.default_color_inactive))
        ibtnFleet?.setImageResource(R.drawable.ic_taxi_inactive)
        ibtnMap?.setImageResource(R.drawable.ic_map_inactive)

        when (footerSelectorItemType) {
            FooterSelectorItemType.LIST -> {
                tvFleet?.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                ibtnFleet?.setImageResource(R.drawable.ic_taxi)
            }
            FooterSelectorItemType.MAP -> {
                tvMap?.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                ibtnMap?.setImageResource(R.drawable.ic_map)
            }
        }
    }

    fun getFooterItemType() = footerSelectorItemType
}