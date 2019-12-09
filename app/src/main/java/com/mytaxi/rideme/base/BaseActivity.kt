package com.mytaxi.rideme.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dagger.android.support.DaggerAppCompatActivity
import com.mytaxi.rideme.R
import com.mytaxi.rideme.logger.Logger
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var logger: Logger

    lateinit var viewDataBinding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        if (dataBindingEnabled()) {
            initViewDataBinding()
        }
    }

    private fun initViewDataBinding() {
        logger.d(getTag(), "initViewDataBinding()")
        viewDataBinding = DataBindingUtil.setContentView<ViewDataBinding>(this, getLayoutId())
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.executePendingBindings()
    }

    fun openActivity(
        activityClass: Class<*>,
        finishCurrentActivity: Boolean,
        requestCode: Int? = null
    ) {
        openActivity(Intent(this, activityClass), finishCurrentActivity, requestCode)
    }

    private fun openActivity(intent: Intent, finishCurrentActivity: Boolean, requestCode: Int? = null) {
        if (requestCode != null) {
            startActivityForResult(intent, requestCode)
        } else {
            startActivity(intent)
        }
        if (finishCurrentActivity) {
            finish()
        }
        overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left)
    }

    fun hideKeyboard() {
        val view = window.decorView.rootView
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right)
    }

    abstract fun getLayoutId(): Int
    abstract fun dataBindingEnabled(): Boolean
    abstract fun getTag(): String
}