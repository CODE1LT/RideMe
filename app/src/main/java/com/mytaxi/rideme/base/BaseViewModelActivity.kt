package com.mytaxi.rideme.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mytaxi.rideme.BR
import com.mytaxi.rideme.network.monitor.NetworkMonitor
import com.mytaxi.rideme.network.monitor.NetworkMonitorListener
import javax.inject.Inject

abstract class BaseViewModelActivity : BaseActivity(), NetworkMonitorListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        if (dataBindingEnabled()) {
            initViewDataBinding()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun initViewModel() {
        logger.d(getTag(), "initViewModel()")
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(getViewModelClass() as Class<ViewModel>)
    }

    private fun initViewDataBinding() {
        logger.d(getTag(), "initViewDataBinding()")
        viewDataBinding.setVariable(BR.viewmodel, viewModel)
    }

    override fun onResume() {
        super.onResume()
        networkMonitor.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        networkMonitor.removeListener(this)
    }

    abstract fun getViewModelClass(): Class<*>
    abstract fun internetConnectionLost()
    abstract fun internetConnectionRecovered()

    override fun onNetworkAvailable() {
        logger.d(getTag(), "onNetworkAvailable()")
        internetConnectionRecovered()
    }

    override fun onNetworkUnavailable() {
        logger.d(getTag(), "onNetworkUnavailable()")
        internetConnectionLost()
    }
}