package com.mytaxi.rideme.features.vehicles.selector

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mytaxi.rideme.R
import com.mytaxi.rideme.base.BaseViewModelActivity
import com.mytaxi.rideme.customviews.footerselector.FooterSelectorItemType
import com.mytaxi.rideme.databinding.ActivitySelectorBinding
import com.mytaxi.rideme.features.vehicles.list.ListFragment
import com.mytaxi.rideme.features.vehicles.list.TaxiData
import com.mytaxi.rideme.features.vehicles.map.MapFragment
import com.mytaxi.rideme.utils.GpsUtils
import com.mytaxi.rideme.utils.PermissionsUtils
import javax.inject.Inject

class SelectorActivity : BaseViewModelActivity(), GpsStatusListener {

    companion object {
        private const val TAG = "SelectorActivity"
        const val FLEET_TYPE = "fleet_type"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
    }

    @Inject
    lateinit var permissionsUtils: PermissionsUtils
    @Inject
    lateinit var gpsUtils: GpsUtils

    private lateinit var selectorViewModel: SelectorViewModel
    private lateinit var selectorDataBinding: ActivitySelectorBinding
    private var isGpsEnabled = false

    override fun getViewModelClass(): Class<*> = SelectorViewModel::class.java

    override fun getLayoutId(): Int = R.layout.activity_selector

    override fun dataBindingEnabled(): Boolean = true

    override fun getTag(): String = TAG

    override fun internetConnectionLost() {
        logger.d(TAG, "internetConnectionLost()")
        selectorViewModel.showGeneralError(getString(R.string.network_error))
    }

    override fun internetConnectionRecovered() {
        logger.d(TAG, "internetConnectionRecovered()")
        if (shouldReplaceFragment(FooterSelectorItemType.LIST)) {
            selectorViewModel.showGeneralSuccess(getString(R.string.connected))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.d(TAG, "onCreate()")
        initialize()
        getAndObserveViewLiveData()
        getAndObserveOnTaxiCardClickEvent()
        permissionsUtils.initPermissionUtils(this)
        permissionsUtils.requestLocationPermissions()
        gpsUtils.init(this)
        gpsUtils.turnGpsOn(this)
    }

    private fun initialize() {
        logger.d(TAG, "initialize()")
        selectorViewModel = viewModel as SelectorViewModel
        selectorDataBinding = viewDataBinding as ActivitySelectorBinding
    }

    private fun getAndObserveViewLiveData() {
        logger.d(TAG, "getAndObserveViewLiveData()")
        selectorViewModel.getSelectorViewLiveData().observe(this, Observer {
            replaceFragment(it?.selectorFooterItemType)
            changeTitle(it?.selectorFooterItemType)
        })
    }

    private fun replaceFragment(footerSelectorItemType: FooterSelectorItemType?) {
        logger.d(TAG, "replaceFragment(), footerSelectorItemType=$footerSelectorItemType")
        if (shouldReplaceFragment(footerSelectorItemType)) return
        val fragment = getFooterItemTypeFragment(footerSelectorItemType)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.a_selector_fl_container,
            fragment,
            footerSelectorItemType.toString())
        transaction.commit()
        hideKeyboard()
    }

    private fun shouldReplaceFragment(footerSelectorItemType: FooterSelectorItemType?): Boolean {
        logger.d(TAG, "shouldReplaceFragment(), footerSelectorItemType=$footerSelectorItemType")
        return supportFragmentManager.fragments.size > 0 &&
                supportFragmentManager.fragments.find { it.tag == footerSelectorItemType.toString() } != null
    }

    private fun getFooterItemTypeFragment(footerSelectorItemType: FooterSelectorItemType?): Fragment {
        logger.d(
            TAG,
            "getFooterItemTypeFragment(), footerSelectorItemType=$footerSelectorItemType")
        return when (footerSelectorItemType) {
            FooterSelectorItemType.LIST -> ListFragment()
            FooterSelectorItemType.MAP -> MapFragment()
            else -> MapFragment()
        }
    }

    private fun changeTitle(footerSelectorItemType: FooterSelectorItemType?) {
        logger.d(
            TAG,
            "changeTitle(), footerSelectorItemType=$footerSelectorItemType")
        if (footerSelectorItemType == FooterSelectorItemType.LIST) {
            selectorDataBinding.aSelectorTvTitle.text =
                this.getString(R.string.a_selector_taxi_fleet_title_tv_text)
        } else {
            selectorDataBinding.aSelectorTvTitle.text =
                this.getString(R.string.a_selector_map_title_tv_text)
        }
    }

    private fun getAndObserveOnTaxiCardClickEvent() {
        logger.d(TAG, "getAndObserveOnTaxiCardClickEvent()")
        selectorViewModel.getOnTaxiCardClickEvent().observe(this, Observer {
            openMapFragment(it)
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        permissionsUtils.onRequestPermissionsResult(requestCode)
    }

    override fun gpsStatus(isGPSEnable: Boolean) {
        this.isGpsEnabled = isGPSEnable
    }

    private fun openMapFragment(taxiData: TaxiData) {
        logger.d(TAG, "openMapFragment()")
        val bundle = Bundle()
        bundle.putString(FLEET_TYPE, taxiData.type)
        bundle.putDouble(LATITUDE, taxiData.latitude)
        bundle.putDouble(LONGITUDE, taxiData.longitude)
        if (shouldReplaceFragment(FooterSelectorItemType.MAP)) return
        val fragment = getFooterItemTypeFragment(FooterSelectorItemType.MAP)
        fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.a_selector_fl_container,
            fragment,
            FooterSelectorItemType.MAP.toString())
        transaction.commit()
        selectorViewModel.clickMapSelector()
        hideKeyboard()
    }

}