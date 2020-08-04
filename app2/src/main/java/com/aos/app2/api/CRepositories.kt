package com.aos.app2.api

import com.aos.app2.bean.ArticleList
import com.aos.app2.bean.Banner
import com.aos.app2.bean.Navigation
import com.aos.life.model.api.BaseRepository
import com.aos.life.model.bean.CResult

/**
 * Created by:  qiliantao on 2020.08.02
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */

class NavigationRepository : BaseRepository() {

    suspend fun getNavigation(): CResult<List<Navigation>> {
        return safeApiCall(call = { requestNavigation() }, errorMessage = "获取数据失败")
    }


    private suspend fun requestNavigation(): CResult<List<Navigation>> =
        executeResponse(CRetrofitClient.service.getNavigation())
}

class HomeRepository : BaseRepository() {

    suspend fun getArticleList(page: Int): CResult<ArticleList> {
        return safeApiCall(call = { requestArticleList(page) }, errorMessage = "")
    }

    private suspend fun requestArticleList(page: Int): CResult<ArticleList> =
        executeResponse(CRetrofitClient.service.getHomeArticles(page))
}