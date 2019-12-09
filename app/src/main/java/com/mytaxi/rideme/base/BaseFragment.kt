package com.mytaxi.rideme.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dagger.android.support.DaggerFragment
import com.mytaxi.rideme.logger.Logger
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var logger: Logger

    lateinit var viewDataBinding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.d(getFragmentTag(), "onCreate()")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger.d(getFragmentTag(), "onCreateView()")
        return when {
            view != null -> view
            dataBindingEnabled() -> {
                viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
                viewDataBinding.lifecycleOwner = this
                viewDataBinding.executePendingBindings()
                viewDataBinding.root
            }
            else -> inflater.inflate(getLayoutId(), container, false)
        }
    }

    abstract fun getLayoutId(): Int
    abstract fun dataBindingEnabled(): Boolean
    abstract fun getFragmentTag(): String
}