package com.aos.app2.ui.home.tab

import android.os.Bundle
import com.aos.app2.BR
import com.aos.app2.R
import com.aos.app2.base.App2Fragment
import com.aos.app2.databinding.PlceholderFragmentMain2Binding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment2 :
    App2Fragment<PlceholderFragmentMain2Binding>(R.layout.plceholder_fragment_main2) {


    val vModel by viewModel<PageViewModel2>()

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment2 {
            return PlaceholderFragment2().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun initView() {
        dataBinding.setVM(BR.vm, vModel)
        dataBinding.run {
            vm = vModel
        }
    }

    override fun initData() {

    }

    override fun startObserve() {

    }
}