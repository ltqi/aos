package com.aos.app.net.mk

import com.aos.app.App
import com.aos.app.ut.saveAccessToken
import com.aos.app.ut.saveRefreshToken
import com.aos.app.ut.saveSessionInfo
import com.q.lib.BuildConfig
import com.q.net.BaseRetrofitClient
import com.q.net.InjectCommonParamsInterceptor
import com.q.net.injectParams
import com.q.net.newBuilder
import com.q.util.GsonHelper
import com.q.util.body2String
import com.q.util.formatJson
import com.q.util.i
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.Buffer
import retrofit2.Retrofit


/**
 * Created by:  qiliantao on 2020.06.22 11:52
 * email     :  qiliantao@mockuai.com
 * Describe  :  Retrofit okHttpClient封装
 */
object MRetrofit : BaseRetrofitClient() {

    val refreshTokenLock = Any()

    private val buffer = Buffer()
    override fun builderOkHttpClient(builder: OkHttpClient.Builder) {
        builder.addInterceptor(
            InjectCommonParamsInterceptor(
                CommonRequestParams.commonParams
            )
        )
        if (BuildConfig.DEBUG) {
            builder.addInterceptor {
                val request = it.request()
                val t1 = System.nanoTime()
                buffer.clear()
                request.body?.writeTo(buffer)
                i(" =============================Request=======================" +
                            String.format(" \n %s to %s%n%s Body = %s", request.url, it.connection(), request.headers, formatJson(buffer.readUtf8())))
                val response = it.proceed(request)
                val t2 = System.nanoTime()
                i(" =============================Response======================= " +
                            String.format(" \n from %s spend %.1fms%n%s Body = %s", response.request.url, (t2 - t1) / 1e6, response.headers, response.body?.body2String()))
                response
            }
        }
        builder.addInterceptor(AccessTokenRefreshInterceptor())
    }

    override fun builderRetrofit(builder: Retrofit.Builder) {}


}

class AccessTokenRefreshInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originRequest = chain.request()

        val response = chain.proceed(originRequest)
        if (response.isSuccessful) {
            val result = GsonHelper.fromJson<MKResult<String?>>(
                response.body?.body2String(), GsonHelper.findType(
                    MKResult::class.java, String::class.java
                )
            )
            when (result?.code) {
                ServerCode.SESSION_TOKEN_INVALID -> {
                    synchronized(MRetrofit.refreshTokenLock) {
                        val sessionTokenResult =
                            MRetrofit.getApiService<ApiMKService>().refreshSessionToken()
                        val sessionToken = sessionTokenResult.data?.sessionToken
                        return if (sessionTokenResult.isSuccess && sessionToken != null) {
                            App.sp.saveSessionInfo(sessionToken)
                            val requestBuilder = originRequest.newBuilder()
                                .injectParams(originRequest, CommonRequestParams.commonParams)
                            chain.proceed(requestBuilder.build())
                        } else {
                            Response.Builder().newBuilder(response).message("获取sessionToken失败")
                                .build()
                        }
                    }
                }
                ServerCode.ACCESS_TOKEN_INVALID -> {
                    synchronized(MRetrofit.refreshTokenLock) {
                        val accessTokenResult =
                            MRetrofit.getApiService<ApiMKService>().refreshAccessToken()
                        val accessToken = accessTokenResult.accessToken
                        val refreshToken = accessTokenResult.refreshToken
                        return if (accessTokenResult.isSuccess && accessToken != null && refreshToken != null) {
                            App.sp.saveAccessToken(accessToken)
                            App.sp.saveRefreshToken(refreshToken)
                            val requestBuilder = originRequest.newBuilder()
                                .injectParams(originRequest, CommonRequestParams.commonParams)
                            chain.proceed(requestBuilder.build())
                        } else {
                            Response.Builder().newBuilder(response).message("获取accessToken失败")
                                .build()
                        }
                    }
                }
                ServerCode.REFRESH_TOKEN_INVALID -> {
                    return Response.Builder().newBuilder(response).message("token已失效，请重新登录").build()
                }
                ServerCode.REQUEST_TIME_OUT -> {
                    return Response.Builder().newBuilder(response).message("请求超时").build()
                }
            }
        }

        return response
    }

}
