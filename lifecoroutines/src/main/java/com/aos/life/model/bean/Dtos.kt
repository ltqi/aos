package com.aos.life.model.bean


data class CResponse<out T>(val errorCode: Int, val errorMsg: String, val data: T)



sealed class CResult<out T : Any> {

    data class Success<out T : Any>(var code: String = "", var msg: String = "", val data: T) : CResult<T>()
    data class Error(val exception: Exception) : CResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}



inline fun <T : Any> CResult<T>.checkRet(
    crossinline onSuccess: (CResult.Success<T>) -> Unit,
    noinline onError: ((String?) -> Unit)? = null
) {
    if (this is CResult.Success) {
        onSuccess(this)
    } else if (this is CResult.Error) {
        onError?.invoke(exception.message)
    }
}


inline fun <T : Any> CResult<T>.checkResult(
    crossinline onSuccess: (T) -> Unit,
    noinline onError: ((String?) -> Unit)? = null
) {
    if (this is CResult.Success) {
        onSuccess(data)
    } else if (this is CResult.Error) {
        onError?.invoke(exception.message)
    }
}

inline fun <T : Any> CResult<T>.checkSuccess(success: (T?) -> Unit) {
    if (this is CResult.Success) {
        success(data)
    }
}
