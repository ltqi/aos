package com.aos.app.ex

/**
 * Created by:  qiliantao on 2020.04.26 16:28
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */

inline fun  Boolean.isTrue(trueRet: () -> String, falseRet: () -> String) : String {
    return if (this) trueRet() else falseRet()
}


fun <T> T.ifNullorNot(nullExecute: () -> Unit, notNullExecute: (T.() -> Unit)? = null) : T? {
    if (this == null) {
        nullExecute()
    }
    notNullExecute?.apply {
        notNullExecute()
    }
    return this
}
