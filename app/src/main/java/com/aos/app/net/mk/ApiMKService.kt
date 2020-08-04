package com.aos.app.net.mk

import com.aos.app.App
import com.aos.app.ut.getRefreshToken
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by:  qiliantao on 2020.06.22 12:24
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */
interface ApiMKService {

    companion object {
        const val BASE_URL_MK = "https://api.mockuai.com"
    }

    @POST("/ec/b/user/shop/login")
    suspend fun login(@Body map: Map<String, Any?>): MKResult<MKUserInfo?>

    @GET("/wdzg/auth/session_token/get")
    fun refreshSessionToken(): MKResult<MKSessionEntity?>

    @POST("/wdzg/auth/access_token/refresh")
    fun refreshAccessToken(@Body map: Map<String, Any?> = mutableMapOf(Pair("refresh_token", App.sp.getRefreshToken() ?: ""))): MKResult<Any?>


}

open class MKResult<T>(
    var code: String? = "",
    var msg: String? = "",
    var data: T? = null,

    var accessToken: String? = null,
    var refreshToken: String? = null,

    var isSuccess: Boolean = code == ServerCode.SUCCESS,
    var exception: Throwable? = null
)


data class MKUserInfo(
    var user: User?
) {
    data class User(
        var accId: String?, // shop50799746
        var bizCode: String?, // mokuaitv
        var id: Int?, // 50799746
        var imgUrl: String?, //
        var mobile: String?, // 15600159691
        var name: String?, // MKO1
        var roleMark: Int?, // 6
        var sid: String?, // 2d305a4af70e2bfb402b20fbe32caa54
        var type: Int?, // 0
        var userExtraInfo: UserExtraInfo?
    ) {
        data class UserExtraInfo(
            var area: String?, // 湖北-襄阳市
            var bizCode: String?, // mokuaitv
            var deleteMark: Int?, // 0
            var deleteTimestamp: Int?, // 0
            var gmtCreated: String?, // 2019-11-27 13:43:43
            var gmtModified: String?, // 2019-11-27 13:43:43
            var headImgUrl: String?, //
            var id: Int?, // 101221482
            var lastLoginTime: String?, // 2020-07-14 16:55:07
            var lastRemoteId: String?, // 220.191.163.138
            var mobile: String?, // 15600159691
            var nickName: String?, // MKO1
            var registerTime: String?, // 2019-11-27 13:43:43
            var sex: Int?, // 1
            var type: Int?, // 1
            var userId: Int? // 50799746
        )
    }
}

inline fun <T : Any?> MKResult<T>.doResult(
    crossinline onSuccess: (T?) -> Unit,
    noinline onError: ((String?, Throwable?) -> Unit)? = null
) {
    if (this.isSuccess) {
        onSuccess(data)
    } else {
        onError?.invoke(msg, exception)
    }
}