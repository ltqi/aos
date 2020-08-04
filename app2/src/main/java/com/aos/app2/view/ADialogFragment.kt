package com.aos.app2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.aos.app2.R

class ADialogFragment(var layoutResId: Int = R.layout.d_loading, var dataBinding: Boolean = false) : DialogFragment() {

    var viewDataBinding: ViewDataBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (dataBinding) {
            viewDataBinding = DataBindingUtil.inflate(layoutInflater, layoutResId, null, false)
            viewDataBinding?.root
        } else {
            inflater.inflate(layoutResId, null)
        }
    }
}