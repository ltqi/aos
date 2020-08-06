package com.aos.app2.ui.dashboard

import androidx.lifecycle.Observer
import com.aos.app2.BR
import com.aos.app2.R
import com.aos.app2.base.App2Fragment
import com.aos.app2.base.BaseBindAdapter
import com.aos.app2.bean.Article
import com.aos.app2.databinding.FragmentDashboardBinding
import com.aos.app2.toast
import com.aos.app2.view.CustomLoadMoreView
import kotlinx.android.synthetic.main.app2_fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : App2Fragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {

    private val vModel: DashboardViewModel by viewModel()

    private val squareAdapter by lazy {
        BaseBindAdapter<Article>(
            R.layout.item_square_constraint,
            BR.article
        )
    }

    override fun startObserve() {
        getVM<DashboardViewModel>()
        dataBinding.setVM(BR.vm, vModel)
        vModel.uiState.observe(viewLifecycleOwner, Observer {
            it.showSuccess?.let { list ->
                squareAdapter.run {
                    squareAdapter.setEnableLoadMore(false)
                    if (it.isRefresh) replaceData(list.datas)
                    else addData(list.datas)
                    setEnableLoadMore(true)
                    loadMoreComplete()
                }
            }

            if (it.showEnd) squareAdapter.loadMoreEnd()

            it.showError?.let { message ->
                toast(if (message.isBlank()) "网络异常" else message)
            }

        })

    }

    override fun initView() {
        dataBinding.run {
            vm = vModel
            adapter = squareAdapter
        }
        initRecycleView()

    }

    private fun initRecycleView() {
        squareAdapter.run {
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