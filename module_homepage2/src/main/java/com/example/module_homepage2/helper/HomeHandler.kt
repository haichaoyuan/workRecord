package com.shhxzq.ztb.ui.home.ui.helper

import android.os.Handler
import android.os.Message

/**
 * Handler 的封装
 */
class HomeHandler(val unit:(msg: Message?, handler:Handler)->Boolean): Handler() {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        unit.invoke(msg, this)
    }
}

class HomePageHandler(val unit:(msg: Message?, handler:Handler)->Boolean): Handler() {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        unit.invoke(msg, this)
    }
}