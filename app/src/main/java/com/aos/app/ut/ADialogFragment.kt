package com.aos.app.ut

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.aos.app.R

/**
 * Created by:  qiliantao on 2020.07.10 14:19
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */
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