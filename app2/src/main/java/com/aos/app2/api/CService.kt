package com.aos.app2.api

import com.aos.app2.bean.*
import com.aos.life.model.bean.*
import retrofit2.http.*


interface CService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }

    @GET("/article/list/{page}/json")
    suspend fun getHomeArticles(@Path("page") page: Int): CResponse<ArticleList>

    @GET("/banner/json")
    suspend fun getBanner(): CResponse<List<Banner>>

    @GET("/tree/json")
    suspend fun getSystemType(): CResponse<List<SystemParent>>

    @GET("/article/list/{page}/json")
    suspend fun getSystemTypeDetail(@Path("page") page: Int, @Query("cid") cid: Int): CResponse<ArticleList>

    @GET("/navi/json")
    suspend fun getNavigation(): CResponse<List<Navigation>>

    @GET("/project/tree/json")
    suspend fun getProjectType(): CResponse<List<SystemParent>>

    @GET("/wxarticle/chapters/json")
    suspend fun getBlogType(): CResponse<List<SystemParent>>

    @GET("/wxarticle/list/{id}/{page}/json")
    fun getBlogArticle(@Path("id") id: Int, @Path("page") page: Int): CResponse<ArticleList>

    @GET("/project/list/{page}/json")
    suspend fun getProjectTypeDetail(@Path("page") page: Int, @Query("cid") cid: Int): CResponse<ArticleList>

    @GET("/article/listproject/{page}/json")
    suspend fun getLastedProject(@Path("page") page: Int): CResponse<ArticleList>

    @GET("/friend/json")
    suspend fun getWebsites(): CResponse<List<Hot>>

    @GET("/hotkey/json")
    suspend fun getHot(): CResponse<List<Hot>>

    @FormUrlEncoded
    @POST("/article/query/{page}/json")
    suspend fun searchHot(@Path("page") page: Int, @Field("k") key: String): CResponse<ArticleList>

    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(@Field("username") userName: String, @Field("password") passWord: String): CResponse<User>

    @GET("/user/logout/json")
    suspend fun logOut(): CResponse<Any>

    @FormUrlEncoded
    @POST("/user/register")
    suspend fun register(@Field("username") userName: String, @Field("password") passWord: String, @Field("repassword") rePassWord: String): CResponse<User>

    @GET("/lg/collect/list/{page}/json")
    suspend fun getCollectArticles(@Path("page") page: Int): CResponse<ArticleList>

    @POST("/lg/collect/{id}/json")
    suspend fun collectArticle(@Path("id") id: Int): CResponse<ArticleList>

    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun cancelCollectArticle(@Path("id") id: Int): CResponse<ArticleList>

    @GET("/user_article/list/{page}/json")
    suspend fun getSquareArticleList(@Path("page") page: Int): CResponse<ArticleList>

    @FormUrlEncoded
    @POST("/lg/user_article/add/json")
    suspend fun shareArticle(@Field("title") title: String, @Field("link") url: String): CResponse<String>

}