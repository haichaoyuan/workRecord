package com.example.module_appbarlayout

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.module_appbarlayout.homepage2.MyFragment
import com.example.module_appbarlayout.homepage2.MyFragmentPageAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.activity_homepage2.*

/**
 * 顶部是四个 Tab ，下面是 ViewPager + Fragment + RecyclerView 。
 * 如果在加上悬浮效果的话 ，那这个页面结构是 CoordinatorLayout + AppBarLayout + TabLayout + ViewPager + Fragment + RecyclerView 。
————————————————
版权声明：本文为CSDN博主「Super 含」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/qq_37492806/article/details/104926969
 */
class HomePage2Activity : AppCompatActivity() {
    private val CHANNELS = arrayOf("直播", "视频", "题库", "资料")
    private val fragmentList: ArrayList<Fragment> = ArrayList()

    var expendedtag = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_homepage2)
        initViewPager()
        init()
        initTabLayout()

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->

            if (expendedtag == 1) {
                banAppBarScroll(true)
                expendedtag = 2;
            } else {
                banAppBarScroll(false)
                expendedtag = 1;
            }
        }
    }

    private fun banAppBarScroll(isScroll: Boolean) {
//        val mAppBarChildAt: View = appbar.getChildAt(0)
//        val mAppBarParams = mAppBarChildAt.getLayoutParams() as AppBarLayout.LayoutParams
//        if (isScroll) {
//            mAppBarParams.scrollFlags =
//                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
//            mAppBarChildAt.setLayoutParams(mAppBarParams)
//        } else {
//            mAppBarParams.scrollFlags = 0
//            mAppBarChildAt.setLayoutParams(mAppBarParams)
//        }



        for(item in fragmentList){
            (item as MyFragment).setNestScrollEnable(isScroll)
        }
    }

    private fun init() {
        appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                System.out.println("appBarLayout: $appBarLayout, verticalOffset:$verticalOffset")
                if (supportActionBar!!.getHeight() - appBarLayout!!.getHeight() == verticalOffset) {
                    //折叠监听
                    Toast.makeText(this@HomePage2Activity, "折叠了", Toast.LENGTH_SHORT).show();
                }
                if (expendedtag == 2 && verticalOffset == 0) {
                    //展开监听
                    Toast.makeText(this@HomePage2Activity, "展开了", Toast.LENGTH_SHORT).show();
                }
                if (expendedtag != 2 && verticalOffset == 0) {
                    expendedtag++;
                }
            }
        })
    }

    private fun initTabLayout() {
        for (it in CHANNELS) {
            tab_layout.addTab(tab_layout.newTab().setText(it))
        }
        tab_layout!!.tabMode = TabLayout.MODE_SCROLLABLE
        tab_layout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent))
        tab_layout.setTabTextColors(Color.BLACK, ContextCompat.getColor(this, R.color.colorAccent))
        tab_layout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                //点击tab的时候，RecyclerView自动滑到该tab对应的item位置
                val position = tab.position
                viewPager.currentItem = position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun initViewPager() {
        val yaDianNaFragment = MyFragment(1)
        val yaDianNaFragment1 = MyFragment(2)
        val yaDianNaFragment2 = MyFragment(3)
        val yaDianNaFragment3 = MyFragment(4)
        fragmentList.add(yaDianNaFragment)
        fragmentList.add(yaDianNaFragment1)
        fragmentList.add(yaDianNaFragment2)
        fragmentList.add(yaDianNaFragment3)
        val collapsingTestAdapter = MyFragmentPageAdapter(
            this, fragmentList
        )
        viewPager.adapter = collapsingTestAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val tabAt: TabLayout.Tab? = tab_layout.getTabAt(position)
                if (tabAt != null && !tabAt.isSelected) {
                    tabAt.select()
                }
            }
        })
    }


}