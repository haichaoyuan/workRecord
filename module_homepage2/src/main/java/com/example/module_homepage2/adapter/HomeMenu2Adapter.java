package com.example.module_homepage2.adapter;

import android.content.Context;


import com.example.module_homepage2.R;
import com.example.module_homepage2.base.AppMenuRes;

import java.util.List;

/**
 * Des:
 * Author: Luke
 * Data: 17/2/16
 */
public class HomeMenu2Adapter extends HomeMenuAdapter {
    public HomeMenu2Adapter(Context ctx, List<AppMenuRes> objects) {
        super(ctx, objects);
        drawables = new int[]{R.drawable.ico_menu_mystock, R.drawable.ico_menu_stockindex, R.drawable.ico_menu_ranking,
                R.drawable.ico_menu_hotblock, R.drawable.ico_menu_ipo, R.drawable.ico_menu_winnerlist,
                R.drawable.ico_menu_oa, R.drawable.ico_menu_trade, R.drawable.ico_menu_trade, R.drawable.ico_menu_trade};

    }
}
