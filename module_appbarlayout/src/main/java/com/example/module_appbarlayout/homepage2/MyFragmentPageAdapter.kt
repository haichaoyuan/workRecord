package com.example.module_appbarlayout.homepage2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyFragmentPageAdapter(fm: FragmentActivity, val fragmentList: List<Fragment>) : FragmentStateAdapter(fm) {

//    override fun getCount(): Int {
//        return if (fragmentList == null) 0 else fragmentList.size
//    }
//
//    override fun getItem(i: Int): Fragment {
//        return fragmentList[i]
//    }

    override fun getItemCount(): Int {
        return if (fragmentList == null) 0 else fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}