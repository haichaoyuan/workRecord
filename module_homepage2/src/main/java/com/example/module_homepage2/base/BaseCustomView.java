package com.example.module_homepage2.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by lidaofu on 2015/4/28.
 * 自定义view的基类
 */
public abstract class BaseCustomView extends LinearLayout {

    private static final String TAG = "MyCustomView";

    public Context mContext;
    public LayoutInflater mInflater;
    public Resources resources;

    public BaseCustomView(Context context) {
        this(context, null);
    }

    public BaseCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        resources = getResources();
        setAttrs(attrs);
        initView(mInflater);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BaseCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }


    public abstract void setAttrs(AttributeSet attrs);

    public abstract void initView(LayoutInflater inflater);
}