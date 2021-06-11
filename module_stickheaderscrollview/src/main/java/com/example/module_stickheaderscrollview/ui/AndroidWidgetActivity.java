package com.example.module_stickheaderscrollview.ui;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.module_stickheaderscrollview.R;
import com.example.module_stickheaderscrollview.ui.adapter.TabFragmentAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;


/**
 * @author: JiangWeiwei
 * @time: 2018/8/2-10:05
 * @email: jiangweiwei@qccr.com
 * @desc:
 */
public class AndroidWidgetActivity extends AppCompatActivity {
    private AppBarLayout mAppBar;
    private CollapsingToolbarLayout mToolbarLayout;
    private ImageView mImageview;
    private Toolbar mToolbar;
    private TabLayout mTablayout;
    private ViewPager mVp;
    private TabFragmentAdapter mTabFragmentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_widget);
        initView();
        initUI();
    }

    private void initUI() {
        mTabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager());
        mVp.setAdapter(mTabFragmentAdapter);
        mVp.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
        mVp.setOffscreenPageLimit(3);
        mTablayout.setupWithViewPager(mVp);
    }

    private void initView() {
        mAppBar = findViewById(R.id.app_bar);
        mToolbarLayout = findViewById(R.id.toolbar_layout);
        mImageview = findViewById(R.id.imageview);
        mToolbar = findViewById(R.id.toolbar);
        mTablayout = findViewById(R.id.tablayout);
        mVp = findViewById(R.id.viewpage);
    }
}
