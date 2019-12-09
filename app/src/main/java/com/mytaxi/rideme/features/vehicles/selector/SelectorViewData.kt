package com.mytaxi.rideme.features.vehicles.selector

import com.mytaxi.rideme.customviews.footerselector.FooterSelectorItemType
import com.mytaxi.rideme.customviews.generalmessage.GeneralMessage

data class SelectorViewData(
    var selectorFooterItemType: FooterSelectorItemType = FooterSelectorItemType.LIST,
    var loading: Boolean = false,
    var generalMessage: GeneralMessage = GeneralMessage()
)