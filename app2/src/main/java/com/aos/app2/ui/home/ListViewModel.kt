package com.aos.app2.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aos.app2.api.HomeRepository
import com.aos.app2.api.SquareRepository
import com.aos.app2.bean.ArticleList
import com.aos.life.base.BaseViewModel
import com.aos.life.model.bean.CResult

open class ListViewModel(val homeRepository: HomeRepository, private val squareRepository: SquareRepository) : BaseViewModel() {


    private val _uiState = MutableLiveData<ArticleUiModel>()
    val uiState: LiveData<ArticleUiModel>
        get() = _uiState

    private var currentPage = 0

    sealed class ArticleType {
        object Home : ArticleType()
        object Square : ArticleType()
    }

    val refreshHome: () -> Unit = { getHomeArticleList(true) }
    val refreshSquare: () -> Unit = { getSquareArticleList(true)}

    fun getHomeArticleList(isRefresh: Boolean = false) = getArticleList(ArticleType.Home, isRefresh)
    fun getSquareArticleList(isRefresh: Boolean = false) = getArticleList(ArticleType.Square, isRefresh)


    private fun getArticleList(articleType: ArticleType, isRefresh: Boolean = false, cid: Int = 0) {
        emitArticleUiState(true)
        launch(block = {
            val result = when (articleType) {
                ArticleType.Home -> homeRepository.getArticleList(currentPage)
                ArticleType.Square -> squareRepository.getSquareArticleList(currentPage)
            }
            result
        }, resultFail = {
            if (it is CResult.Error) {
                emitArticleUiState(showLoading = false, showError = it.exception.message)
            }
        }, result = { result ->
                val articleList = result.data
                if (articleList.offset >= articleList.total) {
                    emitArticleUiState(showLoading = false, showEnd = true)
                    return@launch
                }
                currentPage++
                emitArticleUiState(
                    showLoading = false,
                    showSuccess = articleList,
                    isRefresh = isRefresh
                )
        })
    }

    private fun emitArticleUiState(
        showLoading: Boolean = false,
        showError: String? = null,
        showSuccess: ArticleList? = null,
        showEnd: Boolean = false,
        isRefresh: Boolean = false,
        needLogin: Boolean? = null
    ) {
        val uiModel =
            ArticleUiModel(showLoading, showError, showSuccess, showEnd, isRefresh, needLogin)
        _uiState.value = uiModel
    }

    data class ArticleUiModel(
        val showLoading: Boolean,
        val showError: String?,
        val showSuccess: ArticleList?,
        val showEnd: Boolean, // 加载更多
        val isRefresh: Boolean, // 刷新
        val needLogin: Boolean? = null
    )


//    private val _uiState = MutableLiveData<List<Navigation>>()
//    val uiState : LiveData<List<Navigation>>
//        get() = _uiState
//
////    val pagerAdapter  = MutableLiveData<SectionsPagerAdapter>()
//
//
//    fun getNavigation() {
//        launch(block = {
//            repository.getNavigation()
//        }, resultFail = {}, result = {
//            _uiState.value = it.data
//        })
//    }
}