package com.aos.app.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.aos.app.ut.ADialogFragment
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

abstract class MvvmFragment<VB : ViewDataBinding> : Fragment()/*, HasDefaultViewModelProviderFactory*/ {

    protected lateinit var viewModelProvider: ViewModelProvider
    protected lateinit var viewDataBinding: VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val actualTypeArguments = (javaClass.genericSuperclass as? ParameterizedType)?.actualTypeArguments
        if (actualTypeArguments != null && actualTypeArguments.isNotEmpty()) {
            val tClass = actualTypeArguments[0] as? Class<VB>
            if (tClass != null && tClass != ViewDataBinding::class.java) {
                viewDataBinding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
                viewDataBinding.lifecycleOwner = this
                viewModelProvider = ViewModelProvider(this)
                return viewDataBinding.root
            }
        }
        return inflater.inflate(getLayout(), container)
    }

    @LayoutRes
    abstract fun getLayout(): Int

    fun isVDBInit() = this::viewDataBinding.isInitialized

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewLifecycleOwner.lifecycleScope.launch {}
//        lifecycleScope.launch {}
    }

    /**
     * reified 修饰的泛型，可避免传Class, 类似xx::class.java
     */
    @MainThread
    inline fun <reified VM : BaseViewModel> get() = viewModelProvider.get<VM>()

    /**使用
     * 带操作符，需至少有一个参数
     */
    @MainThread
    operator fun <VM : BaseViewModel> get(@NonNull modelClass: Class<VM>): VM {
        return viewModelProvider[modelClass]
//        return viewModelProvider.get(modelClass)
    }

    inline fun <reified VM : BaseViewModel> initViewModel(variableId: Int): VM {
        if (!isVDBInit()) {
            throw Exception("未使用MVVM")
        }
//        viewModelProvider[VM::class.java]
//        viewModelProvider.get<VM>()
        viewModels<VM>().value.apply {
            viewModelProvider.value = this@MvvmFragment.viewModelProvider
            lifecycle.addObserver(this)
            viewDataBinding.setVariable(variableId, this)
            showLoading.observe(this@MvvmFragment, Observer {
                when (it) {
                    true -> showLoading()
                    false -> dismissLoading()
                }
            })
            return this
        }
    }

//    inline fun  <reified VM : BaseViewModel>  addViewModel(variableId: Int, viewModel: VM): VM? {
////        if (!this::viewDataBinding.isInitialized)
////            return null
//        viewModel.apply {
//            viewModelProvider.value = this@MvvmFragment.viewModelProvider
//            lifecycle.addObserver(this)
//            viewDataBinding.setVariable(variableId, this)
//            showLoading.observe(this@MvvmFragment, Observer {
//                when(it) {
//                    true -> showLoading()
//                    false -> dismissLoading()
//                }
//            })
//            return this
//        }
//    }

//    fun  <VM : BaseViewModel>  addViewModel(variableId: Int, viewModelClazz: Class<VM>): VM? {
//        if (!this::viewDataBinding.isInitialized)
//            return null
//        viewModelProvider[viewModelClazz].apply {
//            viewModelProvider.value = this@MvvmFragment.viewModelProvider
//            lifecycle.addObserver(this)
//            viewDataBinding.setVariable(variableId, this)
//            showLoading.observe(this@MvvmFragment, Observer {
//                when(it) {
//                    true -> showLoading()
//                    false -> dismissLoading()
//                }
//            })
//            return this
//        }
//    }

//    @MainThread
//    inline fun <reified VM : BaseViewModel> get() = get(VM::class.java)

//    override fun onStart() {
//        super.onStart()
//        if(this::viewDataBinding.isInitialized){
//            viewDataBinding.lifecycleOwner = this
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        if(this::viewDataBinding.isInitialized){
//            viewDataBinding.lifecycleOwner = null//此处导致自定义BindingAdapter方法每次执行onStart()的时候会被执行
//        }
//    }

    //示例特殊生命周期方法处理
//    protected lateinit var viewModel: BaseViewModel

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        viewModel.onActivityResult.value = mutableMapOf(Pair("requestCode", requestCode), Pair("resultCode", resultCode), Pair("data", data))
    }

    fun launch(block: suspend () -> Unit) = lifecycleScope.launch {
        try {
            showLoading()
            block()
            dismissLoading()
        } catch (e: java.lang.Exception) {
            dismissLoading()
        }
    }

    private val dialogLoading by lazy { ADialogFragment() }
    fun showLoading() {
        show("开始加载数据")
        dialogLoading.show(requireActivity().supportFragmentManager, "loading")
    }

    fun dismissLoading() {
        show("加载数据结束")
        dialogLoading.dismiss()
    }
}
