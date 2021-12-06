package com.example.module_scanidbankcard.util;

import android.os.Handler;
import android.os.Looper;

/**
 * Des:
 * Author: yangmingjuan
 * Data: 16/6/21
 */
public class HandlerUtil {
    //主线程的handle
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private HandlerUtil() {
    }

    /**
     * @param callback
     * @return
     */
    public static void post(Runnable callback) {
        if (callback != null) sHandler.post(callback);
    }

    /**
     * @param callback
     * @param delayMillis
     * @return 返回参数callback本身 (这样做的意义在于, 参数为一个匿名callback的时候也可以返回, 方便跟踪callback; 有可能activity销毁时需要移出callback以避免内存泄漏)
     */
    public static Runnable postDelayed(Runnable callback, long delayMillis) {
        if (callback != null) {
            sHandler.postDelayed(callback, delayMillis);
        }
        return callback;
    }

    /**
     * 移出一个callback (特别是对于延迟消息[postDelayed], 删除callback以避免内存泄漏)
     *
     * @param callback
     */
    public static void removeCallback(Runnable callback) {
        if (callback != null)
            sHandler.removeCallbacks(callback);
        else
            sHandler.removeCallbacksAndMessages(null);
    }

    public static Handler getHandler() {
        return sHandler;
    }

}
