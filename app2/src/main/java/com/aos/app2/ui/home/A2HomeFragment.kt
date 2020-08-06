package com.aos.app2.ui.home

import androidx.lifecycle.Observer
import com.aos.app2.BR
import com.aos.app2.R
import com.aos.app2.base.App2Fragment
import com.aos.app2.databinding.App2FragmentHomeBinding
import com.aos.app2.toast
import com.aos.app2.view.CustomLoadMoreView
import kotlinx.android.synthetic.main.app2_fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class A2HomeFragment : App2Fragment<App2FragmentHomeBinding>(R.layout.app2_fragment_home) {

    private val vModel: ListViewModel by viewModel()

    private val homeArticleAdapter by lazy { HomeArticleAdapter() }

    override fun startObserve() {
        dataBinding.setVM(BR.vm, vModel)
        vModel.uiState.observe(viewLifecycleOwner, Observer {
            it.showSuccess?.let { list ->
                homeArticleAdapter.run {
                    homeArticleAdapter.setEnableLoadMore(false)
                    if (it.isRefresh) replaceData(list.datas)
                    else addData(list.datas)
                    setEnableLoadMore(true)
                    loadMoreComplete()
                }
            }

            if (it.showEnd) homeArticleAdapter.loadMoreEnd()

            it.showError?.let { message ->
                toast(if (message.isBlank()) "网络异常" else message)
            }

        })

    }

    override fun initView() {
        dataBinding.run {
            adapter = homeArticleAdapter
        }
        initRecycleView()

    }

    private fun initRecycleView() {
        homeArticleAdapter.run {
            if (headerLayoutCount > 0) removeAllHeaderView()
            setLoadMoreView(CustomLoadMoreView())
            setOnLoadMoreListener({ loadMore() }, homeRecycleView)
        }
    }

    private fun loadMore() {
        vModel.getHomeArticleList(false)
    }

    override fun initData() {
        refresh()
    }

    fun refresh() {
        vModel.getHomeArticleList(true)
    }


}