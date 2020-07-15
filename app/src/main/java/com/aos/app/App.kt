package com.aos.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.aos.app.ui.base.BaseViewModel
import com.aos.app.ui.base.MvvmActivity
import com.aos.app.ui.base.MvvmFragment

/**
 * Created by:  qiliantao on 2020.04.25 16:43
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */
class App : Application() {

    companion object {
        lateinit var application: Application
        lateinit var sp: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        sp = getSharedPreferences(packageName, Context.MODE_PRIVATE)
    }
}

/**
 * 可存储全局需求的数据
 */
class AppViewModel(application: Application) : AndroidViewModel(application) {

}

fun <VM : BaseViewModel> Class<VM>.getVm(activity: MvvmActivity<ViewDataBinding>) = activity[this]
fun <VM : BaseViewModel> Class<VM>.getVm(fragment: MvvmFragment<ViewDataBinding>) = fragment[this]

val mainHandler: android.os.Handler = android.os.Handler(Looper.getMainLooper())

fun AppCompatActivity.toast(msg: CharSequence) {
    mainHandler.post {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.toast(msg: CharSequence) {
    mainHandler.post {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }
}

fun AppCompatActivity.loge(any: Any? = null, msg: String) {
    Log.e(any?.javaClass?.simpleName ?: "AppMvvm", msg)
}

fun Fragment.loge(any: Any? = null, msg: String) {
    Log.e(any?.javaClass?.simpleName ?: "AppMvvm", msg)
}

fun BaseViewModel.loge(any: Any? = null, msg: String) {
    Log.e(any?.javaClass?.simpleName ?: "AppMvvm", msg)
}


