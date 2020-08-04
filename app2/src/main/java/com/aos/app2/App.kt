package com.aos.app2

import android.app.Application
import android.content.Context
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aos.app2.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import kotlin.properties.Delegates

class App : Application() {

    companion object {
        var CONTEXT: Context by Delegates.notNull()
//        lateinit var CURRENT_USER: User
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }

}

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