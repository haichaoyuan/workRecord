package com.shhxzq.ztb.ui.home.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class HomeFragmentViewPageAdapter(fm: FragmentManager, val size: Int, val createFragment:(position: Int) -> Fragment) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT) {

    fun getItemCount(): Int {
        return size
    }

    override fun getCount(): Int {
        return getItemCount()
    }

    override fun getItem(position: Int): Fragment {
        return createFragment(position)
    }

}