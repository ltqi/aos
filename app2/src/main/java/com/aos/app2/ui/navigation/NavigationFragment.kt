package com.aos.app2.ui.navigation

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.aos.app2.R
import com.aos.app2.base.App2Fragment
import com.aos.app2.bean.Article
import com.aos.app2.bean.Navigation
import com.aos.app2.databinding.FragmentNavigationBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.q.util.dp2px
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.fragment_navigation.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView
import q.rorbin.verticaltablayout.widget.TabView

class NavigationFragment : App2Fragment<FragmentNavigationBinding>(R.layout.fragment_navigation) {

    private val navigationViewModel by viewModel<NavigationViewModel>()

    private val navigationList = mutableListOf<Navigation>()
    private val tabAdapter by lazy { VerticalTabAdapter(navigationList.map { it.name }) }
    private val navigationAdapter by lazy { NavigationAdapter() }

    override fun initView() {
        dataBinding.adapter = navigationAdapter
        initTabLayout()
    }

    private fun initTabLayout() {
        tabLayout.addOnTabSelectedListener(object : VerticalTabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabView?, position: Int) {
            }

            override fun onTabSelected(tab: TabView?, position: Int) {
                scrollToPosition(position)
            }
        })
    }

    private fun scrollToPosition(position: Int) {
        val mLayoutManager = dataBinding.navigationRecycleView.layoutManager as LinearLayoutManager
        val firstPotion = mLayoutManager.findFirstVisibleItemPosition()
        val lastPosition = mLayoutManager.findLastVisibleItemPosition()
        when {
            position <= firstPotion || position >= lastPosition -> navigationRecycleView.smoothScrollToPosition(
                position
            )
            else -> navigationRecycleView.run {
                smoothScrollBy(
                    0,
                    this.getChildAt(position - firstPotion).top - dp2px(requireContext(), 8f)
                )
            }
        }
    }

    override fun initData() {
        navigationViewModel.getNavigation()
    }

    private fun getNavigation(navigationList: List<Navigation>) {
        this.navigationList.clear()
        this.navigationList.addAll(navigationList)
        tabLayout.setTabAdapter(tabAdapter)

        navigationAdapter.setNewData(navigationList)
    }

    override fun startObserve() {
        navigationViewModel.run {
            uiState.observe(viewLifecycleOwner, Observer {
                it?.let { getNavigation(it) }
            })
        }
    }
}

class VerticalTabAdapter(private val titles: List<String>) : TabAdapter {

    override fun getIcon(position: Int) = null

    override fun getBadge(position: Int) = null

    override fun getBackground(position: Int) = -1

    override fun getTitle(position: Int): ITabView.TabTitle {
        return ITabView.TabTitle.Builder()
            .setContent(titles[position])
            .setTextColor(-0xc94365, -0x8a8a8b)
            .build()
    }

    override fun getCount() = titles.size
}


class NavigationAdapter(layoutResId: Int = R.layout.item_navigation) :
    BaseQuickAdapter<Navigation, BaseViewHolder>(layoutResId) {

    override fun convert(helper: BaseViewHolder, item: Navigation) {

        helper.setText(R.id.navigationName, item.name)
        helper.getView<TagFlowLayout>(R.id.navigationTagLayout).run {
            adapter = object : TagAdapter<Article>(item.articles) {
                override fun getCount(): Int {
                    return item.articles.size
                }

                override fun getView(
                    parent: com.zhy.view.flowlayout.FlowLayout,
                    position: Int,
                    t: Article
                ): View {
                    val tv = LayoutInflater.from(parent.context)
                        .inflate(R.layout.tag, parent, false) as TextView
                    tv.text = t.title
                    return tv
                }
            }

            setOnTagClickListener { view, position, _ ->

                true
            }
        }
    }

}