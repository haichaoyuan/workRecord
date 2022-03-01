/**
 * 公司:	 HTFFUND
 * 作者:	 吴领
 * 版本:	 1.0
 * 创建日期: 2014-1-28
 * 创建时间: 下午12:24:49
 */
package com.example.module_commonview.nestwebview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.JsResult;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.ProgressBar;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.module_commonview.R;


/**
 * 带进度条的WebView
 *
 */
public class ProgressWebView extends WebView {

    private ProgressBar progressbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean needNotRefresh;

    public ProgressWebView(Context context) {
        this(context, null);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setIndeterminateDrawable(getResources().getDrawable(android.R.drawable.progress_indeterminate_horizontal));
        progressbar.setProgressDrawable(getResources().getDrawable(R.drawable.layer_list_progress_yellow));
        progressbar.setLayoutParams(new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.FILL_PARENT,
                getResources().getDimensionPixelSize(R.dimen.padding_tiny), 0, 0));
        addView(progressbar);
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message,
                                   JsResult result) {
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            // android 6.0 以下通过title获取
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (title.contains("404") || title.contains("500") || title.contains("Error")) {
                    if (onLoadListener != null)
                        onLoadListener.onError(404);
                }
            }

        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);

        if (swipeRefreshLayout != null && !needNotRefresh)
            if (this.getScrollY() == 0) {
                swipeRefreshLayout.setEnabled(true);
            } else {
                swipeRefreshLayout.setEnabled(false);
            }
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void setOnLoadListener(ProgressWebView.onLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    private onLoadListener onLoadListener;

    public interface onLoadListener {
        void onError(int code);
    }

    public void setNeedNotRefresh(boolean needNotRefresh) {
        this.needNotRefresh = needNotRefresh;
    }

}