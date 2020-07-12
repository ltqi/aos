package com.aos.app.ui.coroutines

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.aos.app.ui.base.MvvmActivity
import com.aos.app.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoroutinesActivity : MvvmActivity<ViewDataBinding>() {

    companion object {
        fun start(context: Context) {
            Intent(context, CoroutinesActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }

    override fun getLayoutId() = R.layout.a_coroutines

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.container_coroutines, CoroutinesFragment.newInstance())
//                    .commitNow()
//        }

//        TestLeak.activityList.add(this)

//        lifecycleScope.launchWhenCreated {
//            val retV = getTextFromNet()
////            show(retV.toString())
//        }
//        lifecycleScope.launch {
//            whenStarted {
//
//            }
//        }
    }


    private suspend fun getTextFromNet() = withContext(Dispatchers.IO) {
        request("github.com")
    }

    private fun request(url: String) = Api.request(url)
}
