package com.mytaxi.rideme.features.vehicles.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mytaxi.rideme.R
import com.mytaxi.rideme.base.BaseViewModelFragment
import com.mytaxi.rideme.data.repositories.taxifleetrepository.Poi
import com.mytaxi.rideme.databinding.FragmentListBinding
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.os.Handler

class ListFragment : BaseViewModelFragment() {

    companion object {
        private const val TAG = "ListFragment"
    }

    private lateinit var listViewModel: ListViewModel
    private lateinit var listViewDataBinding: FragmentListBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun getViewModelClass(): Class<*> = ListViewModel::class.java
    override fun getLayoutId(): Int = R.layout.fragment_list
    override fun dataBindingEnabled(): Boolean = true
    override fun getFragmentTag(): String = TAG

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.d(TAG, "onViewCreated()")
        initialize()
        getAndObserveViewLiveData()
    }

    private fun initialize() {
        logger.d(TAG, "initialize()")
        listViewModel = viewModel as ListViewModel
        listViewDataBinding = viewDataBinding as FragmentListBinding
        initRecyclerView()
        initSwipeToRefresh()
    }

    private fun initSwipeToRefresh() {
        swipeRefreshLayout = listViewDataBinding.fListSr
        swipeRefreshLayout.setOnRefreshListener {
            listViewModel.getTaxiFleet()
            val handler = Handler()
            handler.postDelayed({
                if (swipeRefreshLayout.isRefreshing) {
                    swipeRefreshLayout.isRefreshing = false
                }
            }, 500)
        }
    }

    private fun initRecyclerView() {
        logger.d(TAG, "initRecyclerView()")
        listViewDataBinding.fListRv.layoutManager = LinearLayoutManager(context)
        listViewDataBinding.fListRv.isNestedScrollingEnabled = true
        listViewDataBinding.fListRv.addItemDecoration(MarginItemDecoration(
            resources.getDimension(R.dimen.f_list_rv_default_padding).toInt()))
        val taxiListAdapter = TaxiListAdapter(
            listViewModel.viewLiveData
        )
        listViewDataBinding.fListRv.adapter = taxiListAdapter
    }

    private fun getAndObserveViewLiveData() {
        logger.d(TAG, "getAndObserveViewLiveData()")
        listViewModel.getTaxiListViewLiveData().observe(this, Observer {
            loadRecyclerView(it.poiList as ArrayList<Poi>)
        })
    }

    private fun loadRecyclerView(taxiList: ArrayList<Poi>) {
        logger.d(TAG, "loadRecyclerView()")
        val adapter = listViewDataBinding.fListRv.adapter as TaxiListAdapter
        adapter.swapData(taxiList)
    }

    override fun onResume() {
        super.onResume()
        listViewModel.startLocationProvider()
        listViewModel.getLocation()
    }

    override fun onPause() {
        super.onPause()
        listViewModel.stopLoactionProvider()
    }
}