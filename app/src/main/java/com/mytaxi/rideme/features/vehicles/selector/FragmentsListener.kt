package com.mytaxi.rideme.features.vehicles.selector

import com.mytaxi.rideme.features.vehicles.list.TaxiData

interface FragmentsListener {
    fun onTaxiCardClicked(taxiData: TaxiData)
    fun showGeneralError(text: String?)
    fun showGeneralSuccess(text: String?)
    fun showLoading()
    fun hideLoading()
}