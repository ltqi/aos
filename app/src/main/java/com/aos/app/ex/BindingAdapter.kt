package com.aos.app.ex

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter

/**
 * Created by:  qiliantao on 2020.04.26 11:50
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */


fun View.getBackgroundLevel() = background.level

@BindingAdapter("backgroundLevel")
fun View.setBackgroundLevel(backgroundLevel: Int?) {
    backgroundLevel?.let {
        if (background.level != it)
            background.level = it
    }
}


@BindingAdapter("text")
fun TextView.setTxt(text: CharSequence?) {
    this.text = text
}
