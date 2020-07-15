package com.aos.app.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aos.app.R

/**
 * Created by:  qiliantao on 2020.05.17 18:59
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */
class EmptyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_empty)


    }
}