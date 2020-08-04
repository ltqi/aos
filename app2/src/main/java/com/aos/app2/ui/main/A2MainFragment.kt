package com.aos.app2.ui.main

import com.aos.app2.R
import com.aos.app2.base.App2Fragment
import com.aos.app2.databinding.App2MainFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class A2MainFragment : App2Fragment<App2MainFragmentBinding>(R.layout.app2_main_fragment) {

    companion object {
        fun newInstance() = A2MainFragment()
    }

    val viewModel by viewModel<App2MainViewModel>()

    override fun initView() {
        dataBinding.run {
            vm = viewModel
        }
    }

    override fun initData() {

    }

    override fun startObserve() {

    }


}
