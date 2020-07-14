package com.aos.app.ui.main

import android.app.Application
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.aos.app.kt.show
import com.aos.app.ui.base.*
import com.aos.app.ui.inverse.InverseBindingActivity
import com.aos.app.ui.coroutines.CoroutinesActivity
import com.aos.app.ui.origin.OriginVmActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainFragViewModel : BaseViewModel() {

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    val backgroundLevel = MutableLiveData<Int>().apply { value = 0 }
    val title = ObservableField<String>().apply { set("我是标题") }
    val content = MutableLiveData<String>("主页面内容")
//    val singleLiveEvent = SingleLiveEvent<String>("single")

    var color = 0

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        EventBus.getDefault().register(this)
//        this[MainFragViewModel::class.java]?.content
        title.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
//                (owner as Fragment).apply {
//                    show("标题的观察者们收到通知了：" + (sender as? ObservableField<String>)?.get())
//                }
            }
        })
//        singleLiveEvent.observe(owner, Observer {
//            (owner as Fragment).apply {
//                startActivity(Intent(requireContext(), EmptyActivity::class.java))
//            }
//        })
    }

    val b = true
    var str: String? = null
    fun btOnClick(view: View) {
        color++
        backgroundLevel.value = color % 5

    }


    fun toEmpty(view: View) {
        Intent(view.context, EmptyActivity::class.java).apply {
            view.context.startActivity(this)
        }
//        view.postDelayed({
//            title.set("我是标题 我变了")
//            content.value = content.value + " 主页面也变了"
//        }, 2000)
    }


    fun toOriginVm(view: View) {
//        OriginVmActivity.start(view.context)
        Intent(view.context, OriginVmActivity::class.java).apply {
            view.context.startActivity(this)
        }

    }

    fun toCoroutines(view: View) {
        CoroutinesActivity.start(view.context)

    }

    fun toInverseBinding(view: View) {
        Intent(view.context, InverseBindingActivity::class.java).apply {
            view.context.startActivity(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun dataChanged(str : String) {
        title.set("我是标题 我变了")
        content.value = " 主页面 也变了"
//        singleLiveEvent.value = "我变了"

    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        EventBus.getDefault().unregister(this)
    }

}
