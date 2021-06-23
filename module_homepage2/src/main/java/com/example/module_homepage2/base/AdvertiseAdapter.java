package com.example.module_homepage2.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.Transition;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Des:首页广告
 * Author: Luke
 * Data: 17/5/24
 */
public class AdvertiseAdapter extends PagerAdapter {
    private Context context;
    private List<Advertise> advertises;
    private List<ImageView> views;

    public AdvertiseAdapter(Context context, List<Advertise> advertises) {
        this.advertises = advertises;
        this.context = context;
        initView();
    }

    private void initView() {
        views = new ArrayList<>();
        if (advertises == null) {
            return;
        }
        //循环滑动的时候为了顺畅 前后各加一张
        List<Advertise> mockData = new ArrayList<>();
        if (advertises.size() > 1) {
            mockData.add(advertises.get(advertises.size() - 1));
        }
        for (int i = 0; i < advertises.size(); i++) {
            mockData.add(advertises.get(i));
        }
        if (advertises.size() > 1) {
            mockData.add(advertises.get(0));
        }
        for (int i = 0; i < mockData.size(); i++) {

            Advertise advertiseInfo = mockData.get(i);
            final GAImageView imageView = new GAImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setGAString("homebanner" + i + ":" + advertiseInfo.ids);

            DrawableCrossFadeFactory drawableCrossFadeFactory =
                    new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
            Glide.with(context)
                    .load(advertiseInfo.advertisePath)
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .into(new CustomTarget<Drawable>() {
                              @Override
                              public void onResourceReady(@NonNull @NotNull Drawable resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Drawable> transition) {
                                  imageView.setBackground(resource);
                              }

                              @Override
                              public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {

                              }
                          });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //思迪理财跳转特殊处理
//                    if(Advertise.THIRD_SD.equals(advertiseInfo.jumpThird)){
//                        SDHelper.INSTANCE.startActivity(context,advertiseInfo.advertiseTargetUrl);
//                        return;
//                    }
//
//
//                    if (!TextUtils.isEmpty(advertiseInfo.advertiseTargetUrl)) {
//                        if (Advertise.TARGET_TYPE_H5.equals(advertiseInfo.advertiseType)) {
//                            context.startActivity(new Intent(context, WebViewActivity.class)
//                                    .putExtra(WebViewActivity.PARAMS_URL, advertiseInfo.advertiseTargetUrl)
//                                    .putExtra(WebViewActivity.PARAMS_TITLE, advertiseInfo.title));
//                        }
//                        else {
//                            // 首页广告增加基金的跳转
//                            IMainModule mainModule = Router.INSTANCE.get(IMainModule.Companion.getMODULE_NAME());
//                            if(mainModule != null){
//                                mainModule.schemeJump((Activity) context, advertiseInfo.advertiseTargetUrl);
//                            }
//                        }
//
//                    }
                }
            });
            views.add(imageView);
        }
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
}
