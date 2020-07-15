package com.aos.app.ui.login.data


sealed class AResult<out T : Any?>(var code: String = "", var msg: String = "") {

    data class Success<out T : Any>(val data: T?) : AResult<T>()
    data class Error(val exception: Exception) : AResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}

inline fun <T : Any?> AResult<T>.checkResult(
    crossinline onSuccess: (T?) -> Unit,
    noinline onError: ((String?) -> Unit)? = null
) {
    if (this is AResult.Success) {
        onSuccess(data)
    } else if (this is AResult.Error) {
        onError?.invoke(exception.message)
    }
}

inline fun <T : Any?> AResult<T>.checkSuccess(success: (T?) -> Unit) {
    if (this is AResult.Success) {
        success(data)
    }
}