package com.aos.app.ui.base

import android.content.Intent
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.aos.app.ui.main.VmLifecycleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel(), VmLifecycleObserver {

    open class UiState<T>(
        val isLoading: Boolean = false,
        val isRefresh: Boolean = false,
        val isSuccess: T? = null,
        val isError: String? = null
    )

    val activity = MutableLiveData<FragmentActivity>()
    val viewModelProvider = MutableLiveData<ViewModelProvider>()

    val showLoading = MutableLiveData<Boolean>()
    val onRestart = MutableLiveData<Long>()
    val onNewIntent = MutableLiveData<Intent>()
    val onActivityResult = MutableLiveData<Map<String, Any?>>()

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?) {
        loge(owner, event?.name ?: "event")
    }

    override fun onCreate(owner: LifecycleOwner) {
        owner.apply {
            if (this is Fragment) {
                this@BaseViewModel.activity.value = activity
            } else if (this is FragmentActivity) {
                activity.value = this
            }
            initSpecialLifeCircle(owner)
//            lifecycleScope.launchWhenCreated { } //生命周期和Activity/Fragment是一致的, 太碎片化

        }
        activity.value?.lifecycleScope?.launchWhenCreated { }
        loge(owner, "onCreate")
//        viewModelScope.launch {  }
//        activity.value?.viewModelStore?.get

    }

    private fun initSpecialLifeCircle(owner: LifecycleOwner) {
        onRestart.observe(owner, Observer {
            onRestart()
        })
        onNewIntent.observe(owner, Observer {
            onNewIntent(it)
        })
        onActivityResult.observe(owner, Observer {
            onActivityResult(
                it["requestCode"] as? Int,
                it["resultCode"] as? Int,
                it["data"] as? Intent
            )
        })

    }

    @MainThread
    operator fun <T : ViewModel?> get(@NonNull modelClass: Class<T>): T? {
        return viewModelProvider.value?.get(modelClass)
    }

    open fun onRestart() {}
    open fun onNewIntent(intent: Intent) {}
    open fun onActivityResult(requestCode: Int?, resultCode: Int?, data: Intent?) {}

    override fun onStart(owner: LifecycleOwner) {
        loge(owner, "onStart")
        activity.value?.lifecycleScope?.launchWhenStarted { }
    }

    override fun onResume(owner: LifecycleOwner) {
        loge(owner, "onResume")
        activity.value?.lifecycleScope?.launchWhenResumed { }
    }

    override fun onPause(owner: LifecycleOwner) {
        loge(owner, "onPause")
    }

    override fun onStop(owner: LifecycleOwner) {
        loge(owner, "onStop")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        loge(owner, "onDestroy")
    }

    override fun onCleared() {
        activity.value = null
        viewModelProvider.value = null
        super.onCleared()
    }

    private fun loge(owner: LifecycleOwner?, msg: String) {
        Log.e("ViewModel impl use in" + owner?.javaClass?.simpleName, msg)
    }

    fun launchUI(block: suspend CoroutineScope.() -> Unit, isShowLoading: Boolean = true) =
        viewModelScope.launch {
            try {
                if (isShowLoading) showLoading.value = true
                block()
                showLoading.value = false
            } catch (e: Exception) {
                showLoading.value = false
            }
        }
}

