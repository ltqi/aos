package com.aos.app.ui

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.aos.app.R
import com.aos.app.getVm
import com.aos.app.net.ApiAuthService
import com.aos.app.ui.base.MvvmActivity
import com.aos.app.ui.main.MainFragViewModel
import com.aos.app.ui.main.MainFragment
import com.q.net.ARetrofit

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
