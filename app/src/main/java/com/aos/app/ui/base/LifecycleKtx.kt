package com.aos.app.ui.base

import android.Manifest
import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aos.app.toast
import com.permissionx.guolindev.PermissionX

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

fun AppCompatActivity.checkPermissions(allGrantedCall: (() -> Unit)? = null,
                                       notAllGrantedCall: ((deniedList: List<String>?) -> Unit)? = null) {
    PermissionX.init(this)
        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE)
        .onExplainRequestReason { scope, deniedList ->
            scope.showRequestReasonDialog(deniedList, "核心功能需要使用这些到这些权限，如拒绝授权会无法使用", "确定", "取消")
        }
        .request { allGranted, _, deniedList ->
            if (allGranted) {
                allGrantedCall?.invoke()
            } else {
                notAllGrantedCall?.invoke(deniedList)
            }
        }
}