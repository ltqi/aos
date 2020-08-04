package com.aos.app2.ui.home

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aos.app2.R
import com.aos.app2.base.App2Fragment
import com.aos.app2.bean.Navigation
import com.aos.app2.databinding.FragmentHomeBinding
import com.aos.app2.ui.home.tab.SectionsPagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class A2HomeFragment : App2Fragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val vModel: A2HomeViewModel by viewModel()
//    private val vModel: HomeViewModel = getVM()

//    private val titleList = arrayOf("首页", "广场", "最新项目", "体系", "导航")
//    private val fragmentList = arrayListOf<Fragment>()
//    private val homeFragment by lazy { HomeFragment() } // 首页
//    private val squareFragment by lazy { SquareFragment() } // 广场
//    private val lastedProjectFragment by lazy { ProjectTypeFragment.newInstance(0, true) } // 最新项目

    override fun startObserve() {
        getVM<A2HomeViewModel>()
        vModel.uiState.observe(viewLifecycleOwner, Observer {
            it?.let { getNavigation(it) }
        })
    }

    override fun initView() {
        dataBinding.run {
            vm = vModel
        }

        vModel.pagerAdapter.value = SectionsPagerAdapter(
            mutableListOf("TAB1","TAB2"),
//            mutableListOf(),
            requireActivity().supportFragmentManager
        )

        dataBinding.tabs.setupWithViewPager(dataBinding.viewPager)

    }

    override fun initData() {
        vModel.getNavigation()

    }
    private val navigationList = mutableListOf<Navigation>()

    private fun getNavigation(navigationList: List<Navigation>) {
        this.navigationList.clear()
        this.navigationList.addAll(navigationList)
        val titles = navigationList.map { it.name }.take(3).toMutableList()
        vModel.pagerAdapter.value = SectionsPagerAdapter(
            titles,
            requireActivity().supportFragmentManager
        )

//        tabLayout.setTabAdapter(tabAdapter)
//
//        navigationAdapter.setNewData(navigationList)
    }

}