package com.example.module_homepage2

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.module_homepage2.adapter.AllServiceAdapter
import com.example.module_homepage2.adapter.HomeMenu2Adapter
import com.example.module_homepage2.base.AppMenuRes
import com.example.module_homepage2.base.UIUtils
import com.example.module_homepage2.listener.HomeMenuItemClickListener
import com.example.module_homepage2.nestwebview.ProgressWebView
import com.example.module_homepage2.viewmodel.AllServicesViewModel
import com.example.module_homepage2.xtablayout.XTabLayout
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_all_service.*
import kotlinx.android.synthetic.main.activity_all_service.tab_layout
import kotlinx.android.synthetic.main.activity_webview.*

class WebViewTestActivity : FragmentActivity() {
    companion object {
        const val TAG = "WebViewActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        initWebView(webview)

        webview.loadUrl("https://www.qq.com")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(webView: WebView) {
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        // 滑动到底部的阴影
        webView.overScrollMode = View.OVER_SCROLL_NEVER

//        webView.setDefaultHandler(DefaultHandler())
        webView.settings.domStorageEnabled = true //设置适应Html5
        val appCachePath = this.cacheDir.absolutePath
        webView.settings.setAppCachePath(appCachePath)
        webView.settings.allowFileAccess = true
        webView.settings.setAppCacheEnabled(true)
        webView.settings.blockNetworkImage = false
        webView.settings.setUserAgentString(webView.settings.userAgentString.toString() + "/ztb_android")

        if (UIUtils.hasLollipop()) webView.settings.mixedContentMode =
            WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        val webSettings = webView.settings;
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK; //设置缓存
        webSettings.javaScriptEnabled = true; //设置能够解析Javascript

//        webSettings.setUserAgentString("User-Agent:Android")
    }
}