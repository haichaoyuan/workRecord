package com.example.module_homepage2.nestwebview

import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import com.example.module_homepage2.BuildConfig
import com.example.module_homepage2.R
import com.example.module_homepage2.base.UIUtils
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

/**
 * Des: 带嵌套滚动的 webview
 */
class NestedScrollWebViewFragment : Fragment() {
    private lateinit var webView:NestedScrollWebView2
    private lateinit var url: String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_webview_base_with_nestscroll, null)
        webView = view.findViewById(R.id.webview)
        return view
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWebView(this.webView)
        url = getUrl()
        webView.loadUrl(url)
    }

    private fun getUrl(): String {
        if (arguments == null || TextUtils.isEmpty(requireArguments().getString(PARAMS_URL))) return ""
        val url = requireArguments().getString(PARAMS_URL)
        return url!!
    }


    private fun initWebView(webView: ProgressWebView) {
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        // 滑动到底部的阴影
        webView.overScrollMode = View.OVER_SCROLL_NEVER


        if (BuildConfig.DEBUG && UIUtils.hasKitkat()) {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        }
        val appCachePath: String = requireActivity().cacheDir.absolutePath
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptEnabled = true
        webView.settings.setAppCachePath(appCachePath)
        webView.settings.allowFileAccess = true
        webView.settings.setAppCacheEnabled(true)
        webView.settings.blockNetworkImage = false
        webView.settings.userAgentString = webView.settings.userAgentString + " /ztb_android"

        if (UIUtils.hasLollipop()) {
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

    }

    companion object {
        const val PARAMS_URL ="PARAMS_URL"
    }
}