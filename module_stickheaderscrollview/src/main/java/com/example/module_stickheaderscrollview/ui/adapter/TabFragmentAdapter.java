package com.example.module_stickheaderscrollview.ui.adapter;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.module_stickheaderscrollview.ui.fragment.RecyclerViewFragment;

/**
 * @author: JiangWeiwei
 * @time: 2017/11/9-11:33
 * @email:
 * @desc:
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {

    public TabFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
//        return ChildRecyclerViewFragment.newInstance(position);
        return RecyclerViewFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "点餐";
        }
        if (position == 1) {
            return "评价";
        }
        if (position == 2) {
            return "商家";
        }
        return "tab" + position;
    }
}
