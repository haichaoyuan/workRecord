package com.shhxzq.ztb.ui.home.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter


class HomeMsgListFragmentPageAdapter2(fm: FragmentActivity, val fragmentList: List<Fragment>) :
    FragmentPagerAdapter(fm.supportFragmentManager) {
    override fun getCount(): Int {
        return if (fragmentList == null) 0 else fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }
}