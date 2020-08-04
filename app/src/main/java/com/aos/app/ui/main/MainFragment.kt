package com.aos.app.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.aos.app.BR
import com.aos.app.R
import com.aos.app.databinding.FMainBinding
import com.aos.app.ui.base.MvvmFragment
import kotlinx.android.synthetic.main.f_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainFragment : MvvmFragment<FMainBinding>() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var viewModel: MainFragViewModel? = null
//    private val viewModel: MainFragViewModel by viewModels<MainFragViewModel>()
//    private val viewModel: MainFragViewModel by viewModels<MainFragViewModel>(ownerProducer = {this}, factoryProducer = {defaultViewModelProviderFactory})
//    private val viewModel: MainFragViewModel by createViewModelLazy(MainFragViewModel::class, storeProducer = {requireActivity().viewModelStore}/*, factoryProducer = {requireActivity().defaultViewModelProviderFactory}*/)

    override fun getLayout() = R.layout.f_main

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        EventBus.getDefault().register(this)
        viewModel = initViewModel<MainFragViewModel>(BR.mainVm)
//        viewModelProvider.get<MainFragViewModel>()
//        get<MainFragViewModel>()
        viewModel?.content?.value = "我是主页面"



//        this[MainFragViewModel::class.java].content
//        get(MainFragViewModel::class.java).content
//        MainFragViewModel::class.java.getVm(this as MvvmFragment<ViewDataBinding>)
    }

    override fun onStart() {
        super.onStart()
        if(isVDBInit()){
            viewDataBinding.lifecycleOwner = this
        }
    }

    override fun onStop() {
        super.onStop()
        if(isVDBInit()){
            viewDataBinding.lifecycleOwner = null//此处导致自定义BindingAdapter方法每次执行onStart()的时候会被执行
        }
    }

    override fun getViewModelStore(): ViewModelStore {
        return super.getViewModelStore()
    }

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return super.getDefaultViewModelProviderFactory()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun dataChanged(str : String) {
        view?.postDelayed({
            val s = title.text.toString()
            Log.i("MainFragment", s)
        }, 1000)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

}
