package com.aos.app.ui

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.aos.app.R
import com.aos.app.ui.base.checkPermissions
import com.aos.app.ui.base.MvvmActivity
import com.aos.app.ui.main.MainFragment

/**
 * 展示一些使用方法入口
 */
class MainActivity : MvvmActivity<ViewDataBinding>() {

    override fun getLayoutId() = R.layout.a_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

//        MainFragViewModel::class.java.getVm(this)
//        this[MainFragViewModel::class.java].content

    }


    override fun getViewModelStore(): ViewModelStore {
        return super.getViewModelStore()
    }

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return super.getDefaultViewModelProviderFactory()
    }


}
