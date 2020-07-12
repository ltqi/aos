package com.aos.app.ui.base

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.*
import com.aos.app.kt.show
import com.aos.app.ut.ADialogFragment
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.reflect.ParameterizedType

/**
 *
 */
abstract class MvvmActivity<B : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var viewModelProvider: ViewModelProvider
    protected lateinit var viewDataBinding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeOnCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        val actualTypeArguments = (javaClass.genericSuperclass as? ParameterizedType)?.actualTypeArguments
        if (actualTypeArguments != null && actualTypeArguments.isNotEmpty()) {
            val tClass = actualTypeArguments[0] as? Class<B>
            if (tClass != null && tClass != ViewDataBinding::class.java) {
                beforeSetContentView(savedInstanceState)
                viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
                viewDataBinding.lifecycleOwner = this
                viewModelProvider = ViewModelProvider(this)
                afterSetContentView(viewDataBinding.root, savedInstanceState)
            } else {
                beforeSetContentView(savedInstanceState)
                setContentView(getLayoutId())
                afterSetContentView(window.decorView.findViewById<ViewGroup>(android.R.id.content).getChildAt(0), savedInstanceState)
            }
        }
    }

    protected fun beforeOnCreate(savedInstanceState: Bundle?) {}
    protected fun beforeSetContentView(savedInstanceState: Bundle?) {}
    protected fun afterSetContentView(view: View, savedInstanceState: Bundle?) {}

    abstract fun getLayoutId(): Int

    fun  <VM : BaseViewModel>  addViewModel(variableId: Int, viewModelClazz: Class<VM>): VM? {
        if (!this::viewDataBinding.isInitialized)
            return null
        ViewModelProvider(this).apply {
            viewModelProvider = this
        }[viewModelClazz].apply {
            viewModelProvider.value = this@MvvmActivity.viewModelProvider
            lifecycle.addObserver(this)
            viewDataBinding.setVariable(variableId, this)
            showLoading.observe(this@MvvmActivity, Observer {
                when(it) {
                    true -> showLoading()
                    false -> dismissLoading()
                }
            })
            return this
        }
    }

    /**
     * 子类若没有使用ViewModel, 调用自然返回null
     */
    @MainThread
    operator fun <VM : BaseViewModel> get(@NonNull modelClass: Class<VM>): VM {
        if (!this::viewDataBinding.isInitialized) {
            throw Exception("你正在操作的[" + javaClass.name +"]没有可使用viewModel实现")
        }
        return viewModelProvider.get(modelClass)
    }


    //示例特殊生命周期方法处理
//    protected lateinit var viewModel: BaseViewModel
    override fun onRestart() {
        super.onRestart()
//        viewModel.onRestart.value = SystemClock.currentThreadTimeMillis()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
//        viewModel.onNewIntent.value = intent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        viewModel.onActivityResult.value = mutableMapOf(Pair("requestCode", requestCode), Pair("resultCode", resultCode), Pair("data", data))
    }

    val dialogLoading by lazy { ADialogFragment() }
    fun showLoading(){
        show("开始加载数据")
        dialogLoading.show(supportFragmentManager, "loading")
    }

    fun dismissLoading(){
        show("加载数据结束")
        dialogLoading.dismiss()
    }

    fun launch(block: suspend () -> Unit) = lifecycleScope.launch {
        try {
            showLoading()
            block()
            dismissLoading()
        } catch (e: Exception) {
            dismissLoading()
        }
    }

}
