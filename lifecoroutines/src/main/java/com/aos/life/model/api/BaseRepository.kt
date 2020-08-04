package com.aos.life.model.api

import com.aos.life.model.bean.CResponse
import com.aos.life.model.bean.CResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.io.IOException


open class BaseRepository {

    suspend fun <T : Any> apiCall(call: suspend () -> CResponse<T>): CResponse<T> {
        return call.invoke()
    }

    suspend fun <T : Any> safeApiCall(call: suspend () -> CResult<T>, errorMessage: String): CResult<T> {
        return try {
            call()
        } catch (e: Exception) {
            // An exception was thrown when calling the API so we're converting this to an IOException
            CResult.Error(IOException(errorMessage, e))
        }
    }

    suspend fun <T : Any> executeResponse(response: CResponse<T>, successBlock: (suspend CoroutineScope.() -> Unit)? = null,
                                          errorBlock: (suspend CoroutineScope.() -> Unit)? = null): CResult<T> {
        return coroutineScope {
            if (response.errorCode == -1) {
                errorBlock?.let { it() }
                CResult.Error(IOException(response.errorMsg))
            } else {
                successBlock?.let { it() }
                CResult.Success("200", "成功", response.data)
            }
        }
    }
}

