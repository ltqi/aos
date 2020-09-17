package com.aos.app2.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.aos.app2.R


class ADialogFragment(var layoutResId: Int = R.layout.d_loading, var dataBinding: Boolean = false) :
    DialogFragment() {

    var viewDataBinding: ViewDataBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            decorView.setPadding(0, 0, 0, 0)
            val lp: WindowManager.LayoutParams = attributes
            lp.dimAmount = 0.0f
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            attributes = lp
        }

        return if (dataBinding) {
            viewDataBinding = DataBindingUtil.inflate(layoutInflater, layoutResId, null, false)
            viewDataBinding?.root
        } else {
            inflater.inflate(layoutResId, null)
        }
    }

}