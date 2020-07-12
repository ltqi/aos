package com.aos.app.ui.origin

import android.app.Application
import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aos.app.BR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus

/**
 * Created by:  qiliantao on 2020.05.17 17:28
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */
class OriginViewModel(application: Application) : AndroidViewModel(application) {

    val users: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().apply {
            value = getUsers()
//            viewModelScope.launch { value = loadUsers() }
            requestUsers(this)
        }
    }

    fun requestUsers(users: MutableLiveData<List<User>>) {
        viewModelScope.launch { users.value = loadUsers() }
    }

    var updateCount = 0
    private fun getUsers(): List<User> {
        val list = mutableListOf<User>()
        if (updateCount % 2 == 0) {
            list.add(User().apply {
                name = "张三"
                age = 20
            })
        } else {
            list.add(User().apply {
                name = "李四"
                age = 22
            })
        }
        updateCount++
        return list
    }

    private suspend fun loadUsers() = withContext(Dispatchers.Main) {
        delay(200)
        getUsers()
    }


    fun update(view: View) {
        requestUsers(users)
        EventBus.getDefault().post("数据刷新")
//        (view.context as? Activity)?.finish()
    }
}

class User : BaseObservable() {

    @get:Bindable
    var name: String = ""
        //        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    var age: Int = 0
        @Bindable
//    get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.age)
        }

    override fun toString(): String {
        return "姓名：$name, 年龄：$age"
    }

}