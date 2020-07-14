package com.aos.app.net

import com.aos.app.dto.AResponse
import com.aos.app.ui.login.data.model.UserInfo
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by:  qiliantao on 2020.06.22 12:24
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */
interface ApiAuthService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }

    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(@Field("username") userName: String, @Field("password") passWord: String): AResponse<UserInfo?>

    @GET("/user/logout/json")
    suspend fun logout(): AResponse<Any>

    @FormUrlEncoded
    @POST("/user/register")
    suspend fun register(@Field("username") userName: String, @Field("password") passWord: String, @Field("repassword") rePassWord: String): AResponse<UserInfo>


}