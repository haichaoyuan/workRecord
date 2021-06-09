package com.example.module_stickheaderscrollview

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.module_stickheaderscrollview.stickscrollview.ChildRecyclerView
import com.example.module_stickheaderscrollview.stickscrollview.ChildScrollView
import com.example.module_stickheaderscrollview.stickscrollview.ScrollViewWithStickHeader
import com.example.module_stickheaderscrollview.ui.AndroidWidgetActivity
import com.example.module_stickheaderscrollview.ui.adapter.ReListAdapter
import com.example.module_stickheaderscrollview.ui.adapter.TabFragmentAdapter
import com.google.android.material.tabs.TabLayout
import java.util.*

class StickHeadScrollViewActivity : AppCompatActivity() {

    private val mTvStickHeader: TextView? = null
    private val mScrollviewChild: ScrollView? = null
    private val mRv: RecyclerView? = null
    private val mReListAdapter: ReListAdapter? = null

    private val TAG = "MainActivity"
    private var mOrderManagerTabs: TabLayout? = null
    private val mCsv: ChildScrollView? = null
    private var mVp: ViewPager? = null
    private var mTabFragmentAdapter: TabFragmentAdapter? = null
    private var mWebview: WebView? = null
    private var mLLStickList: LinearLayout? = null
    private var mButton: Button? = null
    private var mStickScrollView: ScrollViewWithStickHeader? = null
    private var mLl: LinearLayout? = null
    private val mCrv: ChildRecyclerView? = null
    private var mViewBottom: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stick_head_scroll_view)
        initView()
        initUI()
        mStickScrollView?.setContentView(mLLStickList)
        mStickScrollView?.setSuspensionView(mViewBottom)
        startActivity(Intent(this, AndroidWidgetActivity::class.java))
    }

    private fun initView() {
//        mTvStickHeader = (TextView) findViewById(R.id.tv_stick_header);
        mOrderManagerTabs = findViewById(R.id.order_manager_tabs) as TabLayout
        //        mCsv = (ChildScrollView) findViewById(R.id.csv);
        mVp = findViewById(R.id.vp) as ViewPager?
        mLLStickList = findViewById(R.id.ll_stick_list) as LinearLayout
        mWebview = findViewById(R.id.webview) as WebView
        mButton = findViewById(R.id.button) as Button
        mStickScrollView = findViewById(R.id.stick_scroll_view) as ScrollViewWithStickHeader?
        mLl = findViewById(R.id.ll) as LinearLayout
        mViewBottom = findViewById(R.id.tv_bottom) as View
//        mCrv = (ChildRecyclerView) findViewById(R.id.crv);
    }

    private fun initUI() {
        mTabFragmentAdapter = TabFragmentAdapter(supportFragmentManager)
        mVp?.setAdapter(mTabFragmentAdapter)
        mVp?.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(mOrderManagerTabs)
        )
        mVp?.setOffscreenPageLimit(3)
        mOrderManagerTabs!!.setupWithViewPager(mVp)
        val webSettings = mWebview!!.settings
        //设置WebView属性，能够执行Javascript脚本
        webSettings.javaScriptEnabled = true
        mWebview!!.loadUrl("https://segmentfault.com/u/wellijhon_58622d2f61c2d")

//        Log.d(TAG, "initUI: " + (mLl.getDescendantFocusability() == ViewGroup.FOCUS_BLOCK_DESCENDANTS));
    }
}