package com.aos.app2.api

import com.aos.app2.bean.ArticleList
import com.aos.app2.bean.Banner
import com.aos.app2.bean.Navigation
import com.aos.life.model.api.BaseRepository
import com.aos.life.model.bean.CResponse
import com.aos.life.model.bean.CResult
import java.io.IOException

/**
 * Created by:  qiliantao on 2020.08.02
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */


class HomeRepository : BaseRepository() {

    suspend fun getArticleList(page: Int): CResult<ArticleList> {
        return safeApiCall(call = { requestArticleList(page) }, errorMessage = "")
    }

    private suspend fun requestArticleList(page: Int): CResult<ArticleList> =
        executeResponse(CRetrofitClient.service.getHomeArticles(page))
}

class SquareRepository :  BaseRepository(){


    suspend fun getSquareArticleList(page:Int): CResult<ArticleList> {
        return safeApiCall(call = {requestSquareArticleList(page)},errorMessage = "网络异常")
    }

    private suspend fun requestSquareArticleList(page: Int): CResult<ArticleList> {
//        return executeResponse(CRetrofitClient.service.getSquareArticleList(page))
        val response = CRetrofitClient.service.getSquareArticleList(page)
        return if (response.isSuccess()) CResult.Success(data = response.data)
        else CResult.Error(IOException(response.errorMsg))

    }

}
fun CResponse<Any>.isSuccess(): Boolean = this.errorCode == 0


class NavigationRepository : BaseRepository() {

    suspend fun getNavigation(): CResult<List<Navigation>> {
        return safeApiCall(call = { requestNavigation() }, errorMessage = "获取数据失败")
    }


    private suspend fun requestNavigation(): CResult<List<Navigation>> =
        executeResponse(CRetrofitClient.service.getNavigation())
}