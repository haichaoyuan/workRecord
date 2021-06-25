package com.shhxzq.ztb.ui.home.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeFragmentViewPageAdapter(fm: FragmentActivity, val fragmentList: List<Fragment>) :
    FragmentStateAdapter(fm) {

    override fun getItemCount(): Int {
        return if (fragmentList == null) 0 else fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}