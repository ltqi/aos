package com.aos.app2.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aos.app2.R

class A2MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app2_main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, A2MainFragment.newInstance())
                .commitNow()
        }
    }
}
