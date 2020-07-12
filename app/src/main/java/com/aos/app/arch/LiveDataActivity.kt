package com.aos.app.arch

import android.content.Context
import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.aos.app.ui.base.MvvmActivity
import com.aos.app.R

class LiveDataActivity : MvvmActivity<ViewDataBinding>() {

    companion object {
        fun start(context: Context) {
            Intent(context, LiveDataActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }

    override fun getLayoutId() = R.layout.a_livedata

}
