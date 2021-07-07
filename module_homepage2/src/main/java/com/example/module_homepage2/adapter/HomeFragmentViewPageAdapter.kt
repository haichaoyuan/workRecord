package com.shhxzq.ztb.ui.home.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class HomeFragmentViewPageAdapter(fm: FragmentManager, val fragmentList: List<Fragment>) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT) {

    fun getItemCount(): Int {
        return if (fragmentList == null) 0 else fragmentList.size
    }

    fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return getItemCount()
    }

    override fun getItem(position: Int): Fragment {
        return createFragment(position)
    }

}