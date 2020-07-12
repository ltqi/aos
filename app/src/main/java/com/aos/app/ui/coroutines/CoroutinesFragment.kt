package com.aos.app.ui.coroutines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.aos.app.BR
import com.aos.app.R
import com.aos.app.databinding.FCoroutinesBinding
import com.aos.app.databinding.FMainBinding
import com.aos.app.ui.base.MvvmFragment
import com.aos.app.ui.main.MainFragViewModel

class CoroutinesFragment : MvvmFragment<FCoroutinesBinding>() {

    companion object {
        fun newInstance() = CoroutinesFragment()
    }

    override fun getLayout() = R.layout.f_coroutines
    private val viewModel: CoroutinesFragViewModel /*by activityViewModels()*/
        by viewModels<CoroutinesFragViewModel> ()

//        by viewModels<CoroutinesFragViewModel> ({this}, {defaultViewModelProviderFactory})
//        by viewModels<CoroutinesFragViewModel> {defaultViewModelProviderFactory}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        viewModel = ViewModelProvider(this).get(CoroutinesFragViewModel::class.java)
//        lifecycle.addObserver(viewModel)
//        viewDataBinding.setVariable(BR.mainVm, viewModel)
//        addViewModel(BR.vm, CoroutinesFragViewModel::class.java)
        val vm = initViewModel<CoroutinesFragViewModel>(BR.vm)

//        viewModel = this[CoroutinesFragViewModel::class.java]

//        viewDataBinding.vm = viewModel
        viewModel.content.value = "协程页面"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }


//    override fun onStart() {
//        super.onStart()
//        if(isViewDataBindingInit()){
//            viewDataBinding.lifecycleOwner = this
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        if(isViewDataBindingInit()){
//            viewDataBinding.lifecycleOwner = null//此处导致自定义BindingAdapter方法每次执行onStart()的时候会被执行
//        }
//    }
}
