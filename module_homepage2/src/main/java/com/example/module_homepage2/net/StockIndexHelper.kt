package com.shhxzq.ztb.ui.home.ui.net


/**
 * 指数帮助类，开启、关闭后台socket 请求
 * 访问指数接口
 */
object StockIndexHelper {
    private var pushTag = -1

    /**
     * 唤起
     */
//    fun onResume(codeInfoList: List<CodeInfo>, unit:(StockQuoteInfo)-> Boolean) {
//        if (pushTag == -1) {
//            pushTag = pushApi.pushTag()
//        }
//        pushApi.monitorStockList(
//            pushTag, DataLevel.QUOTE_SIMPLE, codeInfoList,
//            object : IPushCallback<StockQuoteInfo> {
//                override fun onReceivePush(data: StockQuoteInfo) {
//                    unit.invoke(data)
//                }
//            })
//    }


    /**
     * pause
     */
//    fun onPause() {
//        pushApi.unregister(pushTag)
//    }
}