package com.aos.app.ui.login.data

import com.aos.app.dto.AResponse
import com.aos.app.net.ApiAuthService
import com.aos.app.net.mk.ApiMKService
import com.aos.app.net.mk.MKResult
import com.aos.app.net.mk.UserData
import com.aos.app.net.mk.MRetrofit
import com.aos.app.ui.login.data.model.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.io.IOException


class LoginRepository(private val authService: ApiAuthService) {

    var user: UserInfo? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    suspend fun logout() {
        user = null
        authService.logout()
    }

    suspend fun login(username: String, password: String): AResult<UserInfo?> {
        // handle login
        val result = authService.login(username, password)
        return handleResponse(result, {
            user = result.data
        })
    }

//    val apiMKService by lazy { ARetrofit.getApiService<ApiMKService>(ApiMKService.BASE_URL_MK) }
    val apiMKService by lazy { MRetrofit.getApiService<ApiMKService>(
    ApiMKService.BASE_URL_MK) }
    suspend fun loginMK(account: String, pwd: String) : MKResult<UserData?> {
        return apiMKService.login(mutableMapOf<String, Any?>().apply {
            put("mobile", account)
            put("password", pwd)
        })
    }

    suspend fun <T : Any> handleResponse(response: AResponse<T?>, successBlock: (suspend CoroutineScope.() -> Unit)? = null,
                                          errorBlock: (suspend CoroutineScope.() -> Unit)? = null): AResult<T?> {
        return coroutineScope {
            if (response.errorCode == -1) {
                errorBlock?.let { it() }
                AResult.Error(IOException(response.errorMsg))
            } else {//wanandroid中 errorCode == 0 表示返回正确的数据
                successBlock?.let { it() }
                AResult.Success(response.data)
            }
        }
    }
}