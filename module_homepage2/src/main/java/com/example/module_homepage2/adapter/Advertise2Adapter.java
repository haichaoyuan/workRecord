package com.example.module_homepage2.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.module_homepage2.R;
import com.example.module_homepage2.base.Advertise;
import com.example.module_homepage2.base.GAImageView;
import com.example.module_homepage2.helper.GlideRoundTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Des:首页广告
 * 广告图片圆形矩形
 * Author: Luke
 * Data: 17/5/24
 */
public class Advertise2Adapter extends PagerAdapter {
    private Context context;
    private List<Advertise> advertises;
    private List<ImageView> views;
    private int firstAd = 0; // 第一个图片，0开始
    private int lastAd = 0; // 最后一个图片，0开始
    private int offset = 2; // 添加的偏移

    public Advertise2Adapter(Context context, List<Advertise> advertises) {
        this.advertises = advertises;
        this.context = context;
        initView();
    }

    private void initView() {
        views = new ArrayList<>();
        if (advertises == null) {
            return;
        }
        //循环滑动的时候为了顺畅 前后各加两张
        List<Advertise> mockData = new ArrayList<>();
        firstAd = 0;
        lastAd = 0;
        if (advertises.size() > 1) {
            mockData.add(advertises.get(advertises.size() - 2));
            mockData.add(advertises.get(advertises.size() - 1));
            firstAd = mockData.size();
            offset = 2;
        }
        mockData.addAll(advertises);
        lastAd = mockData.size() - 1;
        if (advertises.size() > 1) {
            mockData.add(advertises.get(0));
            mockData.add(advertises.get(1));
        }
        for (int i = 0; i < mockData.size(); i++) {
            Advertise advertiseInfo = mockData.get(i);
            GAImageView imageView = getImageView(advertiseInfo, i);
            views.add(imageView);
        }
    }

    protected GAImageView getImageView(Advertise advertiseInfo, int index) {
        final GAImageView imageView = new GAImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setGAString("homebanner" + index + ":" + advertiseInfo.ids);
        Glide.with(context)
                .load(advertiseInfo.advertisePath)
                .dontAnimate()
                .error(R.mipmap.bg_home_ad_banner_small)
                .placeholder(R.mipmap.bg_home_ad_banner_small)
                .transform(new GlideRoundTransform(context,6))
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                IMainModule mainModule = Router.INSTANCE.get(IMainModule.MODULE_NAME);
//                if(mainModule != null){
//                    mainModule.bannerJump(context, advertiseInfo.jumpThird, advertiseInfo.title, advertiseInfo.advertiseTargetUrl, advertiseInfo.advertiseType,advertiseInfo.showNav());
//                }
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if (views == null) {
            return 0;
        }
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    /**
     * 相对首张图片
     */
    public int getFirstAd() {
        return firstAd;
    }

    public int getLastAd() {
        return lastAd;
    }

    public int getOffset() {
        return offset;
    }
}
