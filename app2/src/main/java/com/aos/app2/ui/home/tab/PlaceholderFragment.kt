package com.aos.app2.ui.home.tab

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.aos.app2.R
import com.aos.app2.base.App2Fragment
import com.aos.app2.databinding.PlceholderFragmentMainBinding

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment :
    App2Fragment<PlceholderFragmentMainBinding>(R.layout.plceholder_fragment_main) {


    val vModel by viewModels<PageViewModel>()

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun initView() {
        vModel.initVM()
        dataBinding.run {
            vm = vModel
        }
    }

    override fun initData() {

    }

    override fun startObserve() {

    }
}