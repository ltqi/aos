package com.aos.app.ui.inverse

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.*
import com.aos.app.R
import com.aos.app.databinding.AInverseBindingBinding


/**
 * Created by:  qiliantao on 2020.06.10 18:59
 * email     :  qiliantao@mockuai.com
 * Describe  :  1. 点击View触发onClick事件中调用反向绑定接口，显然这种方式必须触发点击才能完成反向绑定
 *              2. 自定义反向绑定属性监听接口，放入view的tag中，自定义设置背景基本扩展方法，内部设置(更改)了背景，再调用tag中的接口(模拟监听)，
 *              在定义反向绑定静态方法中放入view中的tag(对象方法)被触发，在该方法中调用属性反向绑定监听(即InverseBindingListener)
 */
class InverseBindingActivity : AppCompatActivity() {

    private lateinit var binding: AInverseBindingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = AInverseBindingBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        binding = DataBindingUtil.setContentView(this, R.layout.a_inverse_binding)
//        binding.backgroud = Backgroud().apply { backgroundLevel.set(0) }
        binding.backgroud = Backgroud()


    }
}


class Backgroud {
    val backgroundLevel = ObservableInt(0)
    var bgLevel = 0

    val inverseStr = ObservableField("我的文字内容支持双向绑定\n点击切换背景色和文字内容")

    //    val bgResIds = arrayListOf(R.drawable.bg_inverse_1,R.drawable.bg_inverse_2,R.drawable.bg_inverse_1)
    fun viewClick(view: View) {
        bgLevel++
//        view.setBackgroundLevel(bgLevel % 5)

        view.background.level = bgLevel % 5
//        view.setBgLevel(bgLevel % 5)

//        view.setBackgroundResource(bgResIds[bgLevel % 3])
//        (view as? TextView)?.apply {
////            text = text.toString() + "$bgLevel"
//            text = ""
//        }
    }
}



@RestrictTo(RestrictTo.Scope.LIBRARY)
object MyBindingAdapter{

    @JvmStatic
    @BindingAdapter(value = ["cbackgroudLevel"], requireAll = false)
    fun setCbackgroudLevel(view: TextView, bgLeve: Int) {
        if (view.background.level != bgLeve) {
            view.background.level = bgLeve
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "cbackgroudLevel", event = "cbackgroudLevelAttrChanged")
    fun getCbackgroudLevel(view: TextView?) :Int? {
        return view?.background?.level
    }


//    @JvmStatic
//    @BindingAdapter(value = ["android:beforeTextChanged", "android:onTextChanged",
//        "android:afterTextChanged", "cbackgroudLevelAttrChanged"], requireAll = false)
//    fun bgLevelAttrChanged(view: View, before : TextViewBindingAdapter.BeforeTextChanged?,
//                           on: TextViewBindingAdapter.OnTextChanged?, after: TextViewBindingAdapter.AfterTextChanged?,
//                           cbackgroudLevelAttrChanged: InverseBindingListener?){
//        var newValue : TextWatcher?
//        if (cbackgroudLevelAttrChanged == null) {
//            newValue = null
//        } else {
//            newValue = object : TextWatcher {
//                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//                }
//
//                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                    on?.onTextChanged(s, start, before, count)
//                    cbackgroudLevelAttrChanged?.onChange()
//                }
//
//                override fun afterTextChanged(s: Editable) {
//
//                }
//            }
//        }
//        val oldValue = ListenerUtil.trackListener(view, newValue, R.id.textWatcher)
//        if (oldValue != null) {
//            (view as? TextView)?.removeTextChangedListener(oldValue)
//        }
//        if (newValue != null) {
//            (view as? TextView)?.addTextChangedListener(newValue)
//        }
//    }

    @JvmStatic
    @BindingAdapter(value = ["viewClick", "cbackgroudLevelAttrChanged"], requireAll = false)
    fun bgLevelAttrChanged(view: View, vc: ViewClickListener, cbackgroudLevelAttrChanged: InverseBindingListener?) {
        val onClickListener: View.OnClickListener? = if (cbackgroudLevelAttrChanged == null) {
            null
        } else {
            View.OnClickListener {
                vc.click(it)
                cbackgroudLevelAttrChanged?.onChange()
            }
        }
        view.setOnClickListener(onClickListener)
    }

//    @JvmStatic
//    @BindingAdapter(value = ["viewClick", "cbackgroudLevelAttrChanged"], requireAll = false)
//    fun bgLevelAttrChanged(view: View, vc: ViewClickListener, cbackgroudLevelAttrChanged: InverseBindingListener?) {
//        view.setTag(object : BackgroundLevelChangeListener {
//            override fun bgChanged() {
//                cbackgroudLevelAttrChanged?.onChange()
//            }
//        })
//        view.setOnClickListener {
//            vc.click(it)
//        }
//    }
}

interface ViewClickListener{
    fun click(view: View)
}


//fun View.setBgLevel(bgLeve: Int) {
//    if (background.level != bgLeve) {
//        background.level = bgLeve
//        (getTag() as? BackgroundLevelChangeListener)?.bgChanged()
//    }
//}
//
//interface BackgroundLevelChangeListener{
//    fun bgChanged()
//}