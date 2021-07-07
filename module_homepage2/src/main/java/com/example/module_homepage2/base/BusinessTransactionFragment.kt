package com.example.module_homepage2.base

import com.shhxzq.ztb.ui.home.ui.helper.HelpFragment

/**
 * 业务办理 fragment,
 * 从 BusinessTransactionActivity 里抽离，仅仅供全部服务页面的业务办理类型里使用
 */
class BusinessTransactionFragment : HelpFragment() {
    companion object {
        fun getInstance(): BusinessTransactionFragment {
            return BusinessTransactionFragment()
        }
    }

}