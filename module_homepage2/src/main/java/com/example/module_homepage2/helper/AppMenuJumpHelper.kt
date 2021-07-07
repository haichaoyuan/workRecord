package com.shhxzq.ztb.ui.home.ui.helper

import android.app.Activity
import android.text.TextUtils
import com.example.module_homepage2.base.AppMenuRes
import com.example.module_homepage2.base.NumberUtil

/**
 * App 菜单跳转帮助类
 */
object AppMenuJumpHelper {
    fun menuClickImp(context: Activity, menuRes: AppMenuRes): Boolean {
        if (AppMenuRes.MENU_TYPE_H5.equals(menuRes.getMenuType())) {
//            context.startActivity(
//                Intent(
//                    context, WebViewActivity::class.java
//                ).putExtra(WebViewActivity.PARAMS_URL, menuRes.getTargetUrl())
//                    .putExtra(WebViewActivity.PARAMS_TITLE, menuRes.getMenuName())
//            )
            return true
        } else if (AppMenuRes.MENU_TYPE_SCHEME.equals(menuRes.getMenuType())) {
//            context.startActivity(
//                SchemeUtil.getSchemeIntent(
//                    context, menuRes.targetUrl
//                )
//            )
        } else {
            if (!TextUtils.isEmpty(menuRes.targetUrl)) {
                val type = NumberUtil.toInt(menuRes.targetUrl, -1)
                if (type == -1) {
//                    context.startActivity(
//                        SchemeUtil.getSchemeIntent(
//                            context, menuRes.targetUrl
//                        )
//                    )
                    return true
                } else {
                    // 根据type 来跳转
                    jumpByType(type, context)
                }
            }
        }
        return false
    }

    private fun jumpByType(type: Int, context: Activity) {
//        if (type / 100 == 1) {
//            jumpByTypeForQuota(type, context)
//        } else if (type / 100 == 2) {
//            jumpByTypeForTrade(type, context)
//        } else if (type / 100 == 3) {
//            jumpByTypeForFund(type, context)
//        } else if(type / 100 == 6){
//            jumpByTypeForBusinessTransaction(type, context)
//        }

    }
}