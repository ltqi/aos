package com.aos.app2.base

import android.util.Log
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.aos.app2.view.ADialogFragment
import androidx.lifecycle.lifecycleScope
import com.aos.life.base.BaseVMFragment
import com.aos.life.model.bean.CResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class App2Fragment<T : ViewDataBinding>(@LayoutRes layoutId: Int) :
    BaseVMFragment<T>(layoutId) {

    fun <D, R : CResult<D?>> launch(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> R,
        resultFail: ((R) -> Unit)? = null,
        result: (R) -> Unit
    ) = lifecycleScope.launch(context) {
        try {
            showLoading()
            val retV = withContext(Dispatchers.IO) {
                block()
            }
            if (retV is CResult.Success<*>) {
                result(retV)
            } else {
                if (resultFail != null) {
                    resultFail?.invoke(retV)
                } else {
//                    retV.msg?.also {
//                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
//                    }
                }
            }
            dismissLoading()
        } catch (e: Exception) {
            dismissLoading()
        }

    }

    private val dialogLoading by lazy { ADialogFragment() }
    override fun showLoading() {
//        toast("开始加载数据")
        requireActivity().apply {
            dialogLoading.show(supportFragmentManager, "loading")
            try {
                supportFragmentManager?.executePendingTransactions()
            } catch (e: Exception) {
                Log.e("App2Fragment->", e.localizedMessage)
            }
        }
    }

    override fun dismissLoading() {
//        toast("加载数据结束")
        dialogLoading.dismiss()
    }
}