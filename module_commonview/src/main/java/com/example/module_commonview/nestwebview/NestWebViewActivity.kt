package com.example.module_commonview.nestwebview

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import androidx.fragment.app.FragmentActivity
import com.example.lib_util.utils.UIUtil
import com.example.module_commonview.R
import kotlinx.android.synthetic.main.activity_nest_webview.*

/**
 * 参考子
 */
class NestWebViewActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nest_webview)

        // 滑动到底部的阴影
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)
        webview.setOverScrollMode(View.OVER_SCROLL_NEVER)

        webview.getSettings().setDomStorageEnabled(true)
        val appCachePath: String = this.getCacheDir().getAbsolutePath()
        webview.getSettings().setAppCachePath(appCachePath)
        webview.getSettings().setAllowFileAccess(true)
        webview.getSettings().setAppCacheEnabled(true)
        webview.getSettings().setBlockNetworkImage(false)
        webview.getSettings().setUserAgentString(
            webview.getSettings().getUserAgentString().toString() + "/ztb_android"
        )

        if (UIUtil.hasLollipop()) {
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
        }

        webview.loadUrl("https://www.cnblogs.com/")
    }
}