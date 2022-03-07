package com.example.module_homepage2.adapter;

import android.content.Context;


import com.example.module_homepage2.base.Advertise;
import com.example.module_homepage2.base.GAImageView;
import com.example.module_homepage2.base.UIUtils;

import java.util.List;


/**
 * 小 banner
 */
public class LittleAdBannerAdapter extends Advertise2Adapter {

    public LittleAdBannerAdapter(Context context, List<Advertise> advertises) {
        super(context, advertises);
    }

    @Override
    public float getPageWidth(int position) {
        return 0.8f;
    }

    @Override
    protected GAImageView getImageView(Advertise advertiseInfo, int index) {
        GAImageView imageView = super.getImageView(advertiseInfo, index);
        int right = UIUtils.dip2px(imageView.getContext(), 10);
        imageView.setPadding(0, 0, right, 0);
        return imageView;
    }
}
