package com.mytaxi.rideme.features.vehicles.map

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mytaxi.rideme.R
import com.mytaxi.rideme.base.domain.ReactiveInteractor
import com.mytaxi.rideme.data.repositories.taxifleetrepository.Poi
import com.mytaxi.rideme.features.vehicles.list.MapBoundsData
import com.mytaxi.rideme.features.vehicles.selector.FragmentsListener
import com.mytaxi.rideme.logger.Logger
import com.mytaxi.rideme.utils.NetworkConnectionUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class MapViewModel @Inject constructor(
    private val context: Context,
    private val logger: Logger,
    private val networkConnectionUtils: NetworkConnectionUtils,
    private val fragmentsListener: FragmentsListener,
    @Named(MapModule.GET_TAXI_FLEET_INTERACTOR)
    private val getTaxiFleetInteractor: ReactiveInteractor.RetrieveStreamInteractorWithParams<@JvmSuppressWildcards MapBoundsData, List<Poi>>)
    : ViewModel() {

    companion object {
        private const val TAG = "MapViewModel"
    }

    private val viewLiveData = MutableLiveData<MapViewData>()
            .apply { value = MapViewData() }
    private val compositeDisposable = CompositeDisposable()

    fun getTaxiFleet(mapBounds: MapBoundsData) {
        if (networkConnectionUtils.isNetworkAvailable()) {
            logger.d(TAG, "getTaxiFleet()")
            fragmentsListener.showLoading()
            val getTaxiFleetDisposable = getTaxiFleetInteractor
                .getStream(mapBounds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        logger.d(TAG, "getTaxiFleet(), onNext()")
                        viewLiveData.value?.poiList = it
                        fragmentsListener.hideLoading()
                        notifyLiveDataObservers()
                    },
                    {
                        logger.e(TAG, "getTaxiFleet(), onError(), error=${it?.message}")
                        fragmentsListener.showGeneralError(it?.message)
                        fragmentsListener.hideLoading()
                    })
            compositeDisposable.add(getTaxiFleetDisposable)
        } else {
            fragmentsListener.showGeneralError(context.getString(R.string.network_error))
        }
    }


    fun getTaxiListViewLiveData(): MutableLiveData<MapViewData> {
        logger.d(TAG, "getTaxiListViewLiveData()")
        return viewLiveData
    }

    @Suppress("UNUSED")
    private fun notifyLiveDataObservers() {
        logger.d(TAG, "notifyLiveDataObservers()")
        viewLiveData.postValue(viewLiveData.value)
    }

    override fun onCleared() {
        logger.d(TAG, "onCleared()")
        compositeDisposable.clear()
        super.onCleared()
    }
}