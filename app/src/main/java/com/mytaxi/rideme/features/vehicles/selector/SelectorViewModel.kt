package com.mytaxi.rideme.features.vehicles.selector

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mytaxi.rideme.archextensions.SingleLiveEvent
import com.mytaxi.rideme.customviews.footerselector.FooterSelectorItemType
import com.mytaxi.rideme.features.vehicles.list.TaxiData
import com.mytaxi.rideme.logger.Logger
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SelectorViewModel @Inject constructor(
    private val logger: Logger
) : ViewModel(), FragmentsListener {

    companion object {
        private const val TAG = "SelectorViewModel"
    }

    private val compositeDisposable = CompositeDisposable()
    private val onTaxiCardClickEvent = SingleLiveEvent<TaxiData>()
    val viewLiveData = MutableLiveData<SelectorViewData>().apply { value = SelectorViewData() }

    fun getOnTaxiCardClickEvent(): SingleLiveEvent<TaxiData> {
        logger.d(TAG, "getOnTaxiCardClickEvent()")
        return onTaxiCardClickEvent
    }

    fun getSelectorViewLiveData(): MutableLiveData<SelectorViewData> {
        logger.d(TAG, "getSelectorViewLiveData()")
        return viewLiveData
    }

    override fun onTaxiCardClicked(taxiData: TaxiData) {
        logger.d(TAG, "onTaxiCardClicked()")
        onTaxiCardClickEvent.postValue(taxiData)
    }

    fun onSelectorFooterItemTypeSelected() {
        logger.d(TAG, "onSelectorFooterItemTypeSelected()")
        notifyLiveDataObservers()
    }

    fun clickMapSelector() {
        logger.d(TAG, "clickMapSelector()")
        viewLiveData.value?.selectorFooterItemType = FooterSelectorItemType.MAP
        notifyLiveDataObservers()
    }

    private fun notifyLiveDataObservers() {
        logger.d(TAG, "notifyLiveDataObservers()")
        viewLiveData.postValue(viewLiveData.value)
    }

    override fun showGeneralError(text: String?) {
        logger.d(TAG, "showGeneralError(), key=$text")
        viewLiveData.value?.generalMessage?.setErrorMessage(text ?: "")
        notifyLiveDataObservers()
    }

    override fun showGeneralSuccess(text: String?) {
        logger.d(TAG, "showGeneralSuccess(), key=$text")
        viewLiveData.value?.generalMessage?.setSuccessMessage(text ?: "")
        notifyLiveDataObservers()
    }

    override fun showLoading() {
        logger.d(TAG, "showLoading()")
        viewLiveData.value?.loading = true
        notifyLiveDataObservers()
    }

    override fun hideLoading() {
        logger.d(TAG, "hideLoading()")
        viewLiveData.value?.loading = false
        notifyLiveDataObservers()
    }

    override fun onCleared() {
        logger.d(TAG, "onCleared()")
        super.onCleared()
        compositeDisposable.clear()
    }

}