package com.mytaxi.rideme.features.vehicles.list

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mytaxi.rideme.R
import com.mytaxi.rideme.base.domain.ReactiveInteractor
import com.mytaxi.rideme.data.repositories.taxifleetrepository.Poi
import com.mytaxi.rideme.features.vehicles.selector.FragmentsListener
import com.mytaxi.rideme.logger.Logger
import com.mytaxi.rideme.utils.NetworkConnectionUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class ListViewModel @Inject constructor(
    private val context: Context,
    private val logger: Logger,
    @Named(ListModule.GET_TAXI_FLEET_INTERACTOR)
    private val getTaxiFleetInteractor: ReactiveInteractor.RetrieveStreamInteractorWithParams<@JvmSuppressWildcards MapBoundsData, List<Poi>>,
    private val fragmentsListener: FragmentsListener,
    private val networkConnectionUtils: NetworkConnectionUtils,
    private val getLocationProviderInteractor: ReactiveInteractor.RetrieveStreamInteractor<Location>
) : ViewModel() {

    companion object {
        private const val TAG = "ListViewModel"
    }

    private val compositeDisposable = CompositeDisposable()
    val viewLiveData =
        MutableLiveData<ListViewData>().apply {
            value = ListViewData(fragmentsListener = fragmentsListener)
        }
    //TODO just for test, Hamburg city area
    private val boundsOfHamburg = MapBoundsData(
        53.694865.toString(),
        9.757589.toString(),
        53.394655.toString(),
        10.099891.toString())

    init {
        getTaxiFleet()
        //viewLiveData.value?.fragmentsListener = fragmentsListener
    }

    fun getTaxiFleet() {
        if (networkConnectionUtils.isNetworkAvailable()) {
            logger.d(TAG, "getTaxiFleet()")
            fragmentsListener.showLoading()
            val getTaxiFleetDisposable = getTaxiFleetInteractor
                .getStream(boundsOfHamburg)
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

    fun getTaxiListViewLiveData(): MutableLiveData<ListViewData> {
        logger.d(TAG, "getTaxiListViewLiveData()")
        return viewLiveData
    }

    @Suppress("UNUSED")
    private fun notifyLiveDataObservers() {
        logger.d(TAG, "notifyLiveDataObservers()")
        viewLiveData.postValue(viewLiveData.value)
    }

    fun getLocation() {
        logger.d(TAG, "getLocation()")
        val getLocationDisposable = getLocationProviderInteractor
            .getStream()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    logger.d(TAG, "getLocation(), onNext()")
                    viewLiveData.value?.location = it
                    notifyLiveDataObservers()
                },
                {
                    logger.e(TAG, "getLocation(), onError(), error=${it?.message}")
                })
        compositeDisposable.add(getLocationDisposable)
    }

    fun startLocationProvider() {
        logger.d(TAG, "startLocationProvider()")
        val getLocationProviderInteractor =
            getLocationProviderInteractor as GetLocationProviderInteractor
        val disposable = getLocationProviderInteractor
            .startLocationProvider()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    logger.e(TAG, "onSuccess()")
                },
                {
                    logger.e(TAG, "onError(), error=${it?.message}")
                }
            )
        compositeDisposable.add(disposable)
    }

    fun stopLoactionProvider() {
        logger.d(TAG, "stopLocationProvider()")
        val getLocationProviderInteractor =
            getLocationProviderInteractor as GetLocationProviderInteractor
        val disposable = getLocationProviderInteractor
            .stopLocationProvider()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    logger.e(TAG, "onSuccess()")
                },
                {
                    logger.e(TAG, "onError(), error=${it?.message}")
                }
            )
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        logger.d(TAG, "onCleared()")
        compositeDisposable.clear()
        super.onCleared()
    }
}