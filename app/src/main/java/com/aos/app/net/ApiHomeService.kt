package com.aos.app.net

import com.aos.app.dto.ArticleList
import com.aos.app.dto.Banner
import com.aos.app.dto.SystemParent
import com.aos.app.dto.WanResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by:  qiliantao on 2020.06.22 12:31
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */
interface ApiHomeService {


    @GET("/article/list/{page}/json")
    suspend fun getHomeArticles(@Path("page") page: Int): WanResponse<ArticleList>

    @GET("/banner/json")
    suspend fun getBanner(): WanResponse<List<Banner>>

    @GET("/tree/json")
    suspend fun getSystemType(): WanResponse<List<SystemParent>>

    @GET("/article/list/{page}/json")
    suspend fun getSystemTypeDetail(@Path("page") page: Int, @Query("cid") cid: Int): WanResponse<ArticleList>


}