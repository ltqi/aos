package com.aos.app2.ui.home.tab

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(val titles: MutableList<String> = mutableListOf(), fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT) {



    override fun getItem(position: Int): Fragment {
        return PlaceholderFragment.newInstance(
            position + 1
        )
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getCount(): Int {
        return titles.size
    }
}