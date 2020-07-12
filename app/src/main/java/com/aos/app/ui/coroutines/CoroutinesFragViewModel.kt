package com.aos.app.ui.coroutines

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import com.aos.app.ui.base.BaseViewModel
import kotlinx.coroutines.*

class CoroutinesFragViewModel(application: Application) : BaseViewModel(application) {

//    val noteDeferred = CompletableDeferred<Book>()

    val content = MutableLiveData<String>()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        viewModelScope.launch/*(Dispatchers.Default)*/ {
            showLoading.value = true
            content.value = getTextFromNet().toString()
            showLoading.value = false
        }
    }

    private suspend fun getTextFromNet() = withContext(Dispatchers.IO) {
        println("------------" + Thread.currentThread())
        delay(1000)
        Api.request("github.com")
    }


//    private suspend fun getTextFromNet() : Book {
//        delay(2000)
//        return request("github.com")
//    }

    fun update(view: View) {
        content.value = "setValue按钮更新的内容"

//        view.postDelayed({
//            content.postValue("postValue按钮更新的内容")
//        }, 0)
//        view.postDelayed({
//            content.value = "setValue按钮更新的内容"
//        }, 15)

    }


    private val userId: LiveData<String> = MutableLiveData()
    val user = userId.switchMap { id ->
        liveData/*(context = viewModelScope.coroutineContext + Dispatchers.IO)*/ {
//            emit(database.loadUserById(id))
            emit(getUser(id))
        }
    }

    private suspend fun getUser(id: String)  = "name = zs, age=20"
}
