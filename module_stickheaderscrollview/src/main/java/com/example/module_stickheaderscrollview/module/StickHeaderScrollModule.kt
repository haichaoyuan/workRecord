package com.example.module_homepage2.module

import android.content.Context
import android.content.Intent
import com.example.lib_util.utils.jump.IAppBarModule
import com.example.lib_util.utils.jump.IHomePageModule
import com.example.lib_util.utils.jump.ModuleMenu
import com.example.module_stickheaderscrollview.StickHeadScrollViewActivity

class StickHeaderScrollModule : IHomePageModule {
    override fun jump(context: Context, ids: Int):Boolean {
        return when (ids) {
            ModuleMenu.TARGET_APPBAR_STICKHEADER_SCROLL -> {
                context.startActivity(Intent(context, StickHeadScrollViewActivity::class.java))
                true
            }
            else -> {
                false
            }
        }
    }
}