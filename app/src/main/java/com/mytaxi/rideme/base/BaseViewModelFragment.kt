package com.mytaxi.rideme.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mytaxi.rideme.BR
import javax.inject.Inject

abstract class BaseViewModelFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: ViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setDataBindingVariable()
    }

    private fun setDataBindingVariable() {
        viewDataBinding.setVariable(BR.viewmodel, viewModel)
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun initViewModel() {
        logger.d(getFragmentTag(), "initViewModel()")
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(getViewModelClass() as Class<ViewModel>)
    }

    abstract fun getViewModelClass(): Class<*>
}