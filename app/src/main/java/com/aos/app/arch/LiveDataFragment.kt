package com.aos.app.arch

import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import com.aos.app.BR
import com.aos.app.R
import com.aos.app.databinding.FCoroutinesBinding
import com.aos.app.ui.base.MvvmFragment
import kotlinx.android.synthetic.main.f_livedata.*

class LiveDataFragment : MvvmFragment<FCoroutinesBinding>() {

    companion object {
        fun newInstance() = LiveDataFragment()
    }

    override fun getLayout() = R.layout.f_livedata
    private lateinit var viewModel: LiveDataFragViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = initViewModel(BR.vm)

//        viewModel.getPostalCode("1").observe(viewLifecycleOwner, Observer {
//                tvLiveData.text = it
//        })
//
//        viewModel.postalCode.observe(viewLifecycleOwner, Observer {
//            tvLiveData.text = it
//        })

    }

}
