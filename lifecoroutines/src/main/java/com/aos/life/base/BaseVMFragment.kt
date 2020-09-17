package com.aos.life.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseVMFragment<T : ViewDataBinding>(@LayoutRes var layoutId: Int, var brId: Int = 0) :
    Fragment(layoutId) {

    lateinit var dataBinding: T

    protected fun <T : ViewDataBinding> binding(
        inflater: LayoutInflater,
        @LayoutRes layoutId: Int,
        container: ViewGroup?
    ): T = DataBindingUtil.inflate<T>(inflater, layoutId, container, false).apply {
        lifecycleOwner = this@BaseVMFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = binding(inflater, layoutId, container)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataBinding.lifecycleOwner = this
        startObserve()
        initView()
        initData()
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     *
     */
    fun ViewDataBinding.setVM(variable: Int, viewModel: BaseViewModel) {
        viewModel.initVM()
        setVariable(variable, viewModel)
    }

    /**
     * koin注入方式在 TabLayout ViewPager中不能用, 只能使用viewModels<VM>()
     * 异常（Can't access ViewModels from detached fragment）
     */
    inline fun <reified VM : BaseViewModel> getVM() = viewModel<VM>().value.initVM()

//    /**不用koin viewModel 的时候可以用这个 viewModels, 两者相差一个s
//     * ktx 扩展方法方式获取与 Fragment 或 Activity 一一对应的vm。子类需要手动调用 VM.initVM()
//     */
    inline fun <reified VM : BaseViewModel> getVMs() : Lazy<VM>  = viewModels()

    inline fun <reified VM : BaseViewModel> VM.initVM(): VM {
        lifecycle.addObserver(this)
        showLoading.observe(this@BaseVMFragment, Observer {
            when (it) {
                true -> this@BaseVMFragment.showLoading()
                false -> this@BaseVMFragment.dismissLoading()
            }
        })
        return this
    }


    /** viewmodel 赋值 给dataBinding
     * dataBinding.run {
     *   vm = vModel
     * }
     */
    abstract fun initView()
    abstract fun initData()
    abstract fun startObserve()


    open fun showLoading() {}

    open fun dismissLoading() {}
}