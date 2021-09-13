package com.example.module_homepage2.module

import android.content.Context
import android.content.Intent
import com.example.lib_util.utils.jump.IHomePageModule
import com.example.lib_util.utils.jump.ModuleMenu
import com.example.module_a.StickHeaderListViewActivity

class StickListModule : IHomePageModule {
    override fun jump(context: Context, ids: Int):Boolean {
        return when (ids) {
            ModuleMenu.TARGET_APPBAR_STICK_LIST -> {
                context.startActivity(Intent(context, StickHeaderListViewActivity::class.java))
                true
            }
            else -> {
                false
            }
        }
    }
}