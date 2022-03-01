package com.example.module_commonview.nestwebview

import androidx.fragment.app.FragmentActivity
import android.os.Bundle
import com.example.module_commonview.R
import kotlinx.android.synthetic.main.activity_nest_webview.*

/**
 * 参考子
 */
class NestWebViewActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nest_webview)
        webview.loadUrl("https://news.baidu.com/")
//        webview.loadUrl("https://m.baidu.com/?from=844b&vit=fps")
    }
}