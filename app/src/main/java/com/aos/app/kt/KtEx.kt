package com.aos.app.kt

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Created by:  qiliantao on 2020.04.20 17:20
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */

fun Activity.show(msg: String) {
    Toast.makeText(this.applicationContext, msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.show(msg: String) {
    Toast.makeText(this.activity?.applicationContext, msg, Toast.LENGTH_SHORT).show()
}

